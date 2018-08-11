package st.zudamue.support.android.util;

import android.os.AsyncTask;
import android.util.Log;



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
    private URL createdUrl;
    private String encode = "UTF-8";
    private String method;
    private List< OnReadLine > onReadLines;
    private List<OnSuccess> onSuccesses;
    private List<OnFail> onFail;

    private int responseCode;
    private String responseMessage;

    WebService(String url) {
        this.url = url;
        this.onReadLines = new LinkedList<>();
        this.onSuccesses = new LinkedList<>();
        this.onFail = new LinkedList<>();
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

    public URL getCreatedUrl() {
        return createdUrl;
    }


    public WebService onSuccess(OnSuccess onSuccess){
        this.onSuccesses.add(onSuccess);
        return this;
    }

     public WebService onFail( OnFail onFail ){
        this.onFail.add(onFail);
        return this;
    }

    /**
     * @param voids
     * @return
     */
    @Override
    protected Void doInBackground(Void... voids) {
        HttpURLConnection request = null;
        String line;
        BufferedReader reader = null;
        StringBuilder builder;
        InputStream in = null;

        try {
            Log.i( this.getClass().getName(), "doInBackground start" );
            this.createdUrl = this.createURL();

            builder = new StringBuilder();
            request = (HttpURLConnection) createdUrl.openConnection();
            request.setRequestMethod( this.getMethod() );
            this.processParameter( request );

            request.connect();
            in = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            while ( ( line = reader.readLine() ) != null ){
                builder.append( line).append( "\n" );
                for( OnReadLine onReadLine: this.onReadLines ){
                    onReadLine.onReadLine( line );
                }
            }

            this.responseMessage = request.getResponseMessage();
            this.responseCode = request.getResponseCode();

            in.close();
            request.disconnect();

            String text = builder.toString();
            for( OnSuccess onReadText: this.onSuccesses){
                onReadText.onSuccess( text, this.responseCode, this.responseMessage );
            }

        } catch ( Exception e ) {
            e.printStackTrace();

            for( OnFail onFail: this.onFail){
                onFail.onFail( e );
            }

            if( reader != null ){
                try {
                    reader.close();
                } catch (IOException ex) {
                    for( OnFail onFail: this.onFail){
                        onFail.onFail( ex );
                    }
                }
            }

            if ( in != null ){
                try {
                    in.close();
                } catch (IOException e1) {
                    for( OnFail onFail: this.onFail){
                        onFail.onFail( e1 );
                    }
                }
            }

            if( request != null  ){
                request.disconnect();
            }
        }
        Log.i( "siie-scanner", "doInBackground end" );
        return null;
    }

    /**
     *
     * @param request
     */
    protected void processParameter(HttpURLConnection request) throws IOException { }


    /**
     *
     * @return
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     */
    protected URL createURL() throws UnsupportedEncodingException, MalformedURLException {
        return new URL( this.url );
    }


    /**
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    String encodeParams( List< Parameter> params ) throws UnsupportedEncodingException {
        StringBuilder combinedParams = new StringBuilder();
        String moreAttribute = "";
        String paramString;

        if(!params.isEmpty()){
            for( Parameter p : params) {
                paramString = p.getName() + "=" + URLEncoder.encode( p.getValue(),this.getEncode() );
                combinedParams.append( moreAttribute ).append( paramString );
                moreAttribute = "&";
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


    public interface OnSuccess {
        void onSuccess(String text, int resultCode, String message );
    }

    public interface OnFail {
        void onFail( Exception ex );
    }

    public interface OnFinish {
        void onFinish(  );
    }




}
