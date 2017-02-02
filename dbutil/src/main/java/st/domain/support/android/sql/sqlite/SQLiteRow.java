package st.domain.support.android.sql.sqlite;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import st.domain.support.android.sql.SQLRow;

/**
 *
 * Created by xdata on 12/25/16.
 */

class SQLiteRow implements SQLRow {

    private Map<String, HeaderCell> headerIndex;
    private Object[]row;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private SimpleDateFormat timestampFormant;
    private String tag;


    private SQLiteRow(int columnCount) {

        this.tag = getClass().getName();
        this.row = new Object[columnCount];
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.timeFormat = new SimpleDateFormat("HH:mm:ss");
        this.timestampFormant = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    }

    SQLiteRow (int columnCount,  Map<String, HeaderCell> headerIndex) {
        this(columnCount);
        this.headerIndex = headerIndex;
    }

    @Override
    public Object value(String columnName) {
        return this.row[indexOf(columnName)];
    }

    private int indexOf(String columnName) {
        return this.headerIndex.get(columnName).index;
    }

    @Override
    public Integer integer(String columnName) {

        Object value = value(columnName);
        try{
            return (Integer) value;
        }catch (Exception ex) {
            return Integer.valueOf(value.toString());
        }

    }

    @Override
    public Float real(String columnName) {

        Object value = value(columnName);
        try{
            return (Float) value;
        }catch (Exception ex) {
            return Float.valueOf(value.toString());
        }

    }

    @Override
    public Double realDouble(String columnName) {

        Object value = value(columnName);
        try{
            return (Double) value;
        }catch (Exception ex) {
            return Double.valueOf(value.toString());
        }
    }

    @Override
    public String string(String columnName) {

        Object value = value(columnName);
        try{
            return (String) value;
        }catch (Exception ex) {
            return String.valueOf(value.toString());
        }
    }

    @Override
    public byte[] blob(String columnName) {

        Object value = value(columnName);
        try{
            return (byte[]) value;
        }catch (Exception ex) {
            return value.toString().getBytes();
        }

    }

    @Override
    public Date date(String columnName) {

        return this.getDate(columnName, this.dateFormat);
    }

    @Override
    public Date timestamp(String columnName) {
        return this.getDate(columnName, this.timestampFormant);
    }

    @Override
    public Date time(String columnName) {

        return getDate(columnName, this.timeFormat);
    }

    @Override
    public String type(String columnName) {
        return this.headerIndex.get(columnName).type;
    }

    private Date getDate(String columnName, SimpleDateFormat format) {
        Object value = value(columnName);
        if(value instanceof Date) return (Date) value;
        try{
            return format.parse(value.toString());
        }catch (Exception ex) {
            Log.e( this.tag, "Invalid date convert{formatter:\""+format.toPattern()+"\", value:{\""+ value+"\"}}" );
            return null;
        }
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setTimeFormat(SimpleDateFormat timeFormat) {
        this.timeFormat = timeFormat;
    }

    public void setTimestampFormant(SimpleDateFormat timestampFormant) {
        this.timestampFormant = timestampFormant;
    }

    private void put(String columnName, Object rowCell) {
        int index = this.indexOf(columnName);
        this.row[index] = rowCell;
    }

    void blob(String columnName, byte[] bytesValue) {
        this.put(columnName, bytesValue);
    }

    void real(String columnName, Float floatValue) {
        this.put(columnName, floatValue);
    }

    void integer(String columnName, int integerValue) {
        this.put(columnName, integerValue);
    }

    void string(String columnName, String stringValue) {
        this.put(columnName, stringValue);
    }

    void value(String columnName, Object objectValue) {
        this.put(columnName, objectValue);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public String getTag() {
        return this.tag;
    }


    public String text(){
        String json = "{";
        String itemRow;
        int iCount =0;
        for(Map.Entry<String, HeaderCell> item: this.headerIndex.entrySet()){

            Object oValue = this.row[item.getValue().index];
            String value = oValue != null ? String.valueOf(oValue) : "";
            String type = String.valueOf(item.getValue().type);
            type = oValue != null ? type : "<"+type+">";
            String name =  item.getKey();
            itemRow =  "\""+name+"\":\""+type+"{"+value+"}\"";
            if(iCount == 0)
                json += itemRow;
            else json += ", "+itemRow+"";
            iCount ++;

        }
        json +="}";
        return json;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+ text();
    }

    static class HeaderCell {
        final String name;
        final String type;
        final int index;

        HeaderCell(String name, String type, int index) {
            this.name = name;
            this.type = type;
            this.index = index;
        }
    }
}
