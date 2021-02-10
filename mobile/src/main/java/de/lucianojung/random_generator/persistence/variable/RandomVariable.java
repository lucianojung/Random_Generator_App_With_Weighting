package de.lucianojung.random_generator.persistence.variable;


import androidx.room.Entity;
import androidx.room.ForeignKey;

import de.lucianojung.random_generator.persistence.DatabaseEntity;
import de.lucianojung.random_generator.persistence.generator.RandomGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static androidx.room.ForeignKey.CASCADE;

@SuppressWarnings("serial")
@Getter
@ToString
@Entity(tableName = "RandomVariable", foreignKeys = {
        @ForeignKey(entity = RandomGenerator.class, parentColumns = "id",
                childColumns = "gid", onDelete = CASCADE)})
public class RandomVariable extends DatabaseEntity{

    private final long gid;
    private String value; //make it object for different values
    private int weighting;

    @Builder
    public RandomVariable(long id, long gid, String value, int weighting) {
        super(id);
        this.gid = gid;
        this.value = value;
        this.weighting = weighting;
    }

    public long getVid() {
        return super.getId();
    }

    public long getGid() {
        return gid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getWeighting() {
        return weighting;
    }

    public void setWeighting(int weighting) {
        this.weighting = weighting;
    }
}
