package st.zudamue.support.android.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import st.zudamue.support.android.util.exception.ZudamueException;


/**
 * Created by xdaniel on 4/29/17.
 *
 * @author Daniel Costa <costa.xdaniel@gmail.com>
 */

public class JsonMapper implements Iterable<Map.Entry<String, Object>> {


    public static JsonMapper parse(String jsonText) {
        if (jsonText == null) return null;
        JsonMapper mapper = null;
        try {
            mapper = new JsonMapper(jsonText);
        } catch (Exception ignored) {
        }
        return mapper;
    }

    public static boolean isJsonFromater(String jsonText) {
        return parse(jsonText) != null;
    }

    public static Gson gsonInstance() {
        Gson gson = new Gson();
        return gson;
    }

    public static JsonMapper from(Object data) {
        if (data == null) return null;
        if (data instanceof Map) return new JsonMapper((Map<String, Object>) data);
        if (data instanceof List) return new JsonMapper((List<Object>) data);
        return JsonMapper.parse(JsonMapper.gsonInstance().toJson(data));
    }

    public static JsonMapper fromRaw(Context context, int rawId) {
        InputStream input = context.getResources().openRawResource(rawId);
        Gson gson = new Gson();
        JsonReader read = new JsonReader(new InputStreamReader(input));
        Object o = gson.fromJson(read, Object.class);
        if (o == null) return null;
        if (o instanceof Map) return new JsonMapper((Map<String, Object>) o);
        if (o instanceof List) return new JsonMapper((List<Object>) o);
        return null;
    }



    @Expose
    private Object root;

    private Map<String, Object> map;
    private List<Object> list;
    private List<Object> point;
    private List<Object> location;

    public JsonMapper(String json) {
        Gson gson = JsonMapper.gsonInstance();
        try {
            this.root = gson.fromJson(json, Map.class);
        } catch (Exception e) {
            this.root = gson.fromJson(json, List.class);
        }

        if (root == null) throw new ZudamueException("invalid json");
        if (root instanceof Map)
            map = (Map<String, Object>) root;
        else if (root instanceof List)
            list = (List<Object>) root;

        this.point = new LinkedList<Object>();
        this.location = new LinkedList<Object>();
    }

    /**
     * Create new instace JsonMapper basede in map backToRoot
     *
     * @return
     */
    public static JsonMapper newObjectInstance() {
        return new JsonMapper(new LinkedHashMap<String, Object>());
    }

    /**
     * Create new instance of JsonMapper basede in list backToRoot
     *
     * @return
     */
    public static JsonMapper newInstanceList() {
        return new JsonMapper(new LinkedList<Object>());
    }

    public JsonMapper(Map<String, Object> object) {
        this.root = object;
        this.map = object;
        this.list = null;
        this.point = new LinkedList<>();
        this.location = new LinkedList<>();
    }

    public JsonMapper(List<Object> list) {
        this.root = list;
        this.list = list;
        this.map = null;
        this.point = new LinkedList<Object>();
        this.location = new LinkedList<Object>();
    }

    /**
     * Replace backToRoot to map
     *
     * @param map
     * @return
     */
    public synchronized JsonMapper rootMap(Map<String, Object> map) {
        this.map(map);
        this.root = map;
        return this;
    }

    /**
     * Change map value
     *
     * @param map
     * @return
     */
    private synchronized JsonMapper map(Map<String, Object> map) {
        this.map = map;
        this.list = null;
        return this;
    }

    /**
     * Replace backToRoot to list
     *
     * @param list
     * @return
     */
    public synchronized JsonMapper rootList(List<Object> list) {
        list(list);
        root = list;
        return this;
    }

    /**
     * Change pint to list
     *
     * @param list
     * @return
     */
    private synchronized JsonMapper list(List<Object> list) {
        this.map = null;
        this.list = list;
        return this;
    }

    /**
     * Devolver um elemento do mapper
     *
     * @param nodes
     * @return
     */
    public synchronized Object element(Object... nodes) {

        Object value = null;
        List<Object> asList = asList(nodes);
        Object field = asList.remove(asList.size() - 1);
        int currentPoint = this.getCheckPoint();
        JsonMapper mapper = this.enter(asList);

        if (this.isInList() && !(
                field.getClass().equals(Long.class)
                        || field.getClass().equals(Integer.class)
                        || field.getClass().equals(Short.class)
                        || field.getClass().equals(Byte.class)
        )
                )
            throw new ZudamueException("Invalid index integer");

        if (mapper != null && isInMap())
            value = this.map.get(String.valueOf(field));
        else if (mapper != null && isInList())
            value = this.list.get(Integer.valueOf(String.valueOf(field)));
        this.backAt(currentPoint);

        return value;
    }


    /**
     * Delevolver um elemento como json
     *
     * @param nodes
     * @return
     */
    public synchronized String elementAsJson(Object... nodes) {
        return new Gson().toJson(element(nodes));
    }

    /**
     * Obter o submapper de um elemento
     *
     * @param nodes
     * @return
     */
    public synchronized JsonMapper suMapper(Object... nodes) {
        return JsonMapper.parse(elementAsJson(nodes));
    }


    public synchronized String string(Object... nodes) {
        Object obj = element(nodes);
        return (obj != null && (
                obj instanceof String
                        || obj instanceof Boolean
                        || obj instanceof Byte
                        || obj instanceof Short
                        || obj instanceof Integer
                        || obj instanceof Long
                        || obj instanceof Float
                        || obj instanceof Double
                        || obj instanceof CharSequence

        )) ? String.valueOf(obj) : null;
    }

    public synchronized String stringForce(Object... nodes) {
        Object obj = element(nodes);
        return (obj != null) ? String.valueOf(obj) : null;
    }

    public synchronized Class<?> classOf(Object... nodes) {
        Object value = this.element(nodes);
        if (value == null) return null;
        return value.getClass();
    }

    /**
     * Read Boolean
     *
     * @param nodes
     * @return
     */
    @Nullable
    public synchronized Boolean booleaner(Object... nodes) {
        String num = string(nodes);
        return num != null ? Boolean.valueOf(num) : null;
    }

    /**
     * Read boolean
     *
     * @param nodes
     * @return false if null
     */
    public synchronized boolean bool(Object... nodes) {
        String bool = string(nodes);
        if (bool == null) return false;
        return Boolean.parseBoolean(bool);
    }

    /**
     * Read byte string
     *
     * @param nodes
     * @return
     */
    public synchronized Byte byter(Object... nodes) {
        Object value = element(nodes);
        return asByte(value);
    }

    /**
     * Read short
     *
     * @param nodes
     * @return
     */
    public synchronized Short shorter(Object... nodes) {
        Object value = element(nodes);
        return asShort(value);
    }

    /**
     * Read Integer
     *
     * @param nodes
     * @return
     */
    public synchronized Integer integer(Object... nodes) {
        Object value = element(nodes);
        return asInteger(value);
    }

    /**
     * Read Long
     *
     * @param nodes
     * @return
     */
    public synchronized Long longer(Object... nodes) {
        Object value = element(nodes);
        return asLong(value);
    }

    /**
     * Read float
     *
     * @param nodes
     * @return
     */
    public synchronized Float floater(Object... nodes) {
        Object value = element(nodes);
        return asFloat(value);
    }

    /**
     * Read double
     *
     * @param nodes
     * @return
     */
    public synchronized Double doubler(Object... nodes) {
        Object value = element(nodes);
        return asDoubler(value);
    }

    /**
     * Conver value as number
     *
     * @param value
     * @return
     */
    private synchronized Byte asByte(Object value) {
        if (value == null) return null;
        try {
            if (value instanceof Byte) return (Byte) value;
            else if (value instanceof Number) return ((Number) value).byteValue();
            else if (value instanceof String) return Byte.parseByte(String.valueOf(value));
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Conver value as number
     *
     * @param value
     * @return
     */
    private synchronized Short asShort(Object value) {
        try {
            return Short.parseShort(String.valueOf(value));
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Conver value as number
     *
     * @param value
     * @return
     */
    private synchronized Integer asInteger(Object value) {
        if (value == null) return null;
        try {
            if (value instanceof Integer) return (Integer) value;
            else if (value instanceof Number) return ((Number) value).intValue();
            else if (value instanceof String) return Integer.parseInt(String.valueOf(value));
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Conver value as number
     *
     * @param value
     * @return
     */
    private synchronized Long asLong(Object value) {
        if (value == null) return null;
        try {
            if (value instanceof Long) return (Long) value;
            else if (value instanceof Number) return ((Number) value).longValue();
            else if (value instanceof String) return Long.parseLong(String.valueOf(value));
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Conver value as number
     *
     * @param value
     * @return
     */
    private synchronized Float asFloat(Object value) {

        if (value == null) return null;
        try {
            if (value instanceof Float) return (Float) value;
            else if (value instanceof Number) return ((Number) value).floatValue();
            else if (value instanceof String) return Float.parseFloat(String.valueOf(value));
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Conver value as number
     *
     * @param value
     * @return
     */
    private synchronized Double asDoubler(Object value) {
        if (value == null) return null;
        try {
            if (value instanceof Double) return (Double) value;
            else if (value instanceof Number) return ((Number) value).doubleValue();
            else if (value instanceof String) return Double.parseDouble(String.valueOf(value));
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Read list
     *
     * @param nodes
     * @return
     */
    public synchronized List lister(Object... nodes) {
        return (List) element(nodes);
    }

    /**
     * Read list
     *
     * @param nodes
     * @return
     */
    public Map<String, Object> mapperNode(Object... nodes) {
        return (Map<String, Object>) element(nodes);
    }

    /**
     * Verify if has nodes
     *
     * @param nodes
     * @return
     */
    public synchronized boolean has(Object... nodes) {
        boolean result = false;
        List<Object> asList = this.asList(nodes);
        Object field = asList.remove(asList.size() - 1);

        int startPoint = this.getCheckPoint();
        JsonMapper mapper = this.enter(asList);
        result = mapper != null && mapper.contains(field);
        this.backAt(startPoint);
        return result;
    }

    /**
     * Vefiricas se conten um dada valor
     *
     * @param field
     * @return
     */
    private synchronized boolean contains(Object field) {
        if (isInMap() && field instanceof String)
            return this.map.containsKey(field);
        else if (isInList() && field instanceof Integer)
            return (Integer) field >= 0 && (Integer) field < list.size();
        return false;
    }


    /**
     * Verificar se consegue entrar em uma localizacao
     *
     * @param nodes
     * @return
     */
    public synchronized boolean canEnter(Object... nodes) {
        if (nodes == null || nodes.length == 0) return false;
        return this.canEnter(map, list, asList(nodes));
    }

    /**
     * @param map
     * @param list
     * @param nodes
     * @return
     */
    private synchronized boolean canEnter(Map<String, Object> map, List<Object> list, List<Object> nodes) {
        int index;
        String node;
        Object field = nodes.remove(0);
        Object value = null;
        String fieldType = null;
        if (field != null && field instanceof String && map != null && list == null) {
            node = String.valueOf(field);
            value = map.get(node);
            fieldType = "String";
        } else if (field != null && field instanceof Integer && list != null && map == null) {
            index = (Integer) field;
            value = (index >= 0 && index < list.size()) ? list.get(index) : null;
            fieldType = "Integer";
        }

        if (nodes.size() == 0)
            return value != null
                    && (value instanceof List || value instanceof Map);
        else if (value != null) {
            if (value instanceof Map) return canEnter((Map<String, Object>) value, null, nodes);
            else if (value instanceof List) return canEnter(null, (List<Object>) value, nodes);
        }
        return false;
    }


    /**
     * Enter in multiples nodes
     *
     * @param nodesLocations
     * @return
     */
    public synchronized JsonMapper enter(Object... nodesLocations) {
        if (!canEnter(nodesLocations)) return null;
        int startPoint = this.getCheckPoint();
        JsonMapper mapper = enter(asList(nodesLocations));
        if (mapper == null) this.backAt(startPoint);

        return mapper;
    }

    /**
     * @param nodes
     * @return
     */
    private synchronized JsonMapper enter(List<Object> nodes) {
        if (nodes.size() < 1) return this;
        Object field = nodes.remove(0);
        Object value = null;
        if (field != null && isInMap() && field instanceof String)
            value = this.map.get(field);
        else if (field != null && isInList() && field instanceof Integer) {
            int index = (Integer) field;
            value = (index > -1 && index < this.list.size()) ? list.get(index) : null;
        }

        JsonMapper mapper = enter(field, value);

        if (mapper != null && nodes.size() > 0) return enter(nodes);
        return mapper != null ? this : null;
    }

    private synchronized JsonMapper enter(Object field, Object value) {
        if (value == null) return null;
        if (isInMap()) {
            Object add = this.map;
            return addPoint(field, value, add);
        } else if (isInList())
            return this.addPoint(field, value, this.list);
        else return null;
    }

    @NonNull
    private synchronized JsonMapper addPoint(Object field, Object value, Object addValue) {
        this.point.add(0, addValue);
        this.location.add(field);
        if (value instanceof Map) {
            this.map((Map<String, Object>) value);
        } else if (value instanceof List) {
            this.list((List<Object>) value);
        }
        return this;
    }


    /**
     * Convert on array to list | LinkedList
     *
     * @param nodes
     * @return
     */
    private synchronized List<String> asList(String... nodes) {
        return new LinkedList<String>(Arrays.asList(nodes));
    }

    /**
     * Convert on array to list | LinkedList
     *
     * @param nodes
     * @return
     */
    private synchronized List<Object> asList(Object... nodes) {
        return new LinkedList<Object>(Arrays.asList(nodes));
    }

    private String[] asArray(String name) {
        return new String[]{name};
    }

    /**
     * @return
     */
    public synchronized boolean hasBack() {
        return !this.point.isEmpty()
                && !this.location.isEmpty();
    }

    /**
     * @return
     */
    public synchronized JsonMapper back() {
        if (hasBack()) {
            Object point = this.point.remove(0);
            this.location.remove(location.size() - 1);
            if (point instanceof Map) this.map((Map<String, Object>) point);
            else if (point instanceof List) this.list((List<Object>) point);
            return this;
        }
        return null;
    }


    /**
     * Back multiples point
     *
     * @param point
     * @return
     */
    public synchronized JsonMapper back(int point) {
        if (point < 1 || point > this.point.size()) return null;
        while (point > 0) {
            this.back();
            point--;
        }
        return this;
    }

    /**
     * Back point at specified start point
     *
     * @param startPoint
     * @return
     */
    public synchronized JsonMapper backAt(int startPoint) {
        if (startPoint < 0 || startPoint > this.point.size()) return null;
        while (getCheckPoint() > startPoint && hasBack()) {
            this.back();
        }
        return this;
    }


    /**
     * Back to backToRoot rootMap
     *
     * @return
     */
    public synchronized JsonMapper backToRoot() {
        if (!this.isInRoot()) {
            this.map = this.rootAsObject();
            this.list = this.rootAsList();
            this.point.clear();
        }
        return this;
    }


    /**
     * @return
     */
    public synchronized Map<String, Object> asObject() {
        return this.map;
    }

    /**
     * @return
     */
    public synchronized List<Object> asList() {
        return this.list;
    }


    /**
     * Create dependence and last as value
     *
     * @param nodesLastValue
     * @return
     */
    public synchronized JsonMapper create(Object... nodesLastValue) {

        if (nodesLastValue == null || nodesLastValue.length < 2) return null;

        List<Object> asList = this.asList(nodesLastValue);
        List<Object> canEnter = new LinkedList<>();

        int lastCan = 0;
        Object value = asList.remove(asList.size() - 1);
        Object key = asList.remove(asList.size() - 1);
        int startPoint = this.getCheckPoint();

        if (key == null) return null;
        else if (!((key instanceof String) || (key instanceof Integer))) return null;

        if (!asList.isEmpty()) {
            boolean last;
            do
                canEnter.add(asList.get(lastCan++));
            while ((last = canEnter(canEnter.toArray())) && lastCan < asList.size());
            if (!last) {
                canEnter.remove(canEnter.size() - 1);
                lastCan--;
            }
        }

        if (canEnter.size() < asList.size()) {
            this.enter(canEnter);
            canEnter.clear();
            for (lastCan = lastCan; lastCan < asList.size(); lastCan++) {
                canEnter.add(asList.get(lastCan));
            }
            canEnter.add(key);

            if (!canCreate(canEnter)) {
                backAt(startPoint);
                return null;
            } else if (key instanceof Integer && (Integer) key != 0) return null;
            else create(canEnter, isInMap() ? this.map : isInList() ? this.list : null);
        }

        backAt(startPoint);
        this.enter(asList);

        if ((key instanceof String && !isInMap())
                || (key instanceof Integer && !isInList())
                || (key instanceof Integer && ((Integer) key < 0 || (Integer) key > list.size()))
                ) {
            backAt(startPoint);
            return null;
        }

        if (key instanceof String && isInMap())
            this.map.put((String) key, value);
        else if (key instanceof Integer && isInList() && (Integer) key < list.size())
            this.list.set((Integer) key, value);
        else if (key instanceof Integer && isInList() && (Integer) key == list.size())
            list.add(value);
        else throw new ZudamueException("Algo deu erado ao adicionar");

        this.backAt(startPoint);
        return this;
    }


    public synchronized JsonMapper removeSingle(Object node) {
        if (isInMap() && node instanceof String) {
            map.remove(node);
            return this;
        } else if (isInList() && node instanceof Integer) {
            int index = (Integer) node;
            this.list.remove(index);
            return this;
        }
        return null;
    }


    private synchronized boolean canCreate(List<Object> nodes) {
        for (Object node : nodes) {
            if (node == null) return false;
            else if (node instanceof String) continue;
            else if (node instanceof Integer && (Integer) node == 0) continue;
            else return false;
        }
        return true;
    }

    private synchronized boolean create(List<Object> nodes, Object startContainer) {
        Object node = nodes.remove(0);
        Object nextNode = (nodes.size() > 0) ? nodes.get(0) : null;
        Map<String, Object> map = startContainer instanceof Map ? (Map<String, Object>) startContainer : null;
        List<Object> list = startContainer instanceof LinkedList ? (List<Object>) startContainer : null;


        if (nextNode != null) {
            if (node instanceof String && nextNode instanceof String && map != null)
                map.put((String) node, startContainer = new LinkedHashMap<String, Object>());
            else if (node instanceof String && nextNode instanceof Integer && map != null)
                map.put((String) node, startContainer = new LinkedList<Object>());
            else if (node instanceof Integer && nextNode instanceof String && list != null)
                list.add(startContainer = new LinkedHashMap<String, Object>());
            else if (node instanceof Integer && nextNode instanceof Integer && list != null)
                list.add(startContainer = new LinkedList<Object>());
            else throw new ZudamueException("Algo deu erado !");
            return create(nodes, startContainer);
        }
        return true;
    }

    public synchronized JsonMapper set(String key, Object value) {
        this.map.put(key, value);
        return this;
    }


    public JsonMapper set(int index, Object value) {
        if (!isInList()) return null;
        if (index < 0 || index > list.size() - 1) return null;
        this.list.set(index, value);
        return this;
    }

    public synchronized JsonMapper add(int index, Object value) {
        if (!isInList()) return null;
        if (index < 0 || index > list.size()) return null;
        this.list.add(index, value);
        return this;
    }

    public synchronized JsonMapper add(Object value) {
        if (!isInList()) return null;
        return this.add(this.list.size(), value);
    }


    /**
     * @param pairs
     * @return
     */
    public synchronized JsonMapper putPairsValues(Object... pairs) {
        //Quando os valores nao for par
        if (!isInMap()) return null;
        if (pairs.length % 2 != 0) return null;
        Object key;
        Object value;
        for (int i = 0; i < pairs.length; i = i + 2) {
            key = pairs[i];
            value = pairs[i + 1];
            if (key == null || !(key instanceof CharSequence)) return null;
            this.set(String.valueOf(key), value);
        }
        return this;
    }

    public synchronized JsonMapper addPairsValues(Object... pairs) {
        //Quando os valores nao for par
        if (isInList()) {
            throw new ZudamueException("Current location in list");
        }

        if (pairs.length % 2 != 0) {
            throw new ZudamueException("values no pair");
        }

        Object key;
        Object value;
        Map<String, Object> pair = new LinkedHashMap<String, Object>();
        for (int i = 0; i < pairs.length; i = i + 2) {
            key = pairs[i];
            value = pairs[i + 1];
            if (key == null || !(key instanceof CharSequence)) {
                throw new ZudamueException("Invalid key");
            }
            pair.put(String.valueOf(key), value);
        }
        this.add(pair);
        return this;
    }


    public synchronized JsonMapper createNode(Object... nodes) {
        List<Object> asList = this.asList(nodes);
        asList.add(new LinkedHashMap<String, Object>());
        this.create(asList.toArray());
        return this.enter(nodes);
    }


    public synchronized JsonMapper createList(Object... nodes) {
        List<Object> asList = this.asList(nodes);
        asList.add(new LinkedList<Object>());
        this.create(asList.toArray());
        return this.enter(nodes);
    }

    /**
     * @return
     */
    public synchronized String rootAsJson() {
        GsonBuilder builder = new GsonBuilder()
                .setLenient();
        Gson gson = builder.create();
        return gson.toJson(this.getRoot());
    }

    public synchronized JSONObject rootAsJSONObject() {
        try {
            return new JSONObject(this.rootAsJson());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized String toJsonOfCurrentLocation() {
        GsonBuilder builder = new GsonBuilder()
                .setLenient();
        Gson gson = builder.create();
        if (this.isInMap()) return gson.toJson(this.map);
        if (this.isInList()) return gson.toJson(this.list);
        return gson.toJson(this.root);
    }

    @Override
    public synchronized String toString() {
        if (this.isInMap())
            return this.map.toString();
        else if (this.isInList())
            return this.list.toString();
        else return null;
    }

    /**
     * @return
     */
    public synchronized int countElement() {
        if (this.isInMap())
            return this.map.size();
        else if (this.isInList())
            return this.list.size();
        else return -1;
    }

    //@Override
    public synchronized Iterator<Map.Entry<String, Object>> iterator() {
        return map.entrySet().iterator();
    }

    public synchronized Object getRoot() {
        return this.root;
    }

    /**
     * Get backToRoot as map if backToRoot as one map
     *
     * @return
     */
    public synchronized Map<String, Object> rootAsObject() {
        return (this.root != null && root instanceof Map) ? (Map<String, Object>) root : null;
    }

    /**
     * Get backToRoot as list if roos as one list
     *
     * @return
     */
    public synchronized List<Object> rootAsList() {
        return (this.root != null && root instanceof List) ? (List<Object>) root : null;
    }


    /**
     * get the current location
     *
     * @return
     */
    public synchronized Object[] getLocation() {
        return this.location.toArray();
    }

    /**
     * Get the current point entred
     *
     * @return
     */
    public synchronized int getCheckPoint() {
        return this.point.size();
    }


    public synchronized boolean isRootMap() {
        return this.root instanceof Map;
    }

    public synchronized boolean isRootList() {
        return this.root instanceof List;
    }

    public synchronized boolean isInRoot() {
        return this.point.isEmpty();
    }

    public synchronized boolean isInMap() {
        return this.map != null;
    }

    public synchronized boolean isInList() {
        return this.list != null;
    }

    public synchronized Object build() {
        backToRoot();
        return this.root;
    }

    public synchronized void forEach(Consumer consumer) {
        if (this.isInMap()) this.forEachMap(consumer);
        else if (this.isInList()) this.forEachList(consumer);
    }

    private synchronized void forEachMap(Consumer consumer) {
        for (Map.Entry<String, Object> entry : this.map.entrySet()) {
            consumer.accept(this, entry.getKey(), -1, entry.getValue());
        }
    }

    private synchronized void forEachList(Consumer consumer) {
        int iCount = 0;
        for (Object value : this.list) {
            consumer.accept(this, null, iCount++, value);
        }
    }

    public synchronized boolean isEmpty() {
        return this.countElement() == 0;
    }


    public interface Consumer {
        void accept(JsonMapper mapperPoint, String key, int index, Object value);
    }
}
