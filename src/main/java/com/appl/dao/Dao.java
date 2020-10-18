package com.appl.dao;

import java.util.List;

public interface Dao<T> {
    //T findByDate(String date);
    List<T> findAll();
}
