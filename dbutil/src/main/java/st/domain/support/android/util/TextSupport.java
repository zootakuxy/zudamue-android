package st.domain.support.android.util;

/**
 * Created by xdaniel on 6/8/17.
 */

public class TextSupport {

    private String text;

    public TextSupport(String text) {
        this.text = text;
    }

    public TextSupport text( String text ){
        this.text = text;
        return this;
    }

    public boolean isNumberOnly(){
        if( this.text == null || this.text.length() == 0 ) return false;
        for ( char ch : this.text.toCharArray() ){
            if ( ch < '0' || ch > '9' ) return  false;
        }
        return true;
    }

    public String subLength( int indexStart, int length ) {
        return this.text.substring( indexStart, indexStart + length );
    }
}
