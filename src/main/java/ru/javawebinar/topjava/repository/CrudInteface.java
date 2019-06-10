package ru.javawebinar.topjava.repository;

import java.util.List;

public interface CrudInteface<ID, T> {
    void add(T t);
    void update(T t);
    void delete(ID id);
    List<T> findAll();
    T get(ID id);
}
