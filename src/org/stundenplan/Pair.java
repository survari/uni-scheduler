package org.stundenplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pair<T> {
    private final T first;
    private final T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() { return first; }
    public T getSecond() { return second; }

    @Override
    public int hashCode() {
        List<String> t = new ArrayList<String>();
        t.add(first.toString());
        t.add(second.toString());
        Arrays.sort(t.toArray());

        return String.join("", t).hashCode();
    }

    @Override
    public String toString() {
        return "(\""+first.toString()+"\",\""+second.toString()+"\")";
    }
}