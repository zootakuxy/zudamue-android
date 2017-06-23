package st.zudamue.support.android.socket.model;

import com.google.gson.annotations.Expose;

/**
 * Created by daniel on 5/25/17.
 */

public class SocketReceiver {

    @Expose
    private String group;

    @Expose
    private String client;

    public SocketReceiver(String group, String client ) {
        this.group = group;
        this.client = client;
    }

    public String getGroup() {
        return group;
    }

    public String getClient() {
        return client;
    }

    public static SocketReceiver newInstance(String groupId, String clientId) {
        return new SocketReceiver( groupId, clientId );
    }
}
