package st.domain.support.android.util;

import com.google.gson.Gson;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by siie2 on 4/29/17.
 */

public class JsonMapper implements Iterable<Map.Entry<String , Object>> {
    private Map< String, Object > map;
    private List<Map<String, Object> > points;

    public JsonMapper() {
        this.points = new LinkedList<>();
        this.map = new LinkedHashMap<>();
    }

    public JsonMapper( String json ){
        Gson gson = new Gson();
        this.map = gson.fromJson( json,  Map.class );
        this.points = new LinkedList<>();
    }

    public JsonMapper(Map<String, Object> map) {
        this.map = map;
        this.points = new LinkedList<>();
    }



    public JsonMapper map(Map<String, Object> map) {
        this.points = new LinkedList<>();
        this.map = map;
        return this;
    }

    /**
     * Read object
     * @param name
     * @return
     */
    public Object object ( String name ){
        return map.get( name );
    }

    /**
     * Read String
     * @param name
     * @return
     */
    public String string ( String name ){
        Object obj = object( name );
        return (obj == null) ? null : obj.toString();
    }

    /**
     * Read Boolean
     * @param name
     * @return
     */
    public Boolean booleaner ( String name ){
        return Boolean.valueOf( string( name ) );
    }

    /**
     * Read byte string
     * @param name
     * @return
     */
    public Byte byter ( String name ){
        return Byte.valueOf( string( name ) );
    }

    /**
     * Read short
     * @param name
     * @return
     */
    public Short shorter ( String name ) {
        return Short.valueOf( string( name ) );
    }

    /**
     * Read Integer
     * @param name
     * @return
     */
    public Integer integer( String name ){
        return Integer.valueOf( string( name ) );
    }

    /**
     * Read Long
     * @param name
     * @return
     */
    public Long longer( String name ){
        return Long.valueOf( string( name ) );
    }

    /**
     * Read float
     * @param name
     * @return
     */
    public Float floater ( String name ){
        return Float.valueOf( string( name ) );
    }

    /**
     * Read double
     * @param name
     * @return
     */
    public Double doubler ( String name ){
        return Double.valueOf( string( name ) );
    }

    /**
     * Read list
     * @param name
     * @return
     */
    public List lister( String name ){
        return (List) object( name );
    }

    /**
     * Read list
     * @param name
     * @return
     */
    public Map mapper( String name ){
        return (Map) object( name );
    }


    /**
     * Read list
     * @param name
     * @return
     */
    public Map<String, Object> objectMapper( String name ){
        return (Map) object( name );
    }

    public boolean has(String name) {
        return this.map.containsKey( name );
    }


    public boolean canEnter( String name ){
        Object o = object(name);
        return o != null
                && o instanceof Map;
    }
    /**
     *
     * @param name
     */
    public JsonMapper enter(String name) {
        if(!canEnter( name ) ) return null;
        this.points.add( this.map );
        this.map( this.mapper( name) );
        return this;
    }

    /**
     *
     * @return
     */
    public boolean hasBack(){
        return  !this.points.isEmpty();
    }

    /**
     *
     * @return
     */
    public JsonMapper back() {
        if( hasBack() ){
            int backPosition = points.size() -1;
            Map<String, Object> map = points.get( backPosition);
            this.map( map );
            this.points.remove( backPosition );
            return this;
        }
        return null;
    }

    /**
     *
     * @return
     */
    public Map<String, Object> rootMap() {
        if( this.points.size()>0 ) return points.get(0);
        return map;
    }

    public JsonMapper put(String name, String value) {
        this.map.put(name, value);
        return this;
    }

    public JsonMapper put( String name, Object value ) {
        this.map.put(name, value);
        return this;
    }

    public JsonMapper put( String name, Map<String, Object> map ){
        this.map.put( name, map );
        return this;
    }

    public JsonMapper put( String name, List<Object> value ){
        this.map.put( name, value );
        return this;
    }

    public JsonMapper put( String name, boolean value ){
        this.map.put(name, value);
        return this;
    }

    public JsonMapper put( String name, byte value ){
        this.map.put(name, value );
        return this;
    }

    public JsonMapper put(String name, short value ){
        this.map.put( name, value );
        return this;
    }

    public JsonMapper put( String name, int value ){
        this.map.put( name, value );
        return this;
    }

    public JsonMapper put( String name, long value ){
        this.map.put( name, value);
        return this;
    }

    public JsonMapper put( String name, float value ){
        this.map.put( name, value );
        return this;
    }

    public JsonMapper put( String name, double value ){
        this.map.put( name, value );
        return this;
    }

    public JsonMapper createMap(String name) {
        return this.put(name, new LinkedHashMap<String, Object>());
    }

    public JsonMapper createList( String name ){
        return this.put( name, new LinkedList<>());
    }

    /**
     *
     * @return
     */
    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson( this.rootMap() );
    }

    /**
     *
     * @return
     */
    public int countElement(){
        return this.map.size();
    }

    @Override
    public Iterator< Map.Entry< String, Object > > iterator() {
        return map.entrySet().iterator();
    }

    public void backRoot() {
        while ( hasBack() )
            back();
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson( this.rootMap() );
    }
}
