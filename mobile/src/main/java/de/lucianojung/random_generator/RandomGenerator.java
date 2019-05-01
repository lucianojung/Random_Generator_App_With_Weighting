package de.lucianojung.random_generator;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

@SuppressWarnings("serial")
@Entity(tableName = "RandomGenerator")
public class RandomGenerator implements Serializable, Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "gid")
    private final long gid;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "value_type")
    private String valueType;
    //todo make ValueType enum
//    @ColumnInfo(name = "image")
//    private Image image;

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

    //++++++++++++++++++++++++++++++
    //parcelable Methods for Dialogs
    //++++++++++++++++++++++++++++++

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.valueType);
    }

    protected RandomGenerator(Parcel parcel){
        this.gid = 0;
        this.name = parcel.readString();
        this.valueType = parcel.readString();
    }

    public static final Creator<RandomGenerator> CREATOR = new Creator<RandomGenerator>() {
        @Override
        public RandomGenerator createFromParcel(Parcel source) {
            return new RandomGenerator(source);
        }

        @Override
        public RandomGenerator[] newArray(int size) {
            return new RandomGenerator[size];
        }
    };
}
