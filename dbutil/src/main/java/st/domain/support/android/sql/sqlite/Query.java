package st.domain.support.android.sql.sqlite;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import st.domain.support.android.sql.Function;
import st.domain.support.android.sql.OnQueryResult;
import st.domain.support.android.sql.SQLRow;
import st.domain.support.android.sql.builder.Select;

/**
 *
 * Created by xdata on 1/1/17.
 */
public class Query extends BaseSQLExecutable {

    private Cursor cursor;
    private Map<String, SQLiteRow.HeaderCell> index;

    private final static Map<Integer, String> mapType = new LinkedHashMap<>();
    private final static Map<Integer, Class<?>> mapClass = new LinkedHashMap<>();
    static {
        // Maping the types
        mapType.put(Cursor.FIELD_TYPE_BLOB, SQLRow.BLOB);
        mapType.put(Cursor.FIELD_TYPE_FLOAT, SQLRow.FLOAT);
        mapType.put(Cursor.FIELD_TYPE_INTEGER, SQLRow.INTEGER);
        mapType.put(Cursor.FIELD_TYPE_STRING, SQLRow.STRING);
        mapType.put(Cursor.FIELD_TYPE_NULL, SQLRow.NULL);

        // Maping the class
        mapClass.put(Cursor.FIELD_TYPE_BLOB, byte[].class);
        mapClass.put(Cursor.FIELD_TYPE_FLOAT, float.class);
        mapClass.put(Cursor.FIELD_TYPE_INTEGER, int.class);
        mapClass.put(Cursor.FIELD_TYPE_STRING, String.class);
        mapClass.put(Cursor.FIELD_TYPE_NULL, null);
    }

    public Query() {
        super();
    }

    public Query(SQLiteDatabase database){
        super(database);
    }

    public Query (SQLiteDatabase database, CharSequence query, Object ... arguments) {
        super(database, query, arguments);
    }


    @Override
    protected void prepareFromCharSequence(CharSequence query) {

        this.clearArguments();
        if( query instanceof  String ){
            this.sql(String.valueOf(query));
        }
        else if( query instanceof  Select ){
            this.sql(((Select) query).sql());
            this.arguments().addAll(((Select) query).arguments());
        }
        else if( query instanceof Function ){
            this.sql("SELECT "+((Function) query).name());
            this.arguments().addAll(((Function) query).arguments());
        }
        else {
            throw  new RuntimeException("unknown charSequence");
        }

    }

    /**
     * UpdatableSQL any external statement
     * @param sql the query statement
     * @param arguments the string value statement
     */
    protected void exec(String sql, Object [] arguments) {
        Log.i(getTag(), "QUERY->>>  "+sql);
        Log.i(getTag(), "ARGUMENTS->>>  "+Arrays.toString(arguments));
        String[] stringArgument = asArrayString(arguments);
        this.index = null;
        this.cursor = this.getDatabase().rawQuery(sql, stringArgument);
    }

    private String[] asArrayString(Object[] arguments) {

        if( arguments == null || arguments.length == 0 ) return  null;
        String[] array = new String[arguments.length];

        Object arg;
        for ( int i = 0; i < arguments.length; i++ ) {
            arg = arguments[i];
            array[i] = ( arg == null )? null : String.valueOf(arg);
        }
        return array;
    }

    /**
     * Capture all SQLRow into any list
     * @return the list of SQLRow
     */
    public @NonNull List<SQLRow> catchAllResult(){
        return catchAllResult(null);
    }


    /**
     * Capture all SQLRow into any list and execute accept per row cached
     * @param onCatchResult the functional caching
     * @return the list of SQLRow
     */
    public @NonNull List<SQLRow> catchAllResult(OnQueryResult onCatchResult) {

        if(onCatchResult == null) onCatchResult = new OnQueryResult() {
            @Override
            public boolean accept(SQLRow row) {
                return true;
            }
        };

        List<SQLRow> list = new LinkedList<>();
        Cursor loopCursor = this.cursor;

        if(hasRow()) {
            loopCursor.moveToFirst();
            Map< String, SQLiteRow.HeaderCell > index = getIndexer();

            do{
                SQLRow row = catchResultRow( loopCursor, index );
                list.add( row );
                if( !onCatchResult.accept( row ) )
                    break;
            } while ( loopCursor.moveToNext() );

            this.closeCursor( loopCursor );
        }else if( loopCursor.isClosed() ) {
            throw new SQLException("The cursor is closed");
        }

        return list;
    }


    /**
     * Loop in SQLResult
     * @param onCatchSQLRow lambda of loop code
     */
    public void forLoopCursor(OnQueryResult onCatchSQLRow) {
        if(hasRow()) {
            Cursor loopCursor = this.cursor;
            loopCursor.moveToNext();
            Map<String,SQLiteRow.HeaderCell> index = this.getIndexer();

            do {
                SQLRow row = catchResultRow(loopCursor, index);
                if( ! onCatchSQLRow.accept(row) )
                    break;
            }while (loopCursor.moveToNext());
            this.closeCursor(loopCursor);
        }
    }

    /**
     * Verity if as row in current result
     * @return true if as result | false if not has result
     */
    public boolean hasRow() {
        return isOpen() && this.cursor.getCount()>0;
    }

    /**
     * Capture the statusValues for cursor row into map
     * @param cursor the cursor catchable
     * @return the SQLRow for cursor position
     */
    public SQLRow catchResultRow( Cursor cursor, Map<String, SQLiteRow.HeaderCell> indexRow ) {

        SQLiteRow row = new SQLiteRow( cursor.getColumnCount(), indexRow );
        String columnName;

        for(int i = 0; i<cursor.getColumnCount(); i++) {

            columnName = cursor.getColumnName( i );
            switch ( cursor.getType( i ) ) {
                case Cursor.FIELD_TYPE_BLOB:
                    row.blob(columnName, cursor.getBlob(i));
                    break;

                case Cursor.FIELD_TYPE_FLOAT:
                    row.real(columnName, cursor.getFloat(i));
                    break;

                case Cursor.FIELD_TYPE_INTEGER:
                    row.longer(columnName, cursor.getLong( i ));
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
        Log.i(this.getTag(), row.toString());
        return row;

    }

    private Map<String, SQLiteRow.HeaderCell> getIndexer() {
        if(this.index == null)
            this.index();
        return this.index;
    }

    private void index() {
        if( cursor != null
                && isOpen()) {
            this.index = new LinkedHashMap<>();

            for(int i = 0; i<cursor.getColumnCount(); i++) {
                String name = cursor.getColumnName(i);
                String type = this.type(cursor.getType(i));
                Class<?> classOf = this.classOf(i);
                SQLiteRow.HeaderCell headerCell = new SQLiteRow.HeaderCell(name, type, i, classOf);
                index.put(name, headerCell);
            }
            this.index = Collections.unmodifiableMap(this.index);
        }
    }


    private String type(int type) {
        return mapType.get(type);
    }

    private Class<?> classOf(int type) {
        return mapClass.get(type);
    }


    /**
     * close de query
     */
    public void closeCursor(){
        this.closeCursor(this.cursor);
        this.index = null;
    }

    private void closeCursor(Cursor cursor) {
        if(isOpen(cursor)) {
            cursor.close();
        }
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
        SQLRow row = catchResultRow(this.cursor, getIndexer());
        this.cursor.moveToNext();
        return row;
    }

    /**
     * Verify if cursor is open
     * @return true if cursor is open
     */
    public boolean isOpen() {
        return this.isOpen(this.cursor);
    }

    private boolean isOpen(Cursor cursor) {
        return cursor != null
                && !cursor.isClosed();
    }

    @Override
    public void close() {
        closeCursor();
        super.close();
    }
}

