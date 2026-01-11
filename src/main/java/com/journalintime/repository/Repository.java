package com.journalintime.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    T save(T entity);

    T update(T entity);

    void delete(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();
}
