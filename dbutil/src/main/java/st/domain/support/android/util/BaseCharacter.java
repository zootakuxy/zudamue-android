package st.domain.support.android.util;

import java.io.Serializable;

/**
 * Created by xdata on 8/9/16.
 */
public abstract class BaseCharacter implements CharSequence, Serializable
{

    @Override
    public int length() {
        return toString().length();
    }

    @Override
    public char charAt(int index) {
        return this.toString().charAt(index);
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {

        return this.toString().subSequence(beginIndex, endIndex);
    }

    public abstract String toString();
}
