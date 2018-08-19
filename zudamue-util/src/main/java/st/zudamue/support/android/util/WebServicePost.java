package st.zudamue.support.android.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;

public class WebServicePost extends WebService {

    private List< Parameter > posts;

    public WebServicePost(String url) {
        super(url);
        this.method( Method.POST );
        this.posts = new LinkedList<>();
    }

    public WebServicePost post( String name, Object value ){
        this.posts.add( new Parameter( name, String.valueOf( value ) ) );
        return this;
    }

    @Override
    protected void processParameter(HttpURLConnection request) throws IOException {
        OutputStream out;
        BufferedWriter writer;

        request.setDoInput( true );
        request.setDoOutput( true );
        out = request.getOutputStream();
        writer = new BufferedWriter( new OutputStreamWriter( out, this.getEncode() ) );
        writer.write( super.encodeParams( this.posts ) );
        writer.flush();
        writer.close();
        out.close();
    }
}
