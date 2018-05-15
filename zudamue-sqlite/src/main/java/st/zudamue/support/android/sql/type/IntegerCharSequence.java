package st.zudamue.support.android.sql.type;

/**
 * Created by xdaniel on 03/02/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class IntegerCharSequence extends BaseTypeCharSequence<Integer>  {


    public IntegerCharSequence(Integer value) {
        super(value);
    }

    public IntegerCharSequence(CharSequence charSequence) {
        super(0);

        if(charSequence == null) return;
        if(charSequence instanceof IntegerCharSequence)
            super.value(((IntegerCharSequence) charSequence).value());
        else {
            try {
                value(Integer.valueOf(String.valueOf(charSequence)));
            }catch (Exception ignore) {
            }
        }
    }
}
