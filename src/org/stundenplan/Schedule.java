package org.stundenplan;

import com.jakewharton.fliptables.FlipTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Schedule {
    private ArrayList<Course>[] week;
    private Course collision_a;
    private Course collision_b;
    private Map map;
    private boolean has_collisions = false;

    public Schedule(Map m) {
        this.map = m;
        this.week = new ArrayList[7];

        for (int i = 0; i < this.week.length; i++) {
            this.week[i] = new ArrayList<>();
        }
    }

    public Schedule(Schedule seed) {
        this.week = new ArrayList[seed.week.length];

        for (int i = 0; i < this.week.length; i++) {
            this.week[i] = new ArrayList<>();

            for (Course c : seed.week[i]) {
                this.week[i].add(c);
            }
        }

        this.has_collisions = seed.has_collisions;
        this.collision_b = seed.collision_b;
        this.collision_a = seed.collision_a;
        this.map = seed.map;
    }

    private boolean coursesOverlap(Course a, Course b) {
        // given: there is always enough time between courses to get from A to B
        // (that's actually true for Uni-HRO, at least for my branch of study)

        // they overlap because they are at the same time...
        // https://stackoverflow.com/questions/325933/determine-whether-two-date-ranges-overlap/325964#325964
        // doesn't allow meeting edges: if (a.getHourFrom() <= b.getHourTo() && b.getHourFrom() <= a.getHourTo()) {
        if (a.getHourFrom() < b.getHourTo() && b.getHourFrom() < a.getHourTo()) {
            return true;
        }

        // collision
        return false;
    }

    public void insertCourse(Course c) {
        ArrayList<Course> day = this.week[c.getDay()];

        for (Course possible_collision : day) {
            if (coursesOverlap(c, possible_collision)) {
                this.collision_a = c;
                this.collision_b = possible_collision;
                this.has_collisions = true;
                return;
            }
        }

        day.add(c);

        Collections.sort(day, new Comparator<Course>() {
            public int compare(Course c1, Course c2) {
                if (c2.getHourFrom() > c1.getHourFrom()) {
                    return +1;
                }

                if (c2.getHourFrom() < c1.getHourFrom()) {
                    return -1;
                }

                return 0;
            }
        });
    }

    public int getBadness() {
        // badness = sum of all minutes of travel between each course
        //           + 10*(number of courses starting before 9am)
        //           + 10*(number of courses starting after 17am)
        int badness = 0;

        for (int day = 0; day < this.week.length; day++) {
            ArrayList<Course> courses = this.week[day];

            for (int i = 0; i < courses.size(); i++) {
                if (courses.get(i).getHourFrom() < 9 || courses.get(i).getHourFrom() > 17) {
                    badness += 10;
                }
            }

            for (int i = 0; i+1 < courses.size(); i++) {
                badness += this.map.getWeight(courses.get(i).getLocation(),
                        courses.get(i+1).getLocation());
            }
        }

        return badness;
    }

    public boolean collides() {
        return this.has_collisions;
    }

    public Course getCollisionA() {
        return collision_a;
    }

    public Course getCollisionB() {
        return collision_b;
    }

    public void print() {
        int min_hour = 24;
        int max_hour = 0;

        for (int i = 0; i < this.week.length; i++) {
            for (Course c : this.week[i]) {
                if (min_hour > c.getHourFrom()) {
                    min_hour = c.getHourFrom();
                }

                if (max_hour < c.getHourTo()) {
                    max_hour = c.getHourTo();
                }
            }
        }

        ArrayList<String[]> table = new ArrayList<>();
        System.out.println("HOURS: "+min_hour+"-"+max_hour+", BADNESS="+this.getBadness());

        for (int hour = min_hour; hour < max_hour; hour++) {
            table.add(new String[] { "T", "", "", "", "", "", "", "" });
        }

        int taken = 0;

        for (int hour = min_hour; hour < max_hour; hour++) {
            for (int day = 0; day < 7; day++) {
                table.get(hour - min_hour)[0] = hour+"";

                for (Course c : this.week[day]) {
                    if (c.getHourFrom() == hour) {
                        table.get(hour - min_hour)[day+1] = c.getFormattedName() + "\n(" + c.getLocation() + ")";

                        for (int i = 1; i < c.getHourTo() - c.getHourFrom(); i++) {
                            table.get(hour - min_hour + i)[day+1] = "---";
                        }
                    }
                }
            }
        }

        System.out.println(FlipTable.of(new String[] { "T", "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So" },
                table.toArray(String[][]::new)));
    }

    public ArrayList<Course> getCoursesByObligation(String obligationType) {
        ArrayList<Course> list = new ArrayList<>();

        for (Course c : this.getCourses()) {
            if (c.getType().equals(obligationType)) {
                list.add(c);
            }
        }

        return list;
    }

    public ArrayList<Course> getCourses() {
        ArrayList<Course> list = new ArrayList<>();

        for (int i = 0; i < this.week.length; i++) {
            for (Course c : this.week[i]) {
                list.add(c);
            }
        }

        return list;
    }
}
