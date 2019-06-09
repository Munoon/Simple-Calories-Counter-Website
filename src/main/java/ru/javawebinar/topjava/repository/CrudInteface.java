package ru.javawebinar.topjava.repository;

import java.util.List;

public interface CrudInteface<ID, T> {
    void add(T t);
    void addAll(List<T> t);
    void update(ID id, T t);
    void delete(ID id);
    List<T> findAll();
    T get(ID id);
}
