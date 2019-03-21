package de.lucianojung.random_chooser;

import java.util.ArrayList;

class Chooser {
    private String name;
    private ArrayList<ChooserValue> valueList;
    //add Enum for ValueType

    public Chooser(String name) {
        this.name = name;
        valueList = new ArrayList<ChooserValue>();
    }

    //getter and setter//

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
