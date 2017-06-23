package st.zudamoe.support.android.sql;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import st.zudamoe.support.android.sql.object.Function;

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
        return new LinkedList<>();
    }

    @Override
    @Deprecated
    public void inParmm(Object... params) {}


    public void columns(String ... columns) {
        params.addAll(Arrays.asList(columns));
    }

    @Override
    public String name() {
        return super.name+functionSignal();
    }

    private String functionSignal() {
        String function = "(";

        for(int i = 0; i< params.size(); i++){
            function += params.get(i);
            if(i+1 < params.size())
                function+= ", ";
        }

        function += ")";
        return function;
    }


    public static AggregateFunction function (String name, String ... columns) {
        AggregateFunction aggregateFunction  = new AggregateFunction(name);
        aggregateFunction.columns(columns);
        return aggregateFunction;
    }
}
