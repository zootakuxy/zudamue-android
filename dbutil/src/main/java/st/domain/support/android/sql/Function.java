package st.domain.support.android.sql;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xdata on 12/31/16.
 */

public class Function extends Identifier {

    protected List<Object> argumentList;

    public Function(String name) {
        super(name);
        this.argumentList = new LinkedList<>();
    }

    public void params(Object ... params) {
        if(params != null)
            this.argumentList.addAll(Arrays.asList(params));
    }

    @Override
    public String name() {
        return super.name()+functionSignal();
    }

    private String functionSignal() {
        String function = "(";

        for(int i = 0; i< argumentList.size(); i++){
            function += "?";
            if(i+1 < argumentList.size())
                function+= ", ";
        }

        function += ")";
        return function;
    }

    @Override
    public List<Object> arguments() {
        return this.argumentList;
    }

    public static Function function (String name, Object ... params){
        Function function = new Function(name);
        function.params(params);
        return function;
    }
}
