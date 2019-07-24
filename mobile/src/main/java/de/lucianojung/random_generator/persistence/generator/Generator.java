package de.lucianojung.random_generator.persistence.generator;

import android.arch.persistence.room.Entity;

import de.lucianojung.random_generator.persistence.BaseEntity;
import lombok.Builder;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@Entity(tableName = "Generator")
public class Generator extends BaseEntity {

    private String name;

    @Builder
    public Generator(long id, String name) {
        super(id);
        this.name = name;
    }

    public long getGid() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

