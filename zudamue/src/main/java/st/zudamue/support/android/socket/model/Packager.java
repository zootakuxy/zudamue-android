package st.zudamoe.support.android.socket.model;


import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.Calendar;

/**
 * Created by siie2 on 4/24/17.
 */

public class Packager {
    public static final String GROUP = "group";
    public static final String CLIENT = "client";
    public static final String KEY = "key";
    public static final String INTENT = "intent";
    public static final String RECEIVER = "receiver";
    public static final String CONTENT = "content";
    public static final String TIMEOUT = "timeout";
    public static final String TIMESERVER = "timeserver";
    public static final String RESPONSE = "response";

    @Expose
    private String group;

    @Expose
    private String client;

    @Expose
    private String key;

    private SocketIntent socketIntent;

    @Expose
    private Object receiver;

    @Expose
    private Object content;

    @Expose
    private long timeout;

    @Expose
    private String intent;

    private Object response;
    private long timeserver;

    public Packager(String group, String client, SocketIntent intent, Object receiver, String otherIntentName) {
        if( intent == null ) throw  new RuntimeException( "Intent can not be null ");
        if( intent == SocketIntent.OTHER && (otherIntentName == null || otherIntentName.length() == 0) )
            throw new RuntimeException("Other intent require name");
        this.group = group;
        this.client = client;
        this.receiver = receiver;
        this.socketIntent = intent;
        this.intent =  intent == SocketIntent.OTHER ? otherIntentName : intent.getIntent();
        this.timeout = Calendar.getInstance().getTime().getTime();
    }

    public void adjust() {
        this.socketIntent = this.intent !=null?  SocketIntent.OTHER: null;

        for( SocketIntent intent : SocketIntent.values() ){
            if( intent.getIntent().toLowerCase().equals( this.intent ) ) {
                this.socketIntent = intent;
                break;
            }
        }
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

    public Packager setKey(String key) {
        this.key = key;
        return this;
    }

    public SocketIntent getSocketIntent() {
        return socketIntent;
    }

    Packager setIntent(String intent) {
        if( this.socketIntent == SocketIntent.OTHER )
            this.intent = intent;
        else throw new RuntimeException("The current socketIntent is not OTHER socketIntent");
        return this;
    }

    public String getIntent() {
        return this.intent;
    }


    public Object getReceiver() {
        return receiver;
    }


    public Object getContent() {
        return content;
    }

    public Packager setContent(Object content) {
        this.content = content;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public long getTimeserver() {
        return timeserver;
    }

    public Object getResponse() {
        return response;
    }

    public String toJson(){
        if( intent == null ) intent = this.socketIntent.getIntent();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        return gsonBuilder.create().toJson( this );
    }


}
