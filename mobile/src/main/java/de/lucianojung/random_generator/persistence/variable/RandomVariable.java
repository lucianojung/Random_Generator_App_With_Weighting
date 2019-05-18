package de.lucianojung.random_generator.persistence.variable;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import de.lucianojung.random_generator.persistence.generator.RandomGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@SuppressWarnings("serial")
@Getter @Setter
@Builder
@ToString
@Entity(tableName = "RandomVariable", foreignKeys = {
        @ForeignKey(entity = RandomGenerator.class, parentColumns = "gid",
                childColumns = "gid", onDelete = CASCADE)})
public class RandomVariable implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private final long vid;
    private final long gid;
    private String value; //make it object for different values
    private int weighting;

    public RandomVariable(long vid, long gid, String value, int weighting) {
        this.vid = vid;
        this.gid = gid;
        this.value = value;
        this.weighting = weighting;
    }

    public long getVid() {
        return vid;
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
