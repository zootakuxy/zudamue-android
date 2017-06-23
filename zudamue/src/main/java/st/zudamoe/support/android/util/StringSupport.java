package st.zudamoe.support.android.util;

/**
 * Created by xdaniel on 6/8/17.
 */

public class StringSupport {

    private String text;

    public StringSupport(String text) {
        this.text = text;
    }

    public StringSupport text(String text ){
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

    public boolean equalsAny(String... texts) {
        if( this.text == null )return  false;
        for( String s: texts ) {
            if( s != null && text.equals( s ) ) return true;
        }
        return false;
    }

    public boolean anyEqualIgnoreCase(String... texts) {
        if( this.text == null )return  false;
        for( String s: texts ) {
            if( s != null && text.equalsIgnoreCase( s ) ) return true;
        }
        return false;
    }


    public boolean allEqual ( String ... texts ){
        if( this.text == null )return  false;
        for( String s: texts ) {
            if( s != null || text.equals( s ) ) return false;
        }
        return true;
    }

    public boolean allEqualIgnoreCase ( String ... texts ){
        if( this.text == null )return  false;
        for( String s: texts ) {
            if( s != null || text.equalsIgnoreCase( s ) ) return false;
        }
        return true;
    }
}
