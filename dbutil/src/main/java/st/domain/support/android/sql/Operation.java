package st.domain.support.android.sql;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * Created by daniel on 3/14/17.
 */

public class Operation extends AbstractSQL {

    private String sql;
    private List<Object> list;

    public Operation( ){
        this.sql = "";
        this.list = new LinkedList<>();
    }

    public CharSequence argumentsOperation( CharSequence ... sequencesOperation ){
        for( CharSequence sequence: sequencesOperation ){
            this.sql += this.processArgument( sequence );
        }
        return this;
    }

    public CharSequence identifierOperation( CharSequence ... sequencesOperation ){
        for( CharSequence sequence: sequencesOperation ){
            this.sql += this.processIdentifier( sequence );
        }
        return this;
    }

    @Override
    public String sql() {
        return this.sql;
    }

    @Override
    public List<Object> arguments() {
        return this.list;
    }
}
