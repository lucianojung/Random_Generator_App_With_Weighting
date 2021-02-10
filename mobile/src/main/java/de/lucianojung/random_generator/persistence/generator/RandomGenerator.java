package de.lucianojung.random_generator.persistence.generator;

import androidx.room.Entity;

import de.lucianojung.random_generator.persistence.DatabaseEntity;
import lombok.Builder;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@Entity(tableName = "RandomGenerator")
public class RandomGenerator extends DatabaseEntity {

    private String name;

    @Builder
    public RandomGenerator(long id, String name) {
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

