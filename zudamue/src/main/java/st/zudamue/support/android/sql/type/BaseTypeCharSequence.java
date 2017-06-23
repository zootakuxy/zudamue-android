package st.zudamue.support.android.sql.type;

import st.zudamue.support.android.util.BaseCharSequence;

/**
 *
 * Created by dchost on 03/02/17.
 */

public class BaseTypeCharSequence<E> extends BaseCharSequence implements TypeCharSequence<E> {

    private E value;

    public BaseTypeCharSequence(E value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public E value() {
        return this.value;
    }

    protected void value(E value) {
        this.value = value;
    }
}
