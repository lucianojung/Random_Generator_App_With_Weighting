package de.lucianojung.random_generator.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BaseDao<T> {

    @Insert
    public void insert(T obj);

    @Insert
    abstract void insertAll(List<T> objects);

    @Delete
    abstract void delete(T obj);

    @Update
    abstract void update(T obj);
}
