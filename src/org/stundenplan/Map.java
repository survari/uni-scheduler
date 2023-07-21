package org.stundenplan;

import java.util.HashMap;

public class Map {
    private HashMap<String, Integer> locations;
    private HashMap<String, Integer> weightMap;

    public Map(HashMap<Pair<String>, Integer> weights) {
        this.locations = new HashMap<>();
        this.weightMap = new HashMap<>();
        int loc_counter = 0;

        for (Pair<String> p : weights.keySet()) {
            // generate ID for every location
            for (String loc_name : new String[] { p.getFirst(), p.getSecond() }) {
                if (!this.locations.containsKey(loc_name)) {
                    this.locations.put(loc_name, loc_counter);
                    loc_counter++;
                }
            }

            weightMap.put(new Pair<Integer>(
                    this.getLocationFromName(p.getFirst()),
                    this.getLocationFromName(p.getSecond())).toString(),
                weights.get(p));
        }
    }

    public int getLocationFromName(String name) {
        return locations.get(name);
    }

    public int getWeight(String locationA, String locationB) {
        String a = new Pair<Integer>(
                this.getLocationFromName(locationA),
                this.getLocationFromName(locationB)).toString();

        String b = new Pair<Integer>(
                this.getLocationFromName(locationB),
                this.getLocationFromName(locationA)).toString();

        if (weightMap.containsKey(a)) {
            return weightMap.get(a);

        } else if (weightMap.containsKey(b)) {
            return weightMap.get(b);
        }

        return 0;
    }

    public HashMap<String, Integer> getLocations() {
        return this.locations;
    }
}
