package spacemadness.com.lunarconsole.utils;

import java.util.HashMap;
import java.util.Map;

public class DictionaryUtils
{
    public static Map<String, Object> createMap(String key, Object value)
    {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static Map<String, Object> createMap(String key1, Object value1, String key2, Object value2)
    {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }
}
