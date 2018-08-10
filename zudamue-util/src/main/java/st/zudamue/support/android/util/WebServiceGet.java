package st.zudamue.support.android.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class WebServiceGet extends WebService {

    private List< Parameter > gets;

    public WebServiceGet(String url) {
        super(url);
        this.method( "GET" );
        this.gets = new LinkedList<>();
    }

    public void get( String name, String value ){
        this.gets.add( new Parameter( name, value ) );
    }

    @Override
    protected URL createURL() {
        try {
            return new URL( this.getUrl()  + this.encodeParams( this.gets) );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
