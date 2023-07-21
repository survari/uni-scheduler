package org.stundenplan;

public class Main {
    public static void main(String[] args) {
        ScheduleBuilder schedule = new ScheduleBuilder(args[args.length-1]);

        System.out.println(schedule.getMap().getWeight("ABT", "UHG"));
    }
}