package de.lucianojung.random_generator;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RandomGeneratorDAO {

    @Query("SELECT * FROM RandomGenerator")
    List<RandomGenerator> getAllRandomGenerators();

    @Query("SELECT * FROM RandomGenerator WHERE gid IN (:randomGeneratorIDs)")
    List<RandomGenerator> loadAllByIds(int[] randomGeneratorIDs);

    @Query("SELECT * FROM RandomGenerator WHERE name LIKE :name")
    RandomGenerator findByName(String name);

    @Insert
    void insertAll(RandomGenerator... randomGenerators);

    @Delete
    void delete(RandomGenerator randomGenerator);

}
