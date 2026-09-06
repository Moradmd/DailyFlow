package com.dailyflow.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Routine implements Serializable {
    public String id;
    public String name;
    public String time;
    public String category;
    public List<Integer> days;
    public boolean done;
    public long date;

    public Routine() {
        this.id = String.valueOf(System.currentTimeMillis());
        this.days = new ArrayList<>();
        this.done = false;
        this.date = System.currentTimeMillis();
    }

    public Routine(String name, String time, String category, List<Integer> days) {
        this();
        this.name = name;
        this.time = time;
        this.category = category;
        this.days = days;
    }
}
