package org.stundenplan;

public class Course {
    private String name;
    private String location;
    private String day;
    private int hour_from;
    private int hour_to;
    private String type;

    public Course(String type, String name, String day, String from, String to, String location) {
        this.name = name;
        this.location = location;
        this.hour_from = Integer.parseInt(from);
        this.hour_to = Integer.parseInt(to);
        this.day = day;
        this.type = type;
    }

    public int getHourFrom() {
        return this.hour_from;
    }

    public int getHourTo() {
        return this.hour_to;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
