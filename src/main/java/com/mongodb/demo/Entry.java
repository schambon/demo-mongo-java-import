package com.mongodb.demo;

public class Entry {

    int field1;
    int field2;
    int field3;
    String field4;

    public Entry(int field1, int field2, int field3, String field4) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
    }

    public int getField1() {
        return field1;
    }

    public int getField2() {
        return field2;
    }

    public int getField3() {
        return field3;
    }

    public String getField4() {
        return field4;
    }
}
