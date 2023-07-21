package org.stundenplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Schedule {
    private ArrayList<Course>[] week;
    private Course collision_a;
    private Course collision_b;
    private Map map;
    private boolean has_collisions = false;

    public Schedule(Map m) {
        this.map = map;
        this.week = new ArrayList[7];

        for (int i = 0; i < this.week.length; i++) {
            this.week[i] = new ArrayList<>();
        }
    }

    private boolean coursesOverlap(Course a, Course b) {
        // given: there is always enough time between courses to get from A to B
        // (that's actually true for Uni-HRO, at least for my branch of study)

        // they overlap because they are at the same time...
        // https://stackoverflow.com/questions/325933/determine-whether-two-date-ranges-overlap/325964#325964
        if (a.getHourFrom() <= b.getHourTo() && b.getHourFrom() <= a.getHourTo()) {
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
        //           + 30*(number of courses starting at 7am)
        return 0;
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
}
