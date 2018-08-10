package st.zudamue.support.android.util;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public abstract class WebService extends AsyncTask< Void, Void, Void>{


    private String url;
    private String encode = "UTF-8";
    private String method;
    private List< OnReadLine > onReadLines;
    private List< OnException > onExceptions;
    private List< OnFinish > onFinishes;

    private int responseCode;
    private String responseMessage;

    WebService(String url) {
        this.url = url;
        this.onReadLines = new LinkedList<>();
        this.onExceptions = new LinkedList<>();
        this.onFinishes = new LinkedList<>();
    }

    public WebService encode( String encode ) {
        this.encode = encode;
        return this;
    }

    public WebService method( String method ) {
        this.method = method;
        return this;
    }

    public String getMethod() {
        return this.method;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getEncode() {
        return encode;
    }

    public WebService onReadLine( OnReadLine onReadLine ){
        this.onReadLines.add( onReadLine );
        return this;
    }

    public WebService onException( OnException onException  ){
        this.onExceptions.add( onException );
        return this;
    }

    public WebService onFinish( OnFinish onFinish ){
        this.onFinishes.add( onFinish );
        return this;
    }

    /**
     * @param voids
     * @return
     */
    @Override
    protected Void doInBackground(Void... voids) {
        URL url = this.createURL();
        HttpURLConnection request = null;
        String line;
        BufferedReader reader = null;

        StringBuilder builder = new StringBuilder();
        try {
            assert url != null;
            request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod( this.getMethod() );
            request.connect();
            this.processParameter( request );

            InputStream in = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            while ( ( line = reader.readLine() ) != null ){
                builder.append( line).append( "\n");
                for( OnReadLine onReadLine: this.onReadLines ){
                    onReadLine.onReadLine( line );
                }
            }

            this.responseMessage = request.getResponseMessage();
            this.responseCode = request.getResponseCode();

            in.close();
            request.disconnect();

            String text = builder.toString();
            for( OnFinish onReadText: this.onFinishes ){
                onReadText.onFinish( text, this.responseCode, this.responseMessage );
            }

        } catch ( IOException e ) {
            if( request != null  ){
                request.disconnect();
            }

            if( reader != null ){
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            for( OnException onException: this.onExceptions ){
                onException.onException( e );
            }
        }
        return null;
    }

    /**
     *
     * @param request
     */
    protected void processParameter(HttpURLConnection request) { }

    /**
     * @return
     */
    protected URL createURL() {
        try {
            return new URL( this.url );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    String encodeParams( List< Parameter> params ) {
        StringBuilder combinedParams = new StringBuilder();
        String addOperator = "";
        if(!params.isEmpty()){
            combinedParams.append("?");
            for( Parameter p : params) {
                String paramString = null;
                try {
                    paramString = p.getName() + "=" + URLEncoder.encode( p.getValue(),this.encode );
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
                combinedParams.append( addOperator ).append( paramString );
                addOperator = "&";
            }
        }
        return combinedParams.toString();
    }


    public String getUrl() {
        return url;
    }

    /**
     *
     */
    class Parameter {
        private String name;
        private String value;

        protected Parameter(String name, String value) {
            this.name = name;
            this.value = value;
        }

        private String getName() {
            return name;
        }

        private String getValue() {
            return value;
        }
    }

    public interface OnReadLine {
        void onReadLine( String line );
    }


    public interface OnFinish {
        void onFinish( String text, int resultCode, String message );
    }


    public interface OnException {
        void onException(Exception text );
    }




}
