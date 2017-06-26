package st.zudamue.support.android.socket.client;

import org.java_websocket.handshake.ServerHandshake;

import java.util.Map;

import st.zudamue.support.android.socket.model.Packager;
import st.zudamue.support.android.util.JsonMapper;

/**
 * Lista de eventos do socket
 *
 * Created by xdaniel on 4/25/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */
public interface SocketListiner {

    interface OnSocketClose {

        void onSocketClose(int code, String reason, boolean remote);
    }

    interface OnSocketError {
        void onSocketError(Exception ex);
    }

    interface OnSocketInvalidMessage {
        void onInvalidMessage( String message );
    }

    interface OnSocketInvalidPackager {
        void onSocketInvalidPackager(String message, JsonMapper packager );
    }

    interface OnSocketEvent {
        void onSocketEvent(String message );
    }

    interface OnSocketOpen {
        void onClientOpen(ServerHandshake handshakedata );
    }

    interface OnSocketRegisterResult {

        void onRegisterSuccess( String key, Packager packager );

        void onRegisterFailed( Packager packager );
    }

    interface OnSocketSend {

        void onReceiverMessage(Packager packager, String group, String client, Object content );

        void onSendResult(Packager packager, boolean result, Map<String, Object> response );

    }

    interface OnSocketUnregister {
        void onSocketUnregister( Packager packager );
    }

    interface OnSocketNotify {
        void  onSocketNotify ( Packager packager );
    }

    interface OnSocketOtherIntent {
        void onOtherIntent(String intent, Packager packager);
    }

}
