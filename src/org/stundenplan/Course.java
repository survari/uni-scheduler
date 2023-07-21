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

    public String getType() {
        return type;
    }

    public int getDay() {
        return switch (this.day) {
            case "Mo" -> 0;
            case "Di" -> 1;
            case "Mi" -> 2;
            case "Do" -> 3;
            case "Fr" -> 4;
            case "Sa" -> 5;
            case "So" -> 6;
            default -> 0;
        };
    }
}
