package de.lucianojung.random_generator.persistence.variable;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import de.lucianojung.random_generator.persistence.BaseDao;

@Dao
public abstract class VariableDao implements BaseDao<Variable> {

    @Query("SELECT * FROM Variable WHERE gid = :randomVariableGID")
    public abstract List<Variable> getAllRandomVariablesByGID(long randomVariableGID);

    @Query("SELECT * FROM Variable WHERE id = :randomVariableID AND gid = :randomVariableGID")
    public abstract List<Variable> loadById(long randomVariableID, long randomVariableGID);

    @Insert
    public abstract void insert(Variable variable);

    @Insert
    public abstract void insertAll(List<Variable> variables);

    @Delete
    public abstract void delete(Variable variable);

    @Update
    public abstract void update(Variable variable);

    @Query("DELETE FROM Variable")
    public abstract void deleteAll();
}
