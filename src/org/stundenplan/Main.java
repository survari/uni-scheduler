package org.stundenplan;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ScheduleBuilder schedules = new ScheduleBuilder(args[args.length-1]);
        ArrayList<Schedule> possible_schedules = schedules.generatePossibleSchedules(10);

        for (Schedule s : possible_schedules)  {
            s.print();
            s.getBadness();
        }
    }
}