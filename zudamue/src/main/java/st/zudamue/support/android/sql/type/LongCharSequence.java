package st.zudamue.support.android.sql.type;

/**
 * Created by xdaniel on 03/02/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class LongCharSequence extends BaseTypeCharSequence<Long>  {


    public LongCharSequence(Long value) {
        super(value);
    }

    public LongCharSequence(CharSequence charSequence) {
        super(0L);

        if(charSequence == null) return;
        if(charSequence instanceof LongCharSequence)
            super.value(((LongCharSequence) charSequence).value());
        else {
            try {
                value(Long.valueOf(String.valueOf(charSequence)));
            }catch (Exception ignore) {
            }
        }
    }
}
