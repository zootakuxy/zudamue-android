package st.domain.support.android.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xdaniel on 6/7/17.
 */

public class Cryption {

    private  String stringHexa(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
            int parteBaixa = bytes[i] & 0xf;
            if (parteAlta == 0) s.append('0');
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }


    private  byte[] gerarHash(String frase, String algoritmo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(frase.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public String crypt( String algoritm, String frase){
        return ( stringHexa(gerarHash(frase, algoritm) ) );
    }

    public String cryptMD5( String frase ){
        return ( crypt( "MD5", frase));
    }

    public String cryptSHA1( String frase ){
        return ( crypt( "SHA-1", frase));
    }

    public String cryptSHA256( String frase ){
        return ( crypt( "SHA-256", frase));
    }
}
