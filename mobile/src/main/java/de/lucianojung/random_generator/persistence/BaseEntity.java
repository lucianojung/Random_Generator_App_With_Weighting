package de.lucianojung.random_generator.persistence;

import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import lombok.Getter;

@Getter
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private final long id;

    public BaseEntity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
