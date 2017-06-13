package com.example.vpadnnative;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StorageCenter {

    private static StorageCenter mStorage = new StorageCenter();
    private Map<String, Object> storageMap = Collections.synchronizedMap(new HashMap<String, Object>());

    public static StorageCenter instance() {
        return mStorage;
    }
    public Object get(String key) {
        return storageMap.get(key);
    }

    public void set(String key, Object obj) {
        storageMap.put(key, obj);
    }
}
