package com.example.ttlts.service.Iservice;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public interface IBaseRedisService {
    void set(String key, String value);

    void setTimeToLive(String key, long timeToLive);

    void hashSet(String key, String field, Objects value);

    boolean hashExists(String key, String field);

    Object get(String key);

    public Map<String, Object> getField(String key);

    Object hashGet(String key, String field);

    List<Object> hashGetByFieldPrefix(String key, String fieldPrefix);

    Set<String> getFieldPerfix(String key);

    void delete(String key);

    void delete(String key, String field);

    void delete(String key, List<String> fields);
}
