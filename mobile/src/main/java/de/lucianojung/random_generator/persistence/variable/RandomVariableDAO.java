package de.lucianojung.random_generator.persistence.variable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RandomVariableDAO {

    @Query("SELECT * FROM RandomVariable WHERE gid = :randomVariableGID")
    List<RandomVariable> getAllRandomVariablesByGID(long randomVariableGID);

    @Query("SELECT * FROM RandomVariable WHERE id = :randomVariableID AND gid = :randomVariableGID")
    List<RandomVariable> loadById(long randomVariableID, long randomVariableGID);

    @Insert
    void insert(RandomVariable randomVariable);

    @Insert
    void insertAll(List<RandomVariable> randomVariables);

    @Delete
    void delete(RandomVariable randomVariable);

    @Update
    void update(RandomVariable randomVariable);

    @Query("DELETE FROM RandomVariable")
    void deleteAll();
}
