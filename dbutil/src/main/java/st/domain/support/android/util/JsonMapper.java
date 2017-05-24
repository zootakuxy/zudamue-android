package st.domain.support.android.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 *
 * Created by siie2 on 4/29/17.
 */

public class JsonMapper implements Iterable<Map.Entry<String , Object>>
{
    private Object root;
    private Map< String, Object > map;
    private List< Object > list;
    private List< Object > point;
    private List< Object > location;

    public JsonMapper( String json ){
        Gson gson = new Gson();
        this.root = gson.fromJson( json,  Map.class );
        if( root instanceof  List )
            list = (List<Object>) root;
        else map = (Map<String, Object>) root;
        this.point = new LinkedList<Object>();
        this.location = new LinkedList<Object>();
    }

    /**
     * Create new instace JsonMapper basede in map root
     * @return
     */
    public static  JsonMapper newInstanceMap() {
        return  new JsonMapper( new LinkedHashMap<String,  Object>());
    }

    /**
     * Create new instance of JsonMapper basede in list root
     * @return
     */
    public static JsonMapper newInstanceList( ){
        return new JsonMapper( new LinkedList<Object>());
    }

    public JsonMapper(Map<String, Object> object) {
        this.root = object;
        this.map = object;
        this.list = null;
        this.point = new LinkedList<Object>();
        this.location = new LinkedList<Object>();
    }

    public JsonMapper( List<Object> list ){
        this.root = list;
        this.list = list;
        this.map = null;
        this.point = new LinkedList<Object>();
        this.location = new LinkedList<Object>();
    }


    /**
     * Replace root to map
     * @param map
     * @return
     */
    public JsonMapper rootMap(Map<String, Object> map ) {
        this.map( map );
        this.root = map;
        return this;
    }

    /**
     * Change map value
     * @param map
     * @return
     */
    private JsonMapper map(Map<String, Object> map) {
        this.map = map;
        this.list = null;
        return this;
    }

    /**
     * Replace root to list
     * @param list
     * @return
     */
    public JsonMapper rootList ( List<Object> list ){
        list( list);
        root = list;
        return this;
    }

    /**
     * Change pint to list
     * @param list
     * @return
     */
    private JsonMapper list( List<Object> list ) {
        this.map = null;
        this.list = list;
        return this;
    }

    public Object object( Object ... nodes) {

        Object value = null;
        List< Object > asList = asList( nodes );
        Object field = asList.remove(asList.size() - 1);
        int currentPoint = this.getCurrentPoint();
        JsonMapper mapper = this.enter( asList );

        if ( mapper != null && isInMap() )
            value = this.map.get( String.valueOf( field ) );
        else if( mapper != null && isInList() )
            value = this.list.get( Integer.valueOf( String.valueOf( field ) ) );
        this.backAt(currentPoint);
        return value;
    }



    public String string ( Object ... nodes ) {
        Object obj = object( nodes );
        return ( obj != null && (
                obj instanceof  String
                || obj instanceof  Boolean
                || obj instanceof  Byte
                || obj instanceof  Short
                || obj instanceof  Integer
                || obj instanceof  Long
                || obj instanceof  Float
                || obj instanceof  Double
                || obj instanceof  CharSequence

        ) ) ? String.valueOf( obj ): null;
    }

    public String stringForce ( String ... nodes ) {
        Object obj = object( nodes );
        return ( obj != null ) ? String.valueOf( obj ): null;
    }



    public Class<?> classOf(String ... nodes ) {
        Object value = this.object( nodes );
        if ( value == null ) return null;
        return value.getClass();
    }

    /**
     * Read Boolean
     * @param nodes
     * @return
     */
    public Boolean booleaner ( String ... nodes ){
        String num = string( nodes );
        return num != null  ? Boolean.valueOf( num ) : null;
    }

    /**
     * Read byte string
     * @param nodes
     * @return
     */
    public Byte byter ( String ... nodes ){
        Object value = object( nodes );
        return  asByte( value );
    }

    /**
     * Read short
     * @param nodes
     * @return
     */
    public Short shorter ( String ... nodes ) {
        Object value = object( nodes );
        return asShort( value );
    }

    /**
     * Read Integer
     * @param nodes
     * @return
     */
    public Integer integer( Object ... nodes ){
        Object value = object( nodes );
        return asInteger( value );
    }

    /**
     * Read Long
     * @param nodes
     * @return
     */
    public Long longer( Object ... nodes ){
        Object value = object( nodes );
        return  asLong( value );
    }

    /**
     * Read float
     * @param nodes
     * @return
     */
    public Float floater ( String ... nodes ){
        Object value = object( nodes );
        return  asFloat( value );
    }

    /**
     * Read double
     * @param nodes
     * @return
     */
    public Double doubler ( String ... nodes ){
        Object value = object( nodes );
        return  asDoubler( value );
    }

    /**
     * Conver value as number
     * @param value
     * @return
     */
    private Byte asByte( Object value) {
        if( value == null ) return null;
        try {
            if( value instanceof  Byte ) return (Byte) value;
            else if( value instanceof Number ) return ((Number) value).byteValue();
            else if( value instanceof  String ) return Byte.parseByte( String.valueOf(  value ));
        } catch ( Exception ex ){}
        return  null;
    }

    /**
     * Conver value as number
     * @param value
     * @return
     */
    private Short asShort( Object value) {
        try{
            return Short.parseShort( String.valueOf(  value ));
        } catch ( Exception ex ){}
        return  null;
    }    /**
     * Conver value as number
     * @param value
     * @return
     */
    private Integer asInteger(Object value) {
        if( value == null ) return null;
        try {
            if( value instanceof  Integer ) return (Integer) value;
            else if( value instanceof Number ) return ((Number) value).intValue();
            else if( value instanceof  String ) return Integer.parseInt( String.valueOf(  value ));
        } catch ( Exception ex ){}
        return  null;
    }    /**
     * Conver value as number
     * @param value
     * @return
     */
    private Long asLong( Object value) {
        if( value == null ) return null;
        try {
            if( value instanceof  Long ) return (Long) value;
            else if( value instanceof Number ) return ((Number) value).longValue();
            else if( value instanceof  String ) return Long.parseLong( String.valueOf(  value ));
        } catch ( Exception ex ){}
        return  null;
    }    /**
     * Conver value as number
     * @param value
     * @return
     */
    private Float asFloat(Object value) {

        if( value == null ) return null;
        try {
            if( value instanceof  Float ) return ( Float ) value;
            else if( value instanceof Number ) return ((Number) value).floatValue();
            else if( value instanceof  String ) return Float.parseFloat( String.valueOf(  value ));
        } catch ( Exception ex ){}
        return  null;
    }    /**
     * Conver value as number
     * @param value
     * @return
     */
    private Double asDoubler(Object value) {
        if( value == null ) return null;
        try {
            if( value instanceof  Double ) return ( Double) value;
            else if( value instanceof Number ) return ((Number) value).doubleValue();
            else if( value instanceof  String ) return Double.parseDouble( String.valueOf(  value ));
        } catch ( Exception ex ){}
        return  null;
    }

    /**
     * Read list
     * @param nodes
     * @return
     */
    public List lister( String ... nodes ){
        return (List) object( nodes );
    }

    /**
     * Read list
     * @param nodes
     * @return
     */
    public Map<String, Object> mapperNode( String ... nodes ){
        return (Map<String, Object> ) object( nodes );
    }




    public boolean has( Object ... nodes) {
        boolean  result = false;
        List<Object> asList = this.asList(nodes);
        Object field = asList.remove(asList.size() - 1 );

        int startPoint = this.getCurrentPoint();
        JsonMapper mapper = this.enter(asList);
        result =  mapper != null  && mapper.contains( field );
        this.backAt( startPoint );
        return result;
    }

    /**
     * Vefiricas se conten um dada valor
     * @param field
     * @return
     */
    private boolean contains(Object field) {
        if( isInMap() && field instanceof  String )
            return this.map.containsKey( field );
        else if( isInList() && field instanceof  Integer )
            return (Integer) field >= 0 && (Integer) field < list.size();
        return false;
    }


    /**
     * Verificar se consegue entrar em uma localizacao
     * @param nodes
     * @return
     */
    public boolean canEnter( Object ... nodes ){
        if( nodes == null || nodes.length == 0 ) return  false;
        return this.canEnter(map, list, asList( nodes ) );
    }

    /**
     *
     * @param map
     * @param list
     * @param nodes
     * @return
     */
    private boolean canEnter(Map<String, Object> map, List<Object> list, List<Object> nodes) {
        int index;
        String node;
        Object field = nodes.remove( 0 );
        Object value = null;
        String fieldType = null;
        if ( field != null && field instanceof  String && map != null && list == null ){
            node = String.valueOf( field );
            value = map.get( node );
            fieldType = "String";
        }
        else if( field != null && field instanceof  Integer && list != null && map == null){
            index = (Integer ) field;
            value = (index >= 0 && index < list.size() )? list.get( index ): null;
            fieldType = "Integer";
        }

        if( nodes.size() == 0 )
            return  value != null
                    && ( value instanceof  List || value instanceof  Map );
        else if( value != null ){
            if( value instanceof  Map) return  canEnter((Map<String, Object>) value, null, nodes );
            else if( value instanceof  List ) return canEnter( null, (List<Object>) value, nodes );
        }
        return false;
    }


    /**
     * Enter in multiples nodes
     * @param nodesLocations
     * @return
     */
    public JsonMapper enter ( Object ... nodesLocations ){
        if( ! canEnter( nodesLocations ) ) return null;
        int startPoint = this.getCurrentPoint();
        JsonMapper mapper = enter( asList( nodesLocations ) );
        if( mapper == null ) this.backAt( startPoint );

        return mapper;
    }

    /**
     *
     * @param nodes
     * @return
     */
    private JsonMapper enter( List<Object> nodes ){
        if( nodes.size() < 1 ) return this;
        Object field = nodes.remove(0 );
        Object value = null;
        if( field != null && isInMap() && field instanceof  String )
            value = this.map.get( field );
        else if( field != null && isInList() && field instanceof  Integer ){
            int index = (Integer) field;
            value = ( index > -1 && index < this.list.size())? list.get( index ): null;
        }

        JsonMapper mapper = enter( field, value );

        if( mapper != null && nodes.size() > 0 ) return  enter( nodes );
        return mapper != null ? this : null;
    }

    private JsonMapper enter(Object field, Object value) {
        if( value == null ) return  null;
        if( isInMap() )
            this.point.add( 0, this.map );
        else if( isInList() )
            point.add(0, this.list);
        else return null;

        if( value instanceof Map ){
            this.map((Map<String, Object>) value);
            this.location.add( field );
            return this;
        }
        else if( value instanceof  List ){
            this.list( (List<Object>) value );
            this.location.add( field );
            return this;
        }
        return null;
    }


    /**
     * Convert on array to list | LinkedList
     * @param nodes
     * @return
     */
    private List<String> asList(String ... nodes) {
        return new LinkedList<String>( Arrays.asList(nodes));
    }

    /**
     * Convert on array to list | LinkedList
     * @param nodes
     * @return
     */
    private List<Object> asList(Object ... nodes) {
        return new LinkedList<Object>( Arrays.asList( nodes ));
    }

    private String[] asArray(String name) {
        return new String[] { name };
    }

    /**
     *
     * @return
     */
    public boolean hasBack(){
        return  !this.point.isEmpty();
    }

    /**
     * @return
     */
    public JsonMapper back() {
        if( hasBack() ){
            Object point = this.point.remove(0);
            this.location.remove( location.size() -1 );
            if( point instanceof  Map ) this.map( (Map<String, Object>) point);
            else if( point instanceof List ) this.list( (List<Object>) point);
            return this;
        }
        return null;
    }


    /**
     * Back multiples point
     * @param point
     * @return
     */
    public JsonMapper back( int point ) {
        if( point < 1 || point > this.point.size() ) return null;
        while ( point > 0 ) {
            this.back();
            point--;
        }
        return this;
    }

    /**
     * Back point at specified start point
     * @param startPoint
     * @return
     */
    public JsonMapper backAt( int startPoint ) {
        if( startPoint < 0 || startPoint > this.point.size() ) return null;
        while ( getCurrentPoint() > startPoint ){
            this.back();
        }
        return this;
    }


    /**
     * Back to root rootMap
     * @return
     */
    public JsonMapper root() {
        if( !this.isInRoot() ) {
            this.map = this.getRootAsMap();
            this.list = this.getRootAsList();
            this.point.clear();
        }
        return this;
    }

    /**
     *
     * @return
     */
    public Map<String, Object> rootMap() {
        return this.map;
    }


    /**
     * Create dependence and last as value
     * @param nodesLastValue
     * @return
     */
    public JsonMapper create( Object ... nodesLastValue ){

        if (nodesLastValue == null || nodesLastValue.length < 2 ) return null;

        List<Object> asList =  this.asList( nodesLastValue );
        List<Object> canEnter = new LinkedList<Object>();

        int lastCan = 0;
        Object value = asList.remove( asList.size() -1 );
        Object key = asList.remove( asList.size() -1 );
        int startPoint = this.getCurrentPoint();

        if( key == null ) return  null;
        else if ( ! ( ( key instanceof String) || ( key instanceof Integer ) ) ) return null;

        if( !asList.isEmpty()) {
            boolean last;
            do
                canEnter.add( asList.get( lastCan++ ));
            while ( ( last = canEnter( canEnter.toArray()) ) && lastCan < asList.size() );
            if( !last ) {
                canEnter.remove( canEnter.size() -1 );
                lastCan --;
            }
        }

        if( canEnter.size() < asList.size() ){
            this.enter( canEnter );
            canEnter.clear();
            for( lastCan = lastCan; lastCan < asList.size(); lastCan ++ ){
                canEnter.add( asList.get(lastCan));
            }
            canEnter.add( key );

            if( ! canCreate( canEnter) ){
                backAt( startPoint );
                return null;
            }
            else if( key instanceof Integer && (Integer) key != 0 ) return null;
            else create( canEnter, isInMap()? this.map: isInList()? this.list : null );
        }

        backAt( startPoint );
        this.enter( asList );

        if( ( key instanceof  String && !isInMap() )
                || ( key instanceof  Integer && !isInList() )
                || ( key instanceof  Integer && ( ( Integer ) key< 0 || ( Integer ) key > list.size()) )
        ){
            backAt(startPoint);
            return null;
        }

        if( key instanceof  String && isInMap() )
            this.map.put( (String) key, value );
        else if( key instanceof  Integer && isInList() && (Integer) key < list.size() )
            this.list.set((Integer) key, value );
        else if( key instanceof  Integer && isInList() && (Integer) key == list.size() )
            list.add(value);
        else throw  new RuntimeException( "Algo deu erado ao adicionar ");

        this.backAt( startPoint );
        return this;
    }


    public JsonMapper removeSingle( Object node ){
        if ( isInMap() && node instanceof  String ){
            map.remove( node );
            return this;
        }
        else if ( isInList() && node instanceof  Integer ){
            int index = ( Integer ) node;
            this.list.remove( index);
            return this;
        }
        return  null;
    }



    private boolean canCreate( List<Object> nodes ){
        for( Object node: nodes ){
            if( node == null )return  false;
            else if( node instanceof  String ) continue;
            else if( node instanceof Integer && (Integer) node == 0) continue;
            else return false;
        }
        return true;
    }

    private boolean create( List<Object> nodes, Object startContainer ){
        Object node = nodes.remove( 0 );
        Object nextNode = ( nodes.size()>0 )? nodes.get( 0 ) :  null;
        Map<String, Object> map = startContainer instanceof  Map ? (Map<String, Object>) startContainer : null;
        List<Object> list = startContainer instanceof  LinkedList ? (List<Object>) startContainer : null;



        if( nextNode != null ){
            if( node instanceof String && nextNode instanceof String && map != null )
                map.put( ( String ) node, startContainer = new LinkedHashMap<String, Object>() );
            else if( node instanceof String && nextNode instanceof Integer  && map != null )
                map.put( (String) node, startContainer =  new LinkedList<Object>() );
            else if( node instanceof Integer && nextNode instanceof String && list != null )
                list.add( startContainer =  new LinkedHashMap<String, Object>());
            else if( node instanceof  Integer && nextNode instanceof Integer && list != null )
                list.add( startContainer =  new LinkedList<Object>());
            else throw  new RuntimeException( "Algo deu erado ! ");
            return create( nodes, startContainer );
        }
        return true;
    }

    public JsonMapper putSingle( String key, Object value ){
        this.map.put( key, value);
        return this;
    }


    public JsonMapper setSingle( int index, Object value ){
        if( ! isInList() )return  null;
        if( index < 0 || index > list.size() -1 ) return  null;
        this.list.set( index , value );
        return  this;
    }

    public JsonMapper addSingle( int index, Object value ){
        if( ! isInList() )return  null;
        if( index < 0 || index > list.size() ) return  null;
        this.list.add( index , value );
        return  this;
    }

    public  JsonMapper addSingle( Object value ){
        if( ! isInList() ) return  null;
        return this.addSingle( this.list.size(), value );
    }


    /**
     *
     * @param pairs
     * @return
     */
    public JsonMapper putPairsValues(Object ... pairs ) {
        //Quando os valores nao for par
        if( !isInMap() ) return null;
        if( pairs.length % 2 != 0 ) return null;
        Object key;
        Object value;
        for ( int i = 0; i < pairs.length;  i = i + 2 ){
            key = pairs[ i ];
            value = pairs [ i+1 ];
            if( key == null || !( key instanceof  CharSequence ) ) return null;
            this.putSingle( String.valueOf(key), value);
        }
        return this;
    }

    public JsonMapper addPairsValues(Object ... pairs ){
        //Quando os valores nao for par
        if( !isInList() ) return null;
        if( pairs.length % 2 != 0 ) return null;
        Object key;
        Object value;
        Map<String, Object> pair = new  LinkedHashMap<String,Object>();
        for ( int i = 0; i < pairs.length;  i = i + 2 ){
            key = pairs[ i ];
            value = pairs [ i+1 ];
            if( key == null || !( key instanceof  CharSequence ) ) return null;
            pair.put( String.valueOf(key), value);
        }
        this.addSingle( pair );
        return this;
    }


    public JsonMapper createNode(Object ... nodes ) {
        List<Object> asList = this.asList( nodes );
        asList.add( new LinkedHashMap<String, Object>());
        this.create( asList.toArray() );
        return this.enter( nodes );
    }


    public JsonMapper createList( Object ... nodes ){
        List<Object> asList = this.asList( nodes );
        asList.add( new LinkedList<Object>());
        this.create( asList.toArray() );
        return this.enter( nodes );
    }

    /**
     *
     * @return
     */
    public String toJson(){
        GsonBuilder builder = new GsonBuilder()
                .setLenient()

                ;
        Gson gson = builder.create();
        return gson.toJson(this.getRoot());
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

    /**
     *
     * @return
     */
    public int countElement(){
        return this.map.size();
    }

    //@Override
    public Iterator< Map.Entry< String, Object > > iterator() {
        return map.entrySet().iterator();
    }

    public Object getRoot() {
        return this.root;
    }

    /**
     * Get root as map if root as one map
     * @return
     */
    public Map<String, Object> getRootAsMap() {
        return ( this.root != null && root instanceof  Map )? (Map<String, Object>) root: null;
    }

    /**
     * Get root as list if roos as one list
     * @return
     */
    public List<Object> getRootAsList() {
        return ( this.root != null && root instanceof  List )? (List<Object>) root : null;
    }


    /**
     * get the current location
     * @return
     */
    public Object [] getLocation(){
        return this.location.toArray();
    }

    /**
     * Get the current point entred
     * @return
     */
    public int getCurrentPoint() {
        return this.point.size();
    }


    public boolean isRootMap(){
        return this.root instanceof  Map;
    }

    public boolean isRootList() {
        return  this.root instanceof List;
    }

    public boolean isInRoot(){
        return this.point.isEmpty();
    }

    public boolean isInMap() {
        return this.map != null;
    }

    public boolean isInList() {
        return this.list != null;
    }
}
