package st.domain.support.android.sql.builder;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import st.domain.support.android.sql.AbstractSQL;
import st.domain.support.android.sql.type.UpdateLink;

/**
 * Created by daniel on 3/14/17.
 */

public class Update extends AbstractSQL implements UpdateLink.Update, UpdateLink.UpdateSet, UpdateLink.UpdateLinkSet, UpdateLink.WhereOperator, UpdateLink.WhereOperatorResult {
    private List<Object> arguments;
    private String sql;
    private boolean set;

    public  Update(){
        this.arguments = new LinkedList<>();
        this.sql = "";
        this.set = false;
    }

    public Update( CharSequence tableName ){
        this();
        this.update( tableName );
    }

    @Override
    public String sql() {
        return this.sql;
    }



    @Override
    public List<Object> arguments() {
        return this.arguments;
    }


    @Override
    public UpdateLink.UpdateSet update(CharSequence tableName) {
        this.sql = "UPDATE "+tableName;
        return this;
    }

    @Override
    public UpdateLinkSet set( CharSequence columnName, CharSequence argument ) {
        if( set ) this.sql = this.sql + ", ";
        else this.sql = this.sql + " set ";
        this.set = true;
        this.sql = this.sql+ " " + this.processIdentifier( columnName );

        this.sql = this.sql + " = ?";
        this.processArgument( argument );
        return this;
    }

    @Override
    public WhereOperator where(CharSequence column) {
        String process = this.processIdentifier( column );
        this.sql = this.sql + " WHERE " + process;
        return this;
    }

    @Override
    public WhereOperator and(CharSequence... column) {
        String process = this.processIdentifierConjunct( column );
        this.sql = this.sql + " AND " + process;
        return this;
    }

    @Override
    public WhereOperator or(CharSequence... column) {
        String process = this.processIdentifierConjunct( column );
        this.sql = this.sql + " OR " + process;
        return this;
    }

    @Override
    public WhereOperatorResult equal(CharSequence argument) {
        String process = this.processArgument( argument );
        this.sql = this.sql + " = " + process;
        return this;
    }

    @Override
    public WhereOperatorResult notEqual(CharSequence argument) {
        String process = this.processArgument( argument );
        this.sql = this.sql + " != " + process;
        return this;
    }

    @Override
    public WhereOperatorResult like(CharSequence argument) {
        String process = this.processArgument( argument );
        this.sql = this.sql + " LIKE " + process;
        return this;
    }

    @Override
    public WhereOperatorResult in( CharSequence... argument) {
        String process = this.processArgumentsConjunct( argument );
        this.sql = this.sql + " LIKE " + process;
        return this;
    }

    @Override
    public WhereOperatorResult notIn(CharSequence... argument) {
        String process = this.processArgumentsConjunct( argument );
        this.sql = this.sql + " NOT IN " + process;
        return this;
    }

    @Override
    public WhereOperatorResult notLike(CharSequence argument) {
        String process = this.processArgument( argument );
        this.sql = this.sql + " NOT LIKE " + process;
        return this;
    }

    @Override
    public WhereOperatorResult between(CharSequence argumentStart, CharSequence argumentEnd) {
        String processStart  = this.processArgument( argumentStart );
        String processEnd  = this.processArgument( argumentStart );
        this.sql = this.sql + " BETWEEN " + processStart + " AND " + processEnd;
        return this;
    }

    @Override
    public WhereOperatorResult isNull() {
        this.sql = this.sql + " IS NULL";
        return this;
    }

    @Override
    public WhereOperatorResult isNotNull() {
        this.sql = this.sql + " IS NOT NULL";
        return this;
    }
}


