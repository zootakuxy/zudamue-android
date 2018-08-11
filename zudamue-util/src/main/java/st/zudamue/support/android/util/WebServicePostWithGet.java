package st.zudamue.support.android.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class WebServicePostWithGet extends WebServicePost {

    private List< Parameter > gets;

    public WebServicePostWithGet(String url) {
        super(url);
        this.method( "GET" );
        this.gets = new LinkedList<>();
    }

    public WebServicePostWithGet get(String name, String value  ){
        this.gets.add( new Parameter( name, value ) );
        return this;
    }

    @Override
    protected URL createURL() throws UnsupportedEncodingException, MalformedURLException {
        String query = this.encodeParams( this.gets);
        if( query != null && query.length() > 0 ){
            query = "?" + query;
        } else {
            query = "";
        }
        return new URL( this.getUrl()  + query );
    }

}
