package st.zudamue.support.android.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class WebServicePostGet extends WebServicePost {

    private List< Parameter > gets;

    public WebServicePostGet(String url) {
        super(url);
        this.method( "GET" );
        this.gets = new LinkedList<>();
    }

    public WebServicePostGet get( String name, String value  ){
        this.gets.add( new Parameter( name, value ) );
        return this;
    }

    @Override
    protected URL createURL() {
        try {
            return new URL( this.getUrl()  + this.encodeParams( this.gets ) );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
