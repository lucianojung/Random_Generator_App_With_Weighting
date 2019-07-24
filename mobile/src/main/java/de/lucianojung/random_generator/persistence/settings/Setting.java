package de.lucianojung.random_generator.persistence.settings;

import android.arch.persistence.room.Entity;

import de.lucianojung.random_generator.persistence.BaseEntity;
import lombok.ToString;

@SuppressWarnings("serial")
@ToString
@Entity(tableName = "Settings")
public class Setting extends BaseEntity {

    private String name;
    private boolean activated;

    public Setting(long id, String name, boolean activated) {
        super(id);
        this.name = name;
        this.activated = activated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}