package st.domain.support.android.sql;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xdata on 12/31/16.
 */

public class Function extends Identifier {

    protected List<Object> params;
    protected List<Object> argumentsValues;

    public Function(String name) {
        super(name);
        this.params = new LinkedList<>();
    }

    public void inParmm(Object ... params) {
        if(params != null)
            this.params.addAll(Arrays.asList(params));
    }

    @Override
    public String name() {
        return super.name+ processParams();
    }

    private String processParams() {
        if(this.argumentsValues != null)
            this.argumentsValues.clear();
        else this.argumentsValues = new LinkedList<>();

        String function = "(";

        for(int i = 0; i< params.size(); i++){
            String in = "?";
            Object value = this.params.get(i);
            if(value != null && value instanceof Identifier) {
                in = ((Identifier) value).name();
                if(((Identifier) value).arguments() != null)
                    this.argumentsValues.addAll(((Identifier) value).arguments());
            }
            else{
                this.argumentsValues.add(value);
            }

            function += in;
            if(i+1 < params.size())
                function+= ", ";
        }

        function += ")";
        return function;
    }

    @Override
    public List<Object> arguments() {
        if(this.argumentsValues == null)
            this.processParams();
        return this.argumentsValues;
    }

    public static Function function (String name, Object ... params){
        Function function = new Function(name);
        function.inParmm(params);
        return function;
    }
}
