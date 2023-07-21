package org.stundenplan;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleBuilder {
    private String filepath;
    private ArrayList<Course> courses;
    private HashMap<String, Integer> obligations;
    private Map map;

    public ScheduleBuilder(String path) {
        this.filepath = path;
        this.courses = new ArrayList<>();
        this.obligations = new HashMap<>();

        try {
            ArrayList<String[]> lines = CSVParser.parseCSV(path);
            HashMap<Pair<String>, Integer> locationWeights = new HashMap<>();

            for (String[] line : lines) {
                if (line.length == 4 && line[0].equals("LOC")) {
                    Pair<String> loc = new Pair<String>(line[1], line[2]);
                    int weight = Integer.parseInt(line[3]);

                    locationWeights.put(loc, weight);

                } else if (line.length == 3 && line[0].equals("CHOOSE")) {
                    this.obligations.put(line[1], Integer.parseInt(line[2]));

                } else if (line.length > 0 && line[0].equals("COURSE")) {
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

    public Map getMap() {
        return this.map;
    }

    private ArrayList<Schedule> generate(Schedule seed, ArrayList<Schedule> schedules, int obligation_index) {
        if (obligation_index >= obligations.size()) {
            return schedules;
        }

        // for each obligation create a new schedule B that's equal to seed and add the possible obligation

        // if the generated new schedule B has a smaller badness than the existing schedules,
        // then replace the schedule with the highest badness with the new one
        // Algorithm:
        //      sort schedules by badness (worst is first)
        //      if B.badness < schedules[0]: schedules[0] = 0
        //      sort schedules by badness

        return generate(seed, schedules, obligation_index+1);
    }

    public ArrayList<Schedule> generatePossibleSchedules() {
        // generate all schedules, but only save the best 10
        ArrayList<Schedule> list = new ArrayList<>();



        return list;
    }
}
