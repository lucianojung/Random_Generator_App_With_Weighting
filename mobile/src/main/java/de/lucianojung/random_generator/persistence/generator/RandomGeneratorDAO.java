package de.lucianojung.random_generator.persistence.generator;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RandomGeneratorDAO {

    @Query("SELECT * FROM RandomGenerator")
    List<RandomGenerator> getAllRandomGenerators();

    @Query("SELECT * FROM RandomGenerator WHERE id = :randomGeneratorID")
    List<RandomGenerator> loadById(long randomGeneratorID);

    @Query("SELECT * FROM RandomGenerator WHERE name LIKE :name")
    RandomGenerator findByName(String name);

    @Insert
    void insert(RandomGenerator randomGenerator);

    @Insert
    void insertAll(List<RandomGenerator> randomGenerators);

    @Delete
    void delete(RandomGenerator randomGenerator);

    @Update
    void update(RandomGenerator randomGenerator);

    @Query("DELETE FROM RandomGenerator")
    void deleteAll();
}
