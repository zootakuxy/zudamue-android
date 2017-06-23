package st.zudamoe.support.android.sql.type;

/**
 *
 * Created by dchost on 03/02/17.
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
