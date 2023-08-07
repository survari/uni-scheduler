package org.stundenplan;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ScheduleBuilder schedules = new ScheduleBuilder(args[args.length-1]);
        ArrayList<Schedule> possible_schedules = schedules.generatePossibleSchedules(10);

        System.out.println("Checked "+schedules.collision_counter +" collisions and found "+schedules.generated_schedules+" possible schedules...\n");

        if (possible_schedules.size() == 0) {
            for (String key : schedules.collisions.keySet()) {
                System.out.println("Collision ("+schedules.collisions.get(key)+") times: \n    - "+String.join("\n    - ", key.split("///"))+"\n");
            }
        }

        for (Schedule s : possible_schedules)  {
            s.print();
            s.getBadness();
        }
    }
}