package st.domain.support.android.sql.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import st.domain.support.android.sql.SelectFunctionManager;

/**
 *
 * Created by xdata on 12/24/16.
 */

public class SQLResources {

    SelectFunctionManager selectFunctionManager;
    private String tag;
    public SQLResources(SQLiteDatabase dataBase)
    {
        this.selectFunctionManager = new SelectFunctionManager(dataBase);
    }

    private SelectFunctionManager function(String name)
    {
        return this.selectFunctionManager
                .name(name);
    }

    public String current_timestamp() {
        return function("current_titmestamp")
                .executAsString();
    }

    public long last_insert_rowid() {
        return function("last_insert_rowid")
                .executAsLong();
    }

    public String current_date()
    {
        return function("current_date")
                .executAsString();
    }

    public String current_time ()
    {
        return function("current_time")
                .executAsString();
    }

    public String sqlite_version ()
    {
        return function("sqlite_version")
                .executAsString();
    }

    public String substr (String argment, int finalLentgh)
    {
        return function("substr")
                .pString(argment)
                .pLong(finalLentgh)
                .executAsString();
    }

    public String substr (String argment, int indexStrat, int finalLenght)
    {
        return function("substr")
                .pString(argment)
                .pLong(indexStrat)
                .pLong(finalLenght)
                .executAsString();
    }

    public double ABS(double argment){
        String result = function("ABS")
                .pString(String.valueOf(argment))
                .executAsString();
        return Double.parseDouble(result);
    }

    public boolean like (String argment1, String argment2)
    {
        String result =function("like")
                .pString(argment1)
                .pString(argment2)
                .executAsString();
        Log.i(this.getTag(), "like result "+ result);
        return Boolean.parseBoolean(result);
    }


    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }
}
