package de.lucianojung.random_generator;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@SuppressWarnings("serial")
@Entity(tableName = "RandomGenerator")
public class RandomGenerator implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "gid")
    private final long gid;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "value_type")
    private String valueType;
    //todo make ValueType enum

    public RandomGenerator(long gid, String name) {
        this(gid, name, "String");
    }

    @Ignore
    public RandomGenerator(long gid, String name, String valueType) {
        this.gid = gid;
        this.name = name;
        this.valueType = valueType;
    }

    //getter and setter//
    public long getGid() {
        return gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
}
