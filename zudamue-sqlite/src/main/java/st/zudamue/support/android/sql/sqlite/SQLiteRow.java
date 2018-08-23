package st.zudamue.support.android.sql.sqlite;

import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import st.zudamue.support.android.util.exception.ZudamueException;
import st.zudamue.support.android.sql.SQLRow;
import st.zudamue.support.android.sql.object.Identifier;

/**
 * Created by xdaniel on 12/25/16.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
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

    SQLiteRow (int columnCount,  Map<String, HeaderCell> headerIndex ) {
        this(columnCount);
        this.headerIndex = headerIndex;
    }

    @Override
    public Object value(CharSequence columnName) {
        return this.row[ indexOf(columnName) ];
    }

    private int indexOf(CharSequence coll) {
        String columnName = coll instanceof Identifier ? ((Identifier) coll).name() : String.valueOf( coll );
        if( !this.headerIndex.containsKey(columnName) )
            throw new ZudamueException( "the column " + columnName + " not found" );
        return this.headerIndex.get(columnName).index;
    }

    @Override
    public Long longer( CharSequence columnName ) {

        Object value = value(columnName);
        if(value == null) return null;
        try{
            return (Long) value;
        }catch (Exception ex) {
            return Long.valueOf(String.valueOf(value));
        }
    }

    @Override
    public Integer integer(CharSequence columnName) {

        Object value = value(columnName);
        if(value == null) return null;
        try{
            return (Integer) value;
        }catch (Exception ex) {
            return Integer.valueOf(String.valueOf(value));
        }
    }

    @Override
    public Boolean booleaner(CharSequence columnName) {
        Object value = value(columnName);
        if(value == null) return null;
        try{
            return (Boolean) value;
        }catch (Exception ex) {
            String aValue = String.valueOf(value);
            if( aValue.toLowerCase().equals("t")) aValue = "true";
            else if( aValue.toLowerCase().equals("1")) aValue = "true";
            else if( aValue.toLowerCase().equals("0")) aValue = "false";

            return Boolean.valueOf( aValue );
        }
    }

    @Override
    public Float real(CharSequence columnName) {

        Object value = value(columnName);
        if(value == null) return null;
        try{
            return (Float) value;
        }catch (Exception ex) {
            return Float.valueOf(String.valueOf(value));
        }

    }

    @Override
    public Double realDouble(CharSequence columnName) {

        Object value = value(columnName);
        if(value == null) return null;
        try{
            return (Double) value;
        }catch (Exception ex) {
            return Double.valueOf(String.valueOf(value));
        }
    }

    @Override
    public String string(CharSequence columnName) {

        Object value = value(columnName);
        try{
            return (String) value;
        }catch (Exception ex) {
            return String.valueOf(String.valueOf(value));
        }
    }

    @Override
    public byte[] blob(CharSequence columnName) {

        Object value = value(columnName);
        if(value == null) return null;
        try{
            return (byte[]) value;
        }catch (Exception ex) {
            return value.toString().getBytes();
        }

    }

    @Override
    public Date date(CharSequence columnName) {

        return this.getDate(columnName, this.dateFormat);
    }

    @Override
    public Date timestamp(CharSequence columnName) {
        return this.getDate(columnName, this.timestampFormant);
    }

    @Override
    public Date time(CharSequence columnName) {

        return getDate(columnName, this.timeFormat);
    }

    @Override
    public String typeOf(CharSequence columnName) {
        return this.headerIndex.get(columnName).type;
    }

    @Override
    public int columnsCount() {
        return this.headerIndex.size();
    }

    @Override
    public boolean hasColumn(CharSequence columnName) {
        return this.headerIndex.containsKey( String.valueOf( columnName ) );
    }

    @Override
    public <T> T get( CharSequence columnName, Class< T > type) {
        return ( T ) this.value( columnName );
    }

    @Override
    public String asJson() {
        Map< String, Object > map = new LinkedHashMap<>();
        for( String colun: this.headerIndex.keySet() )
            map.put( colun, this.value( colun ) );
        return new Gson().toJson( map );
    }

    @Override
    public String columnNameOf(int index) {
        if(index <0 || index >= this.headerIndex.size()) return null;
        return new LinkedList<>( this.headerIndex.keySet() ).get( index );
    }

    @Override
    public Class<?> classOf(CharSequence columnName) {
        return this.headerIndex.get(columnName).classOf;
    }

    private Date getDate(CharSequence columnName, SimpleDateFormat format) {
        Object value = value(columnName);
        if( value == null ) return null;
        else if(value instanceof Date) return (Date) value;
        try{
            return format.parse(value.toString());
        }catch (Exception ex) {
            Log.e( this.tag, "Invalid value convert{formatter:\""+format.toPattern()+"\", value:{\""+ value+"\"}}" );
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


    public void longer(String columnName, Long longValue) {
        this.put( columnName, longValue );
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
        public Class<?> classOf;

        HeaderCell(String name, String type, int index, Class<?> classOf) {
            this.name = name;
            this.type = type;
            this.index = index;
            this.classOf = classOf;
        }
    }
}
