package de.lucianojung.random_generator.persistence.variable;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import de.lucianojung.random_generator.persistence.BaseEntity;
import de.lucianojung.random_generator.persistence.generator.Generator;
import lombok.Builder;
import lombok.ToString;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@SuppressWarnings("serial")
@ToString
@Entity(tableName = "Variable", foreignKeys = {
        @ForeignKey(entity = Generator.class, parentColumns = "id",
                childColumns = "gid", onDelete = CASCADE)})
public class Variable extends BaseEntity {

    private final long gid;
    private String value; //make it object for different values
    private int weighting;

    @Builder
    public Variable(long id, long gid, String value, int weighting) {
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
