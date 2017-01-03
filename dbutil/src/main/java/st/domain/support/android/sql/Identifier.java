package st.domain.support.android.sql;

import java.util.List;

/**
 *
 * Created by xdata on 12/30/16.
 */

public abstract class Identifier implements CharSequence {

    private final String name;


    public Identifier(String name) {

        this.name = name;

    }

    public abstract List<Object> arguments();

    @Override
    public int length() {
        return this.name.length();
    }

    @Override
    public char charAt(int index) {
        return this.name.charAt(0);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.name.subSequence(start, end);
    }

    public String name(){
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
