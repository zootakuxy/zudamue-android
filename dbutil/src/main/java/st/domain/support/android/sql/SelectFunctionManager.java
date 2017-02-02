package st.domain.support.android.sql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * Created by xdata on 12/24/16.
 */

public class SelectFunctionManager {

    SQLiteDatabase database;
    private String name;
    private List<Object> list;
    private String tag;
    private String prepareFunctionName;


    public SelectFunctionManager(SQLiteDatabase database){
        this.database = database;
        this.list= new LinkedList<>();
        this.tag = this.getClass().getSimpleName();
    }

    /**
     * expecifi selectFunctionManager name
     * @param name
     * @return
     */
    public SelectFunctionManager name(String name){
        this.name = name;
        this.list.clear();
        return this;
    }

    /**
     * put string as param
     * @param argment
     * @return
     */
    public SelectFunctionManager pString(String argment)
    {
        this.list.add(argment);
        return this;
    }

    /**
     * put blob as param
     * @param argment
     * @return
     */
    public SelectFunctionManager pBlob(String argment)
    {
        this.list.add(argment);
        return this;
    }

    /**
     * put double as param
     * @param argment
     * @return
     */
    public SelectFunctionManager pDouble(double argment)
    {
        this.list.add(argment);
        return this;
    }

    /**
     * put long as param
     * @param argment
     * @return
     */
    public SelectFunctionManager pLong(long argment)
    {
        this.list.add(argment);
        return this;
    }

    /**
     * put null as param
     * @return
     */
    public SelectFunctionManager pNull()
    {
        this.list.add(null);
        return this;
    }

    /**
     * Execute any selectFunctionManager returns returns string inSelect data base
     * @return
     */
    public String executAsString(){
        this.prepareFunctionName = prepareFunctionName();
        SQLiteStatement compile = this.database.compileStatement(prepareFunctionName);
        prepareParmans(compile);
        return compile.simpleQueryForString();
    }

    /**
     * Execute any selectFunctionManager return long inSelect data base
     * @return
     */
    public long executAsLong(){
        this.prepareFunctionName = prepareFunctionName();
        SQLiteStatement compile = this.database.compileStatement("SELECT "+prepareFunctionName);
        prepareParmans(compile);
        return compile.simpleQueryForLong();
    }

    /**
     * Prepare imputs inParmm
     * @param compile
     */
    private void prepareParmans(SQLiteStatement compile) {
        int iCount = 0;
        for (Object param : list) {

            if (param == null)
                compile.bindNull(iCount++);

            if (param instanceof Double)
                compile.bindDouble(iCount++, (Double) param);

            if (param instanceof Long)
                compile.bindLong(iCount++, (Long) param);

            if (param instanceof String)
                compile.bindString(iCount++, (String) param);

            if (param instanceof byte[])
                compile.bindBlob(iCount++, (byte[]) param);
        }
    }

    /**
     * Prepare selectFunctionManager name
     * @return
     */
    private String prepareFunctionName() {

        String prepareName = this.name +"(";
        for (int i =0; i<this.list.size(); i++){
            prepareName = prepareName +"?";
            if(i+1 < this.list.size())
                prepareName = prepareName + ", ";
        }
        prepareName = prepareName + ")";
        return prepareName;
    }


    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
