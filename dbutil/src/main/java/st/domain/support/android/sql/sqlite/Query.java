package st.domain.support.android.sql.sqlite;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import st.domain.support.android.sql.Function;
import st.domain.support.android.sql.OnCatchSQLRow;
import st.domain.support.android.sql.SQLRow;
import st.domain.support.android.sql.builder.Select;

/**
 *
 * Created by xdata on 1/1/17.
 */

public class Query {

    private Cursor cursor;
    private SQLiteDatabase database;
    private String query;
    private List<Object> arguments;
    private String tag;
    private Map<String, SQLiteRow.HeaderCell> index;

    private final static Map<Integer, String> mapType = new android.support.v4.util.ArrayMap<>();

    static {
        mapType.put(Cursor.FIELD_TYPE_BLOB, SQLRow.BLOB);
        mapType.put(Cursor.FIELD_TYPE_FLOAT, SQLRow.FLOAT);
        mapType.put(Cursor.FIELD_TYPE_INTEGER, SQLRow.INTEGER);
        mapType.put(Cursor.FIELD_TYPE_STRING, SQLRow.STRING);
        mapType.put(Cursor.FIELD_TYPE_NULL, SQLRow.NULL);
    }

    public Query() {
        this.query = "";
        this.arguments = new LinkedList<>();
        this.tag = Query.class.getSimpleName();
    }

    public Query(SQLiteDatabase database){
        this();
        this.database = database;
    }

    public Query (SQLiteDatabase database, CharSequence query, Object ... arguments) {
        this(database);
        if(query instanceof String)
            execute(String.valueOf(query), arguments);
        else execute(query);
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
     * Execute any sql statement string
     * @param sql the string of statement
     * @param arguments the arguments of statements
     */
    public void execute(String sql, Object ... arguments) {

        this.arguments.clear();
        this.query(sql);
        this.arguments.addAll(Arrays.asList(arguments));
        this.execute();

    }

    /**
     * Execute any query
     * @param query the char sequence query {@link String}, {@link Function}, {@link Select}
     */
    public void execute (CharSequence query) {

        if(query instanceof String) {

            execute(String.valueOf(query), (Object []) null);

        } else if(query instanceof Select){

            this.arguments.clear();
            this.query(((Select) query).sql());
            this.arguments.addAll(((Select) query).arguments());
            execute();

        } else if(query instanceof Function ){

            this.arguments.clear();
            this.query("SELECT "+((Function) query).name());
            this.arguments.addAll(((Function) query).arguments());
            execute();

        }else {

            throw new RuntimeException("Invalid object type query");

        }
    }

    /**
     * Execute the current statement
     */
    public void execute() {
        this.execute(this.query, prepareArguments(this.arguments));
    }


    /**
     * Execute any external statement
     * @param sqlQuery the query statement
     * @param arguments the string value statement
     */
    public void execute(String sqlQuery, String[] arguments) {
        if(this.isOpen())
            this.cursor.close();

        Log.i(tag, "QUERY->>>  "+sqlQuery);
        Log.i(tag, "ARGUMENTS->>>  "+Arrays.toString(arguments));
        this.cursor = this.database.rawQuery(sqlQuery, arguments);
    }

    /**
     * Clear all argument list
     */
    public void clearArguments() {
        this.arguments.clear();
    }

    /**
     * put the sql query stantment
     * @param query sql
     */
    public void query(String query) {
        this.query = query;
    }

    /**
     * put the argument for statement
     * @param arguments the variable array of argument
     */
    public void argumets (Object ... arguments) {
        this.arguments.addAll(Arrays.asList(arguments));
    }

    /**
     * prepare the argument list to argument array
     * @param arguments the list of argument
     * @return the array of arguments
     */
    public static String[] prepareArguments(List<Object> arguments) {
        String argumentsArray[] = (arguments.size() == 0)? null : new String[arguments.size()];
        if(argumentsArray != null){
            for (int i = 0; i<arguments.size(); i++)
                argumentsArray[i] = String.valueOf(arguments.get(i));
        }
        return argumentsArray;
    }

    /**
     * Capture all SQLRow into any list
     * @return the list of SQLRow
     */
    public List<SQLRow> catchAllRow(){
        return catchAllResult(null);
    }


    /**
     * Capture all SQLRow into any list and execute accept per row cached
     * @param onCatchResult the functional caching
     * @return the list of SQLRow
     */
    public List<SQLRow> catchAllResult(OnCatchSQLRow onCatchResult) {

        if(onCatchResult == null) onCatchResult = new OnCatchSQLRow() {
            @Override
            public void accept(SQLRow row) {

            }
        };

        if(hasRow()) {
            this.cursor.moveToFirst();
            List<SQLRow> list = new LinkedList<>();
            do{
                SQLRow row = catchRow(this.cursor);
                list.add(row);
                onCatchResult.accept(row);

            }while (cursor.moveToNext());

            this.closeCurosor();
            return list;
        }else {
            throw new SQLException("The cursor is closed");
        }
    }


    public void forLoopCursor(OnCatchSQLRow onCatchSQLRow) {
        if(hasRow()) {
            this.cursor.moveToNext();
            do {
                SQLRow row = catchRow(this.cursor);
                onCatchSQLRow.accept(row);
            }while (cursor.moveToNext());
            this.closeCurosor();
        }
    }

    public boolean hasRow() {
        return isOpen() && this.cursor.getCount()>0;
    }

    /**
     * Capture the statusValues for cursor row into map
     * @param cursor the cursor catchable
     * @return the SQLRow for cursor position
     */
    public SQLRow catchRow(Cursor cursor)
    {
        Map<String, SQLiteRow.HeaderCell> indexH = this.processIndex(cursor);
        Map<String, SQLiteRow.HeaderCell> headerIndex = Collections.unmodifiableMap(indexH);

        SQLiteRow row = new SQLiteRow(cursor.getColumnCount(), headerIndex);
        String columnName;

        for(int i=0; i<cursor.getColumnCount(); i++)
        {

            columnName = cursor.getColumnName(i);
            switch (cursor.getType(i))
            {
                case Cursor.FIELD_TYPE_BLOB:
                    row.blob(columnName, cursor.getBlob(i));
                    break;

                case Cursor.FIELD_TYPE_FLOAT:
                    row.real(columnName, cursor.getFloat(i));
                    break;

                case Cursor.FIELD_TYPE_INTEGER:
                    row.integer(columnName, cursor.getInt(i));
                    break;

                case Cursor.FIELD_TYPE_STRING:
                    row.string(columnName, cursor.getString(i));
                    break;

                case Cursor.FIELD_TYPE_NULL:
                    break;

                default:
                    row.value(columnName, cursor.getExtras().get(columnName));
            }
        }
        return row;
    }

    private Map<String, SQLiteRow.HeaderCell> processIndex(Cursor cursor) {

        Map<String, SQLiteRow.HeaderCell> index = this.index;
        if(cursor == this.cursor && this.index == null) {
            this.index = new LinkedHashMap<>();
            processIndex(this.cursor, this.index);

            this.index = Collections.unmodifiableMap(this.index);
            index = this.index;
        }
        else if(cursor != this.cursor){
            index = new LinkedHashMap<>();
            processIndex(cursor, index);

            index = Collections.unmodifiableMap(index);
        }
        return index;
    }

    private void processIndex(Cursor cursor, Map<String, SQLiteRow.HeaderCell> index) {
        for(int i = 0; i<cursor.getColumnCount(); i++){
            String name = cursor.getColumnName(i);
            String type = this.type(cursor.getType(i));
            SQLiteRow.HeaderCell headerCell = new SQLiteRow.HeaderCell(name, type, i);
            index.put(name, headerCell);
        }
    }

    private String type(int type) {
        return mapType.get(type);
    }


    /**
     * Close the query and database
     */
    public void close() {
        closeCurosor();
        this.database.close();
    }

    /**
     * close de query
     */
    public void closeCurosor(){
        if(isOpen()) {
            this.cursor.close();
        }

        this.index = null;
        this.cursor = null;

    }

    /**
     * Verify if has next
     * @return true if has next row
     */
    public boolean hasNext() {
        return isOpen()
                && cursor.getPosition()+1 < cursor.getCount();
    }

    /**
     * Get the next row into cursor
     * @return the SQLRow of position in cursor
     */
    public SQLRow nextRow() {
        SQLRow row = catchRow(this.cursor);
        this.cursor.moveToNext();
        return row;
    }

    /**
     * Verify if cursor is open
     * @return true if cursor is open
     */
    public boolean isOpen() {
        return cursor != null
                && !cursor.isClosed();
    }
}
