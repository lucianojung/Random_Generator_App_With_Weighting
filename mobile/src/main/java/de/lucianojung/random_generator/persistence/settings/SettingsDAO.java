package de.lucianojung.random_generator.persistence.settings;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import de.lucianojung.random_generator.persistence.BaseDao;
import de.lucianojung.random_generator.persistence.variable.Variable;

@Dao
public abstract class SettingsDAO implements BaseDao<Setting> {

    @Query("SELECT * FROM Settings WHERE id = :settingID")
    public abstract List<Variable> getAllRandomVariablesByGID(long settingID);

    @Query("SELECT * FROM Settings WHERE id = :settingID")
    public abstract List<Variable> loadById(long settingID);

    @Insert
    public abstract void insert(Setting setting);

    @Insert
    public abstract void insertAll(List<Setting> setting);

    @Delete
    public abstract void delete(Setting setting);

    @Update
    public abstract void update(Setting setting);

    @Query("DELETE FROM Setting")
    public abstract void deleteAll();

}
