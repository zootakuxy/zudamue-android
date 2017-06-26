package st.zudamue.support.android.sql.type;

/**
 * Created by xdaniel on 03/02/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class ByteCharSequence extends BaseTypeCharSequence<Byte>  {


    public ByteCharSequence(Byte value) {
        super(value);
    }

    public ByteCharSequence(CharSequence charSequence) {
        super((byte) 0);

        if(charSequence == null) return;
        if(charSequence instanceof ByteCharSequence)
            super.value(((ByteCharSequence) charSequence).value());
        else {
            try {
                value(Byte.valueOf(String.valueOf(charSequence)));
            }catch (Exception ignore) {
            }
        }
    }
}
