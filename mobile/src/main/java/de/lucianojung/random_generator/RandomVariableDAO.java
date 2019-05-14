package de.lucianojung.random_generator;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RandomVariableDAO {

    @Query("SELECT * FROM RandomVariable WHERE gid = :randomVariableGID")
    List<RandomVariable> getAllRandomVariablesByGID(long randomVariableGID);

    @Query("SELECT * FROM RandomVariable WHERE vid = :randomVariableID AND gid = :randomVariableGID")
    List<RandomVariable> loadById(long randomVariableID, long randomVariableGID);

    @Insert
    void insertAll(RandomVariable... randomVariables);

    @Delete
    void delete(RandomVariable randomVariable);

    @Update
    void update(RandomVariable randomVariable);

    @Query("DELETE FROM RandomVariable")
    void deleteAll();

}
