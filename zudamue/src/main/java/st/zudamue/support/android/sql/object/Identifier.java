package st.zudamue.support.android.sql.object;

import java.util.List;

import st.zudamue.support.android.util.BaseCharSequence;

/**
 * Created by xdanielon 12/30/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
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
