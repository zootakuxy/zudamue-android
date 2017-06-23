package st.zudamue.support.android.socket.model;

/**
 * Created by siie2 on 4/24/17.
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
