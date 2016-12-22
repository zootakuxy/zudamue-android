/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package st.domain.support.android.socket;

import android.app.Activity;
import android.util.Log;

import st.domain.support.android.model.Identified;
import com.thoughtworks.xstream.XStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AhmedJorge
 * @author xdata
 */
public class ClientSocketListener implements Serializable
{
	private final String clientIdentifier;
	private final NetIntentConverter netIntentConverter;
	private final XStream xStream;
	private final int port;

	private String host;
    private Activity activity;
	private HashMap<CharSequence, OnTreatNetIntent> listTreater;
    
	private Socket server;
	private Client clientListner;

	Thread controleThread;
	private boolean run;
	private boolean abort;
	private String serverName;
	private int serverConectKey;
	private String connectMessage;


	public ClientSocketListener(String host, int port, String clientIdentifier, Activity activity, OnTreatNetIntent defaultOntreater)
	{
		this.host = host;
		this.port = port;
		this.clientIdentifier = clientIdentifier;
		this.netIntentConverter = new NetIntentConverter();
		this.xStream = new XStream();
		this.serverConectKey = 100;
		this.listTreater = new HashMap<>();
		this.serverName = "SERVER";
		this.connectMessage = "Estabelecendo o mapamento de coexao";
		this.activity = activity;

		this.listTreater.put(defaultOntreater.getProtocolKey(), defaultOntreater);
		this.xStream.registerConverter(this.netIntentConverter);
		this.xStream.alias(this.netIntentConverter.getTagRoot(), NetIntent.class);
	}

	/**
	 * Proucura pelo servidor da aplicacao
	 * @return
	 */
	private Thread simpleConnect()
	{
		Log.i("DBA:APP.TEST", getClass().getSimpleName()+"-> PROCDURANDO PELO SERVIDOR DA APLICACAO "+host);
		Thread creator = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					server = new Socket(host, port);
					clientListner = new Client(server);
					runService();
					ClientSocketListener.this.register();
				} catch (Exception e)
				{
					Log.e("DBA:APP.TEST", "ERRO AO PROCURAR O SERVER | INIT SOCKT", e);
					NetIntent intent = new NetIntent();
					intent.setResult(NetIntent.Result.FAILED);
					intent.setIntent(NetIntent.Intent.CONNECT);
					receivedIntentConnection(intent);
				}
			}
		});
		creator.start();
		return creator;
	}

	/**
	 * Connect with server
	 */
	public void connect()
	{
		simpleConnect();
	}

	public boolean connectKeepIn(int millisTime)
	{
		try
		{
			Thread result = simpleConnect();
			result.join(millisTime);
			return this.isRunming();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private void register()
	{
		Log.i("DBA:APP.TEST", getClass().getSimpleName()+"-> REGISTRING SERVER...");
		NetIntent intent = new NetIntent(this.clientIdentifier, this.serverName, this.serverConectKey, NetIntent.Intent.CONNECT, this.connectMessage);
		boolean result = this.sendIntent(intent);
		Log.i("DBA:APP.TEST", "CONNECTION REQUEST SEND "+result);
	}

	public void setServerName(String serverName)
	{
		this.serverName =  serverName;
	}

	public void setServerConectKey(int serverConectKey) {
		this.serverConectKey = serverConectKey;
	}

	public void setConnectMessage(String connectMessage) {
		this.connectMessage = (connectMessage == null || connectMessage.trim().length() == 0) ? "" : connectMessage;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	/**
	 * PARAR O SERCICO DE CONNEXAO COM O SERVIDOR
	 */
	public void stop()
    {
		Log.i("DBA:APP.TEST",getClass().getSimpleName()+ "-> STOPING SERVICE");
		try 
		{
			this.stopRun();
			this.clientListner.stop();
			this.server.close();
			Log.i("DBA:APP.TEST",getClass().getSimpleName()+ "->SERVICE STOPPED");
		} catch (Exception e) 
		{
			Log.e("DBA:APP.TEST",getClass().getSimpleName()+ "->STOP SERVICE FAILED");
		}
	}

	/**
	 * Verrificar se ha connexao com o servidor
	 * @return
	 */
	public boolean hasConection()
	{
		boolean hasConnection = this.clientListner != null
				&& this.server != null
				&& this.server.isConnected()
				&& !this.server.isClosed();
		Log.i("DBA:APP.TEST", getClass().getSimpleName()+"-> Has Connection : "+hasConnection);
		if(!hasConnection)
		{
			Log.w("DBA:APP.TEST", getClass().getSimpleName()+"-> SERVER : "+server);
			if(server != null)Log.w("DBA:APP.TEST", getClass().getSimpleName()+"-> SERVER.isConnected : "+server.isConnected());
			if(server != null) Log.w("DBA:APP.TEST", getClass().getSimpleName()+"-> SERVER.isClosed : "+server.isConnected());
		}
		return hasConnection;
	}

	public boolean isRunming() 
	{
		return this.run;
	}
	
	private void stopRun()
	{
		this.run = false;
		if(controleThread != null)
			controleThread.interrupt();
		this.controleThread = null;
		if(this.clientListner != null)
			this.clientListner.stop();
	}

	public void setHost(String host)
    {
		this.host = host;
		this.stop();
		this.host = host;
		this.connect();
    }
    
    public void addTreater(OnTreatNetIntent onTreatNetIntent)
    {
		if(listTreater.containsKey(onTreatNetIntent.getProtocolKey()))
			listTreater.remove(onTreatNetIntent.getProtocolKey());
		this.listTreater.put(onTreatNetIntent.getProtocolKey(), onTreatNetIntent);
    }

    /**
     * ENVIARA OS DADOS PARA O SERVIDOR
     * @param intent
     * @return
     */
    public boolean sendIntent(NetIntent intent)
    {
		intent.setSendTime(new Date());
		intent.setResult(NetIntent.Result.WAIT);
    	return this.hasConection() && this.clientListner.sentIntentC(intent);
    }
	
	 /**
     * COLOCAR O SERVICO EM EXECUSAO
     */
	private void runService()
	{
		Log.i("DBA:APP.TEST", getClass().getSimpleName()+"-> .runService called");
		if(isRunming())
		{
			if(this.controleThread != null)
				this.controleThread.interrupt();
		}
		this.run = true;
		this.controleThread = new Thread(clientListner);
		controleThread.start();
	}


	/**
	 * On result connection recived
	 * @param intent
	 */
	private void receivedIntentConnection(final NetIntent intent)
	{
		if(intent != null
				&& intent.getIntent() == NetIntent.Intent.CONNECT)
		{
			if(intent.getResult() == NetIntent.Result.FAILED)
			{
				this.run = false;
				if(this.controleThread != null)
					controleThread.interrupt();
				if(this.clientListner != null)
					this.clientListner.disconnect();

				this.controleThread = null;
				this.clientListner = null;
			}
			this.alertActivity(new Runnable()
			{
				@Override
				public void run()
				{
					for(Map.Entry<CharSequence, OnTreatNetIntent> treatNetIntent : listTreater.entrySet())
					{
						treatNetIntent.getValue().onConnectionResult((intent.getResult()== NetIntent.Result.SUCESS)? NetIntent.ConnectionStatus.SUCCESS
								: NetIntent.ConnectionStatus.FAILED);
					}
				}
			});
		}
	}

	/**
	 * Executar um dado runable na activide de modo a poder atulizar as componetes na activites
	 * @param runnable
     */
	private void alertActivity(final Runnable runnable)
	{
		synchronized (this)
		{
			this.activity.runOnUiThread(runnable);
		}
	}

	/**
	 * Verificar se existe conexão em pe
	 * @return
     */
	public boolean isConnected()
	{
		return this.server != null
				&& this.server.isConnected();
	}

	private class Client implements Runnable
    {
        private ObjectInputStream in;
        private ObjectOutputStream out;
        
        public Client(Socket socket) 
        {
        	try 
        	{
				this.in = new ObjectInputStream(socket.getInputStream());
				this.out = new ObjectOutputStream(socket.getOutputStream());
			} catch (Exception e) 
			{
				Log.e("DBA:APP.TEST",getClass().getSimpleName()+ "-> ERROR AO CRIAR O CLIENTE LISTENER: "+e.getMessage(), e);
			}
        	
		}

		public void stop() 
		{
			Log.i("DBA:APP.TEST",getClass().getSimpleName()+ "-> STOPING LISTINER...");
			try 
			{
				this.in.close();
				this.out.close();
			} catch (Exception e)
			{
				Log.i("DBA:APP.TEST",getClass().getSimpleName()+ "-> LISTINER STOPING FAILED");
			}
			Log.i("DBA:APP.TEST",getClass().getSimpleName()+ "-> LISTINER STOPED");
		}
		
		@Override
		public void run() 
		{
			Log.i("DBA:APP.TEST",getClass().getSimpleName()+ "->RUNNING...");
			try 
			{
				String xml;
				while((xml = (String) this.in.readObject()) != null)
				{
					try
					{
						final NetIntent intent = (NetIntent) xStream.fromXML(xml);
						Log.i("DBA:APP.TEST", getClass().getSimpleName()+"-> intent: "+intent);
						if (intent.getIntent() == NetIntent.Intent.CONNECT
								|| intent.getIntent() == NetIntent.Intent.DISCONNECT)
						{
							ClientSocketListener.this.receivedIntentConnection(intent);
							continue;
						}
						treatIntent(xml);
					}
					catch (Throwable tr)
					{
						Log.e("DBA:APP.TEST", getClass().getSimpleName()+"-> Error in treat the xml:\n "+xml);
						tr.printStackTrace();
					}
				}
			} catch (Exception e) 
			{
				Log.e("DBA:APP.TEST",getClass().getSimpleName()+ "->CONNECTION ERROR "+e.getMessage(), e);
				this.connectionLost();
				this.disconnect();
			}
			Log.i("DBA:APP.TEST",getClass().getSimpleName()+ "-> END RUMING...");
		}

		/**
		 * Tratar o resultdao
         */
		public void treatIntent( final String xml)
		{
			alertActivity(new Runnable() {
				@Override
				public void run() {
					OnTreatNetIntent treater;
					NetIntent result;
					boolean canTreat;
					for(int i =0; i<listTreater.size(); i++)
					{
						result = null;
						NetIntent intent = (NetIntent) xStream.fromXML(xml);
						treater = listTreater.get(i);
						canTreat = treater.canTreat(intent);
						if(canTreat) result = treater.treat(intent);
						if(canTreat && result != null) sendIntent(result);
					}
				}
			});
		}

		public void disconnect() 
		{
			try
			{
				run = false;
				this.in.close();
				this.out.close();
				server.shutdownInput();
				server.shutdownInput();
				server.close();
				controleThread.interrupt();
				server = null;
				clientListner = null;
				in = null;
				out = null;
				controleThread = null;
			}catch(Exception ex)
			{
				Log.e("DBA:APP.TEST",getClass().getSimpleName()+ "-> ERROR IN DISCONNECTION");
				ex.printStackTrace();
			}
		}

		private void connectionLost() 
		{
			Log.i("DBA:APP.TEST", getClass().getSimpleName()+"-> Connectin Losted");
			alertActivity(new Runnable()
			{
				@Override
				public void run() {
					for(Map.Entry<CharSequence, OnTreatNetIntent> treatNetIntent : listTreater.entrySet())
						treatNetIntent.getValue().onConnectionResult(NetIntent.ConnectionStatus.LOSTED);
				}
			});
		}

		/**
		 * Servico de envio de xml para o servidor
		 * @param data
		 * @return 
		 */
		private boolean sentIntentC(NetIntent data)
		{
			try 
			{
				String xml = xStream.toXML(data);
				Log.i("DBA:APP.TEST", getClass().getSimpleName()+ "-> SENDING XML...");
				Log.i("DBA:APP.TEST", getClass().getSimpleName()+ "-> XML: "+xml);
				this.out.writeObject(xml);
				Log.i("DBA:APP.TEST", "DBA:APP.TEST");
				return true;
			}
			catch (IOException e) 
			{
				Log.e("DBA:APP.TEST",getClass().getSimpleName()+ "-> DBA:APP.TEST"+e.getMessage(), e);
				return false;
			}
		}
    }
	
	
	public interface OnTreatNetIntent extends Identified
	{
		/**
		 * Verifi if is possible treat the net intent
		 * @param intent the net intent can cantreater
         * @return true if can treat and false if not treat
         */
		boolean canTreat(NetIntent intent);


		/**
		 * Treater the net intent if is possible treat
		 * @param intent
         * @return
         */
		NetIntent treat(NetIntent intent);

		/**
		 * on connection result altered
		 * @param status
         */
		void onConnectionResult(NetIntent.ConnectionStatus status);
	}
}
