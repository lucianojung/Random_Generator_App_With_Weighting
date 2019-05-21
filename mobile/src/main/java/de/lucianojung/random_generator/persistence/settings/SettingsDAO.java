package de.lucianojung.random_generator.persistence.settings;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import de.lucianojung.random_generator.persistence.variable.RandomVariable;

@Dao
public interface SettingsDAO {

    @Query("SELECT * FROM Settings WHERE id = :settingID")
    List<RandomVariable> getAllRandomVariablesByGID(long settingID);

    @Query("SELECT * FROM Settings WHERE id = :settingID")
    List<RandomVariable> loadById(long settingID);

    @Insert
    void insert(Setting setting);

    @Insert
    void insertAll(List<Setting> setting);

    @Delete
    void delete(Setting setting);

    @Update
    void update(Setting setting);

    @Query("DELETE FROM Setting")
    void deleteAll();

}
