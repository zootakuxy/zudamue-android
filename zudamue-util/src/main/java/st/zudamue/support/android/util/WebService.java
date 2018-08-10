package st.zudamue.support.android.util;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.IOException;
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
public class WebService extends AsyncTask< Void, Void, Void>{

    private List< Parameter > params;

    private String url;

    private int responseCode;
    private String message;

    private String response;
    private String encode = "UTF-8";

    public String getResponse() {
        return response;
    }


    public WebService(String url) {
        this.url = url;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        URL url = this.createURL();
        try {
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     */
    private URL createURL() {
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
    @NonNull
    private String encodeParams() throws UnsupportedEncodingException {
        StringBuilder combinedParams = new StringBuilder();
        String addOperator = "";
        if(!params.isEmpty()){
            combinedParams.append("?");

            for( Parameter p : params) {
                String paramString = p.getName() + "=" + URLEncoder.encode( p.getValue(),this.encode );
                combinedParams.append( addOperator ).append( paramString );
                addOperator = "&";
            }
        }
        return combinedParams.toString();
    }





    /**
     *
     */
    private class Parameter {
        private String name;
        private String value;

        public Parameter(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

}
