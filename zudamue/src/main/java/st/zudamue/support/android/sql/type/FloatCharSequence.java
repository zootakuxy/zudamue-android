package st.zudamue.support.android.sql.type;

/**
 * Created by xdaniel on 03/02/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class FloatCharSequence extends BaseTypeCharSequence<Float>  {


    public FloatCharSequence(Float value) {
        super(value);
    }

    public FloatCharSequence(CharSequence charSequence) {
        super(0f);

        if(charSequence == null) return;
        if(charSequence instanceof FloatCharSequence)
            super.value(((FloatCharSequence) charSequence).value());
        else {
            try {
                value(Float.valueOf(String.valueOf(charSequence)));
            }catch (Exception ignore) {
            }
        }
    }
}
