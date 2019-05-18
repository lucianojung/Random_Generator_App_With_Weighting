package de.lucianojung.random_generator.persistence.generator;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@SuppressWarnings("serial")
@Entity(tableName = "RandomGenerator")
public class RandomGenerator implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private final long gid;
    private String name;
//    private String valueType;
    //todo make ValueType enum


    public RandomGenerator(long gid, String name) {
        this.gid = gid;
        this.name = name;
    }

    public long getGid() {
        return gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

