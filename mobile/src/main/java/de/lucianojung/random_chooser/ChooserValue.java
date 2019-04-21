package de.lucianojung.random_chooser;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChooserValue implements Serializable{

    private long id;
    private long chooserID;
    private String value; //make it object for different values
    private int weighting;

    public ChooserValue(long chooserID, String value, int weighting) {
        this.id = id;
        this.chooserID = chooserID;
        this.value = value;
        this.weighting = weighting;
    }

    //getter and setter//

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChooserID() {
        return chooserID;
    }

    public void setChooserID(long chooserID) {
        this.chooserID = chooserID;
    }
}
