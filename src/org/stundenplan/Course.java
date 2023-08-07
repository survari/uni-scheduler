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
        switch (this.day) {
            case "Mo": return 0;
            case "Di": return 1;
            case "Mi": return 2;
            case "Do": return 3;
            case "Fr": return 4;
            case "Sa": return 5;
            case "So": return 6;
            default: return 0;
        }
    }

    public String getFormattedName() {
        int last_breakable = 0;
        int last_last_breakable = 0;
        String fname = "";
        String part = "";
        String tmp = "";

        for (int i = 0; i < this.name.length(); i++) {
            char c = this.name.charAt(i);
            tmp += c;

            if (c == ' ' || c == '\t' || c == ',' || c == '-' || c == '.' || c == ':') {
                last_breakable = i;
                part = tmp;
            }

            if (tmp.length() > 25) {
                if (last_breakable > last_last_breakable) {
                    last_last_breakable = last_breakable;
                    i = last_breakable;

                    fname += "\n" + part;

                } else {
                    fname += "\n" + tmp;
                }

                part = "";
                tmp = "";
            }
        }

        if (!tmp.isEmpty()) {
            fname += "\n"+tmp;
        }

        return fname.trim();
    }

    public String getShortID() {
        return this.name+" ("+this.location+"/"+this.day+"/"+this.hour_from+"-"+this.hour_to+")";
    }
}
