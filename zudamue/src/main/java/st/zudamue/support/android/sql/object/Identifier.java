package st.zudamoe.support.android.sql.object;

import java.util.List;

import st.zudamoe.support.android.util.BaseCharSequence;

/**
 *
 * Created by xdata on 12/30/16.
 */

public abstract class Identifier extends BaseCharSequence {

    protected final String name;


    public Identifier(String name) {
        this.name = name;
    }

    public abstract List<Object> arguments();

    public String name(){
        return name;
    }

    @Override
    public String toString() {
        return  this.name();
    }
}
