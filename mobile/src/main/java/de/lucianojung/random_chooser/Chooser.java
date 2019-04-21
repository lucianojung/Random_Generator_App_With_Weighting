package de.lucianojung.random_chooser;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
class Chooser implements Serializable{

    private long id;
    private String name;
    private ArrayList<ChooserValue> valueList;
    //add Enum for ValueType

    public Chooser(String name) {
        this.name = name;
        valueList = new ArrayList<>();
    }

    //getter and setter//
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<ChooserValue> getValueList() {
        return valueList;
    }

    public void setValueList(ArrayList<ChooserValue> valueList) {
        this.valueList = valueList;
    }
}
