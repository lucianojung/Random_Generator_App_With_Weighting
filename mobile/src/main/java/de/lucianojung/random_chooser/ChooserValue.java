package de.lucianojung.random_chooser;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChooserValue implements Serializable{

    private int value; //make it object for different values
    private int weighting;

    public ChooserValue(int value, int weighting) {
        this.value = value;
        this.weighting = weighting;
    }

    //getter and setter//

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getWeighting() {
        return weighting;
    }

    public void setWeighting(int weighting) {
        this.weighting = weighting;
    }
}
