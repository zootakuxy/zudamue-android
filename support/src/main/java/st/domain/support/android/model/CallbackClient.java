package st.domain.support.android.model;

import android.os.Bundle;

public interface CallbackClient extends Identified
{
	/**
	 * Os tipos de envios em relacao aos fragmentos
	 * @author xdata
	 *
	 */
	public enum SendType
	{
		/**
		 * O valor sera enviado para todos os fragmentos
		 */
		ALL,
		
		/**
		 * O valor sera enviado para um fragmento expecifico
		 */
		ONE
	}



	/**
	 * @param sendType o tipo de envio para todos ao apenas para a activite
	 * Recoperar o valor enviado de um outro fragmento
	 * @param origem O fragmento que enviou o valor
	 * @param summary O codigo do valor
	 * @param values O valor enviado

	 */
	public void onReceive(SendType sendType, CallbackClient origem, String summary, Object[] values);


	/**
	 * O metudo usado pelo servidor para obter informacao sobre o cliente
	 * @param queryQuention
	 * @return
	 */
	public Bundle query(CallbackClient clientOrigen, String queryQuention, Object... inParams);

}
