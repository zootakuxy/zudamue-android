package st.zudamue.support.android.sql.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import st.zudamue.support.android.sql.SQL;

/**
 *
 * Created by dchost on 03/02/17.
 */

public class UpdatableSQL extends BaseSQLExecutable {

    private Map<String, Execute> mapExecutor;
    private ExecuteType executeType;
    private long result;
    private ExecuteType resultType;

    public UpdatableSQL() {
        this.init();
    }

    public UpdatableSQL(SQLiteDatabase database) {
        super(database);
        this.init();
    }

    public UpdatableSQL(SQLiteDatabase database, CharSequence sql, Object... arguments) {
        super(database, sql, arguments);
        this.init();
    }

    private void init() {
        this.mapExecutor = new LinkedHashMap<>();
        this.mapExecutor.put("INSERT", new Execute() {
            @Override
            public void execute(SQLiteStatement statement) {
                result = statement.executeInsert();
                executeType = ExecuteType.INSERT;
            }
        });

        this.mapExecutor.put("UPDATE", new Execute() {
            @Override
            public void execute(SQLiteStatement statement) {
                result = statement.executeUpdateDelete();
                executeType = ExecuteType.UPDATE;
            }
        });

        this.mapExecutor.put("DELETE", new Execute() {
            @Override
            public void execute(SQLiteStatement statement) {
                result = statement.executeUpdateDelete();
                executeType = ExecuteType.DELETE;
            }
        });
    }

    @Override
    protected void prepareFromCharSequence(CharSequence sql) {
        this.clearArguments();
        if( sql instanceof  String ) {
            this.sql( String.valueOf(sql) );
        }

        else if( sql instanceof SQL) {
            this.sql((( SQL ) sql).sql());
            this.arguments().addAll((( SQL ) sql ).arguments());
        }

    }

    @Override
    protected void exec(String sql, Object[] arguments) {
        Log.i(getTag(), "-> UpdatableSQL.exec");

        Log.i(getTag(), "sql: "+sql);
        Log.i(getTag(), "args: " + Arrays.asList( arguments ) );

        SQLiteStatement statement = getDatabase().compileStatement(sql);
        bindArguments( arguments, statement );
        String checkSqlType = sql.trim();
        checkSqlType = checkSqlType.split(" ")[0];
        checkSqlType = checkSqlType.toUpperCase();

        Execute execute = this.mapExecutor.get(checkSqlType);
        if(execute == null) {
            throw  new RuntimeException( "Invalid sql statement ");
        }

        execute.execute( statement );
    }


    @Override
    protected void onPosExec() {
        super.onPosExec();

    }

    public void onExecuteResult( OnResultExecute onResultExecute ) {
        onResultExecute.onResult( this.executeType, this.result );
    }

    public ExecuteType getResultType() {
        return resultType;
    }

    public Long getResultExecute() {
        return this.result;
    }


    private interface Execute {
        void execute ( SQLiteStatement statement );
    }

    public interface OnResultExecute  {
        void onResult(ExecuteType executeType, long result );
    }

    public enum ExecuteType {
        INSERT,
        DELETE,
        UPDATE
    }
}
