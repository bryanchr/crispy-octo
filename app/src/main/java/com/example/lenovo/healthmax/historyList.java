package com.example.lenovo.healthmax;

public class historyList {

    private String head;
    private String desc;

    //constructor initializing values
    public historyList(String head, String desc) {
        this.head = head;
        this.desc = desc;
    }

    //getters
    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }
}

