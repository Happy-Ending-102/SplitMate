package com.splitmate.repository;

import java.util.List;

public interface Repository<T> {
    T findById(String id);
    List<T> findAll();
    boolean insert(T entity);
    boolean update(T entity);
    boolean delete(T entity);
}
