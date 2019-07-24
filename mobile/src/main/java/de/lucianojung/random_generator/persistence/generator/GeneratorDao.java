package de.lucianojung.random_generator.persistence.generator;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import de.lucianojung.random_generator.persistence.BaseDao;

@Dao
public abstract class GeneratorDao implements BaseDao<Generator> {

    @Query("SELECT * FROM Generator")
    public abstract LiveData<List<Generator>> getAllRandomGenerators();

    @Query("SELECT * FROM Generator WHERE id = :randomGeneratorID")
    public abstract LiveData<List<Generator>> loadById(long randomGeneratorID);

    @Query("SELECT * FROM Generator WHERE name LIKE :name")
    public abstract LiveData<Generator> findByName(String name);

    @Insert
    public abstract void insert(Generator generator);

    @Insert
    public abstract void insertAll(List<Generator> generators);

    @Delete
    public abstract void delete(Generator generator);

    @Update
    public abstract void update(Generator generator);

    @Query("DELETE FROM Generator")
    public abstract void deleteAll();
}
