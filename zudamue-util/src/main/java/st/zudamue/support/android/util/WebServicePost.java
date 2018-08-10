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
        this.method( "POST" );
        this.posts = new LinkedList<>();
    }

    public WebServicePost post( String name, String value ){
        this.posts.add( new Parameter( name, value ) );
        return this;
    }

    @Override
    protected void processParameter(HttpURLConnection request) {
        OutputStream out = null;
        BufferedWriter writer = null;

        try {
            out = request.getOutputStream();
            writer = new BufferedWriter( new OutputStreamWriter( out, this.getEncode() ) );
            writer.write( super.encodeParams( this.posts) );
            writer.flush();
            writer.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            if( writer != null ){
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if( out != null ){
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }
}
