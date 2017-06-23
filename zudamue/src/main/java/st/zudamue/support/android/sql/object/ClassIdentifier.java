package st.zudamue.support.android.sql.object;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xdaniel on 6/2/17.
 */

public class ClassIdentifier extends Identifier {

    private List<Object> arguments;

    public ClassIdentifier() {
        super(null);
        this.arguments = new LinkedList<>();
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return this.name();
    }

    @Override
    public List<Object> arguments() {
        return Collections.unmodifiableList(this.arguments);
    }
}
