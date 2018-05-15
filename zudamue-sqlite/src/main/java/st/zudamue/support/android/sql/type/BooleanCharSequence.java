package st.zudamue.support.android.sql.type;

/**
 * Created by xdaniel on 03/02/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class BooleanCharSequence extends BaseTypeCharSequence<Boolean>  {


    public BooleanCharSequence(Boolean value) {
        super(value);
    }

    public BooleanCharSequence(CharSequence charSequence) {
        super(false);

        if(charSequence == null) return;
        if(charSequence instanceof BooleanCharSequence)
            super.value(((BooleanCharSequence) charSequence).value());
        else {
            try {
                value(Boolean.valueOf(String.valueOf(charSequence)));
            }catch (Exception ignore) {
            }
        }
    }
}
