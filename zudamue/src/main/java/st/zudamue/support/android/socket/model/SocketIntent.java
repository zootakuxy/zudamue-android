package st.zudamue.support.android.socket.model;

/**
 * Created by xdaniel on 4/24/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public enum SocketIntent {
    REGISTER ( "register" ),
    UNREGISTER ( "unregister" ),
    SEND ( "send" ),
    OTHER("other"),
    NOTIFY("notify");


    private String intent;

    SocketIntent ( String intent ){
        this.intent = intent;
    }

    public String getIntent(){
        return this.intent;
    }
}
