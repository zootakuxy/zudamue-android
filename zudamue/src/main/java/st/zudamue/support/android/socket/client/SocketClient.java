package st.zudamue.support.android.socket.client;


import android.util.Log;

import com.google.gson.Gson;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import st.zudamue.support.android.socket.model.SocketReceiver;
import st.zudamue.support.android.socket.model.Packager;
import st.zudamue.support.android.socket.model.SocketIntent;
import st.zudamue.support.android.util.JsonMapper;


/**
 * Created by xdaniel on 4/24/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class SocketClient {

    private static final String WEB_SOCKET_LINK_FORMATER = "ws://$host:$port";
    private final String host;
    private final int port;
    private WebSocketClient webSocketClient;
    private String group;
    private String client;
    private String key;
    private boolean registered;

    private SocketListiner.OnSocketEvent onSocketMessage;
    private SocketListiner.OnSocketOpen onSocketOpen;
    private SocketListiner.OnSocketClose onSocketClientClose;
    private SocketListiner.OnSocketError onSocketClientError;
    private SocketListiner.OnSocketInvalidMessage onSocketInvalidMessage;
    private SocketListiner.OnSocketInvalidPackager onSocketInvalidPackager;
    private SocketListiner.OnSocketRegisterResult onSocketRegister;
    private SocketListiner.OnSocketUnregister onSocketUnregister;
    private SocketListiner.OnSocketSend onSocketSend;
    private SocketListiner.OnSocketOtherIntent onSocketOtherIntent;
    private SocketListiner.OnSocketNotify onSocketNotify;
    private Object registerContent;
    private int timeOut;


    public SocketClient( String hostServer, int port, String group, String client ) {
        this.host = hostServer;
        this.port = port;
        this.group = group;
        this.client = client;
        this.timeOut = 1000 * 5;
    }

    public String createLink() {
        return WEB_SOCKET_LINK_FORMATER
                .replace( "$host", this.host)
                .replace("$port", String.valueOf( this.port ) )
                ;
    }


    public void register( final Object content ){
        try{
            this.registerContent = content;
            if( this.webSocketClient != null && this.getConnection().isConnecting() ){
                return;
            }
            else if( this.webSocketClient != null && this.getConnection().isOpen() ){
                this.close();
                this.init();
            }
            else {
                this.init();
            }

            this.webSocketClient.connect();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }




    private void init(  ){
        URI uri;
        try {
            uri = new URI( this.createLink() );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        createClientSocket(uri);
    }

    private void createClientSocket(final URI uri) {
        webSocketClient = new WebSocketClient(uri, new Draft_10(), null, this.timeOut ) {
            @Override
            public void onOpen( ServerHandshake handshakedata ) {

                Packager packager = createRegisterPackager( registerContent );
                String text = packager.toJson();
                Log.i( "scan.log.send.rg", text );
                SocketClient.this.send( text );
                if( onSocketOpen != null )
                    onSocketOpen.onClientOpen( handshakedata );
            }

            @Override
            public void onMessage( String message ) {
                onSocketMessage( message );
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if( onSocketClientClose != null )
                    onSocketClientClose.onSocketClose( code, reason, remote );
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
                if( onSocketClientError != null )
                    onSocketClientError.onSocketError( ex );
            }
        };
    }

    private boolean send( String text) {
        try{
            Log.i("scan.log.send", text );
            if( getConnection().isOpen() )
                this.webSocketClient.send( text );
            return true;
        } catch ( Exception ex ){
            ex.printStackTrace();
        }
        return false;
    }


    public WebSocket getConnection() {
        return webSocketClient.getConnection();
    }





    public boolean sendContent( Object content, SocketReceiver receiver ) {
        Packager packager = this.createSenderPackager( content, receiver );
        String text = packager.toJson();
        Log.i("scan.log.send.sc", text );
        return send(text);
    }


    public boolean other( String intentName, Object content ) {

        Packager packager = this.createOtherPackager( intentName,  content );
        String text = packager.toJson( );
        Log.i( "scan.log.send.other", text );
        return send(text);


    }

    private void onSocketMessage( String message ) {
        try{
            //Log.i("scan.log", message );
            if( onSocketMessage != null)
                this.onSocketMessage.onSocketEvent( message );
        } catch ( Throwable ex){
            ex.printStackTrace();
        }

        try {
            JsonMapper pack = new JsonMapper( message );

            if( ! pack.has( Packager.GROUP )
                    || !pack.has( Packager.CLIENT )
                    || !pack.has( Packager.CONTENT )
                    || !pack.has( Packager.TIMEOUT )
                    || !pack.has( Packager.TIMESERVER )
                    || !pack.has( Packager.INTENT )
                    || !pack.has( Packager.RECEIVER )
                    ) {
                if( this.onSocketInvalidPackager != null )
                    this.onSocketInvalidPackager.onSocketInvalidPackager( message, pack );
                return;
            }

            //REGISTER
            Gson gson = new Gson();
            Packager packager = gson.fromJson( message, Packager.class );
            packager.adjust();

            if( packager.getSocketIntent() == SocketIntent.REGISTER ) {
                if( packager.getKey() != null && packager.getResponse() != null ){
                    pack.rootMap( (Map) packager.getResponse() );
                    if (pack.booleaner( "result") ){
                        this.key = packager.getKey();
                        if( this.onSocketRegister != null )
                            onSocketRegister.onRegisterSuccess( this.key, packager );
                        return;
                    }
                }

                if( onSocketRegister != null ) onSocketRegister.onRegisterFailed( packager );
            }

            //UNREGISTER
            else if ( packager.getSocketIntent() == SocketIntent.UNREGISTER ){
                this.key = null;
                this.registered = false;
                if( this.onSocketUnregister != null )
                    this.onSocketUnregister.onSocketUnregister( packager );

            }

            else if ( packager.getSocketIntent() == SocketIntent.SEND ) {
                if( packager.getGroup().equals( this.group )
                        && packager.getClient().equals( this.client )
                        && packager.getResponse() != null ){
                    Map<String, Object> response = (Map) packager.getResponse();
                    pack.rootMap( response );
                    if( this.onSocketSend != null )
                        onSocketSend.onSendResult( packager, pack.booleaner("result"), response );
                }
                else
                    this.onSocketSend.onReceiverMessage( packager, packager.getGroup(), packager.getClient(), packager.getContent() );
            }

            //On socket notify
            else if ( packager.getSocketIntent() == SocketIntent.NOTIFY ) {
                if( onSocketNotify != null )
                    onSocketNotify.onSocketNotify( packager );
            }

            //Other intent
            else if ( packager.getSocketIntent() == SocketIntent.OTHER ){
                if( this.onSocketOtherIntent != null )
                    this.onSocketOtherIntent.onOtherIntent( packager.getIntent(), packager );
            }


        } catch (Exception e) {
            e.printStackTrace();
            if( this.onSocketInvalidMessage != null )
                this.onSocketInvalidMessage.onInvalidMessage( message );

        }
    }

    private Packager createOtherPackager(String intentName, Object content ) {
        return this.createEmptyPackager( SocketIntent.OTHER,  "." , intentName)
                .setContent( content )
                .setKey( this.key )
                ;
    }

    private Packager createSenderPackager(Object content, Object receiver ) {
        return this.createEmptyPackager( SocketIntent.SEND,  receiver, null)
                .setContent( content )
                .setKey( this.key )
                ;
    }

    private Packager createRegisterPackager(Object content ){
        return this.createEmptyPackager(  SocketIntent.REGISTER, ".", null)
                .setContent( content )
                ;
    }

    private Packager createEmptyPackager (SocketIntent intent, Object receiver, String otherIntentName ) {
        return new Packager( this.group, this.client, intent, receiver, otherIntentName);
    }


    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public void setOnSocketOpen(SocketListiner.OnSocketOpen onSocketOpen) {
        this.onSocketOpen = onSocketOpen;
    }

    public void setOnSocketClientClose(SocketListiner.OnSocketClose onSocketClientClose) {
        this.onSocketClientClose = onSocketClientClose;
    }

    public void setOnSocketClientError(SocketListiner.OnSocketError onSocketClientError) {
        this.onSocketClientError = onSocketClientError;
    }

    public void setOnSocketInvalidMessage(SocketListiner.OnSocketInvalidMessage onSocketInvalidMessage) {
        this.onSocketInvalidMessage = onSocketInvalidMessage;
    }

    public void setOnSocketInvalidPackager(SocketListiner.OnSocketInvalidPackager onSocketInvalidPackager) {
        this.onSocketInvalidPackager = onSocketInvalidPackager;
    }

    public void setOnSocketRegister(SocketListiner.OnSocketRegisterResult onSocketRegister) {
        this.onSocketRegister = onSocketRegister;
    }

    public void setOnSocketUnregister(SocketListiner.OnSocketUnregister onSocketUnregister) {
        this.onSocketUnregister = onSocketUnregister;
    }

    public void setOnSocketSend(SocketListiner.OnSocketSend onSocketSend) {
        this.onSocketSend = onSocketSend;
    }

    public void setOnSocketMessage( SocketListiner.OnSocketEvent onSocketMessage) {
        this.onSocketMessage = onSocketMessage;
    }

    public void setOnSocketOtherIntent(SocketListiner.OnSocketOtherIntent onSocketOtherIntent) {
        this.onSocketOtherIntent = onSocketOtherIntent;
    }

    public void setOnSocketNotify(SocketListiner.OnSocketNotify onSocketNotify) {
        this.onSocketNotify = onSocketNotify;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public WebSocketClient getWebSocketClient() {
        return webSocketClient;
    }

    public String getGroup() {
        return group;
    }

    public String getClient() {
        return client;
    }

    public String getKey() {
        return key;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void close() {
        try{
            if( getConnection().isClosed() || getConnection().isClosing() ) return;
            this.key = null;
            this.registered = false;
            this.getConnection().close(CloseFrame.NORMAL);
            Log.i("scan.log", "Closing Blocking");
        } catch ( Exception ex ){
            ex.printStackTrace();
        }
    }
}
