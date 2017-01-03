package st.domain.support.android.sql;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by xdata on 12/31/16.
 */

public class AggregateFunction extends Function {

    public AggregateFunction(String name) {
        super(name);
    }

    @Override
    public List<Object> arguments() {
        return null;
    }

    @Override
    @Deprecated
    public void params(Object... params) {}


    public void columns(String ... columns) {
        argumentList.addAll(Arrays.asList(columns));
    }



    @Override
    public String name() {
        return super.name();
    }

    public static AggregateFunction function (String name, String ... columns) {
        AggregateFunction aggregateFunction  = new AggregateFunction(name);
        aggregateFunction.columns(columns);
        return aggregateFunction;
    }
}
