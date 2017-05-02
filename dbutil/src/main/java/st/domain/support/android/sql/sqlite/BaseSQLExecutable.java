package st.domain.support.android.sql.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import st.domain.support.android.sql.type.BaseTypeCharSequence;
import st.domain.support.android.sql.type.TypeCharSequence;

/**
 *
 * Created by dchost on 03/02/17.
 */
public abstract class BaseSQLExecutable implements SQLExecutable {

    private String tag;
    private String sql;
    private SQLiteDatabase database;
    private List<Object> arguments;

    public BaseSQLExecutable() {
        this.sql = "";
        this.arguments = new LinkedList<>();
        this.tag = this.getClass().getSimpleName();
    }



    public BaseSQLExecutable(SQLiteDatabase database) {
        this();
        this.database = database;
    }



    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Get the SQLiteDataBase
     * @return sqliteDatabase
     */
    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    /**
     * Clear all argument list
     */
    public BaseSQLExecutable clearArguments() {
        this.arguments.clear();
        return this;
    }

    /**
     * put the argument for statement
     * @param arguments the variable array of argument
     */
    public void argumets (Object ... arguments) {
        this.arguments.addAll(Arrays.asList(arguments));
    }

    public BaseSQLExecutable(SQLiteDatabase database, CharSequence sql, Object ... arguments) {
        this(database);
        if (sql instanceof String)
            this.execute(String.valueOf(sql), arguments);
        else execute(sql);
    }


    @Override
    public BaseSQLExecutable execute(CharSequence sql) {
        this.prepareFromCharSequence(sql);
        return this.execute();
    }

    protected abstract void prepareFromCharSequence(CharSequence sql);

    /**
     * UpdatableSQL any sql statement string
     * @param sql the string of statement
     * @param arguments the arguments of statements
     */
    public BaseSQLExecutable execute(String sql, Object ... arguments) {
        this.arguments().clear();
        this.sql(sql);
        this.arguments().addAll(Arrays.asList(arguments));
        return this.execute();
    }

    /**
     * UpdatableSQL the current statement
     */
    public BaseSQLExecutable execute() {
        if( this.sql() == null || this.sql().length() == 0 )
            throw new RuntimeException( "No sql for execute" );
        this.exec(this.sql(), ( Object [] ) processArguments());
        this.onPosExec();
        return this;
    }

    protected void onPosExec() {}

    protected abstract void exec(String sql, Object[] arguments);

    protected void bindArguments(Object[] arguments, SQLiteStatement statement) {
        Object argment;
        for(int index = 1; index <= this.arguments().size(); index++ ){
            argment = arguments[index - 1];

            if( argment instanceof BaseTypeCharSequence ){
                argment = ((BaseTypeCharSequence) argment).value();
            }

            if( argment instanceof byte[] )
                statement.bindBlob(index, (byte[]) argment);

            else if( argment instanceof  String )
                statement.bindString( index, String.valueOf( argment ) );

            else if( argment instanceof  Double
                    || argment instanceof Float )
                statement.bindDouble( index, Double.valueOf( String.valueOf( argment ) ) );

            else if( argment instanceof Integer
                    || argment instanceof Long
                    || argment instanceof Byte)
                statement.bindLong( index, Long.valueOf( String.valueOf( argment ) ) );

            else if ( argment instanceof Date )
                statement.bindString( index, argment.toString() );

            else if( argment != null )
                statement.bindString( index, String.valueOf( argment ));
            else statement.bindNull( index );
        }
    }


    public Object [] processArguments() {
        Object [] arguments =  new Object[this.arguments.size()];

        for (int i = 0; i < arguments.length; i++ ) {
            arguments[i] = this.process( this.arguments.get(i) );
        }

        return arguments;
    }

    private Object process(Object argument) {
        if(argument instanceof TypeCharSequence )
            argument = ((TypeCharSequence) argument).value();
        return argument;

    }


    public String sql() {
        return this.sql;
    }

    public List<Object> arguments() {
        return this.arguments;
    }

    public void sql(String sql) {
        this.sql = sql;
    }

    /**
     * Close the query and getDatabase
     */
    public void close() {
        this.database.close();
    }

}
