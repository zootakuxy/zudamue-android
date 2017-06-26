package st.zudamue.support.android.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xdaniel on 6/7/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class Encryption {

    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_SHA1 = "SHA-1";
    private static final String ALGORITHM_SHA256 = "SHA-256";

    private  String stringOfHash(byte[] hash ) {
        StringBuilder s = new StringBuilder();
        for ( int i = 0; i < hash.length; i++ ) {
            int topPoint = ( ( hash[ i ] >> 4 ) & 0xf ) << 4;
            int bottomPoint = hash[ i ] & 0xf;
            if ( topPoint == 0 ) s.append( '0' );
            s.append( Integer.toHexString( topPoint | bottomPoint ) );
        }
        return s.toString();
    }


    private  byte[] generateHash( String algorithm, String text) {
        try {

            MessageDigest md = MessageDigest.getInstance( algorithm );
            md.update( text.getBytes( ) );

            return md.digest( );
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public String crypt( String algorithm, String text){
        return ( stringOfHash( generateHash( algorithm, text) ) );
    }

    public String cryptMD5( String text ){
        return ( crypt( ALGORITHM_MD5, text));
    }

    public String cryptSHA1( String text ){
        return ( crypt( ALGORITHM_SHA1, text ) );
    }

    public String cryptSHA256( String text ){
        return ( crypt( ALGORITHM_SHA256, text ) );
    }
}
