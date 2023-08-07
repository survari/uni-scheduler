package org.stundenplan;

import java.util.*;

public class ScheduleBuilder {
    private String filepath;
    private ArrayList<Course> courses;
    private HashMap<String, Integer> obligations;
    private Map map;
    public int generated_schedules = 0;
    public int collision_counter = 0;
    public HashMap<String, Integer> collisions;

    public ScheduleBuilder(String path) {
        this.filepath = path;
        this.courses = new ArrayList<>();
        this.obligations = new HashMap<>();
        this.collisions = new HashMap<>();

        try {
            ArrayList<String[]> lines = CSVParser.parseCSV(path);
            HashMap<Pair<String>, Integer> locationWeights = new HashMap<>();

            for (String[] line : lines) {
                if (line.length == 4 && line[0].equals("LOC")) {
//                    System.out.println("Added connection from "+line[1]+" to "+line[2]+": "+line[3]+" min");
                    Pair<String> loc = new Pair<>(line[1], line[2]);
                    int weight = Integer.parseInt(line[3]);

                    locationWeights.put(loc, weight);

                } else if (line.length == 3 && line[0].equals("CHOOSE")) {
//                    System.out.println("Choose "+line[2]+" from "+line[1]);
                    this.obligations.put(line[1], Integer.parseInt(line[2]));

                } else if (line.length > 0 && line[0].equals("COURSE")) {
//                    System.out.println("New course/course-variant: "+line[2]);
                    // COURSE, type name, tag, from, to, location
                    this.courses.add(new Course(line[1], line[2], line[3], line[4], line[5], line[6]));
                }
            }

            this.map = new Map(locationWeights);

        } catch (Exception ex) {
            System.out.println("Error while parsing CSV.");
            ex.printStackTrace();
        }
    }

    public void addCollision(Course a, Course b) {
        if (a == null || b == null) {
            return;
        }

        String[] arr = new String[] { a.getShortID(), b.getShortID() };
        Arrays.sort(arr);

        String id = arr[0]+"///"+arr[1];

        if (this.collisions.containsKey(id)) {
            this.collisions.put(id, this.collisions.get(id)+1);

        } else {
            this.collisions.put(id, 1);
        }
    }

    public Map getMap() {
        return this.map;
    }

    public ArrayList<Course> getObligationCourses(String type) {
        ArrayList<Course> list = new ArrayList<>();

        for (Course c : this.courses) {
            if (c.getType().equals(type)) {
                list.add(c);
            }
        }

        return list;
    }

    private ArrayList<Schedule> generate(int max_schedules, Schedule seed, ArrayList<Schedule> schedules, int obligation_index, int nth_obligation_encounter) {
        if (obligation_index >= obligations.size()) {
            return schedules;
        }

        String obligation_type = obligations.keySet().toArray(String[]::new)[obligation_index];
        ArrayList<Course> tmp_obligations = this.getObligationCourses(obligation_type);

        // for each obligation create a new schedule B that's equal to seed and add the possible obligation
        for (int i = nth_obligation_encounter; i < tmp_obligations.size(); i++) {
            Course obligation = tmp_obligations.get(i);
            Schedule new_schedule = new Schedule(seed);
//            System.out.println("ADD COURSE -> "+obligation.getShortID());

            for (Course c : new_schedule.getCourses()) {
//                System.out.println("    - already has: "+c.getShortID());
            }

            new_schedule.insertCourse(obligation);

            if (new_schedule.collides()) {
                if (!new_schedule.getCollisionA().getShortID().equals(new_schedule.getCollisionB().getShortID())) {
                    this.collision_counter++;
                    this.addCollision(new_schedule.getCollisionA(), new_schedule.getCollisionB());
//                    System.out.println("  -> \"" + new_schedule.getCollisionA().getShortID() + "\" collides with \"" + new_schedule.getCollisionB().getShortID() + "\"");
                }

                return generate(max_schedules, seed, schedules, obligation_index, i+1);
            }

            // if the generated new schedule B has a smaller badness than the existing schedules,
            // then replace the schedule with the highest badness with the new one
            // Algorithm:
            //      sort schedules by badness (worst is first)
            //      if B.badness < schedules[last]: schedules[last] = 0
            //      sort schedules by badness
            Collections.sort(schedules, (Schedule s1, Schedule s2) -> {
                if (s2.getBadness() > s1.getBadness()) {
                    return +1;
                }

                if (s2.getBadness() < s1.getBadness()) {
                    return -1;
                }

                return 0;
            });

            if (new_schedule.getCoursesByObligation(obligation_type).size() < this.obligations.get(obligation_type)) {
                schedules = generate(max_schedules, new_schedule, schedules, obligation_index, 0);

            } else {
                if (obligation_index+1 >= obligations.size()) {
//                    System.out.println(new_schedule.getBadness());
                    this.generated_schedules++;

                    if (schedules.size() < max_schedules) {
                        schedules.add(new_schedule);
                        System.out.println("Add schedule with "+schedules.get(schedules.size()-1).getBadness());

                    } else if (schedules.get(schedules.size() - 1).getBadness() > new_schedule.getBadness()) {
                        System.out.println("Remove schedule with "+schedules.get(0).getBadness()+" for one with "+new_schedule.getBadness());
                        schedules.remove(0);
                        schedules.add(new_schedule);
                    }
                }

//                System.out.println("NEXT OBLIGATION!");
                schedules = generate(max_schedules, new_schedule, schedules, obligation_index+1, 0);
            }
        }

        return schedules;
    }

    public ArrayList<Schedule> generatePossibleSchedules(int max_schedules) {
        // generate all schedules, but only save the best 10
        ArrayList<Schedule> list = new ArrayList<>();
        Schedule seed = new Schedule(this.map);

        return generate(max_schedules, seed, list, 0, 0);
    }
}
