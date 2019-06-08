package ru.javawebinar.topjava.util;

import java.util.List;

public interface CRUDInteface<ID, T> {
    void add(T t);
    void addAll(List<T> t);
    void update(ID id, T t);
    void delete(ID id);
    List<T> findAll();
    T get(ID id);
}
