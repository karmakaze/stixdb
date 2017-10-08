package org.keithkim.stixdb.core;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Index<T extends Comparable<T>> {
    private NavigableMap<T, List<Row>> valueRows = new ConcurrentSkipListMap<>();
    private List<Row> nullValuedRows = new ArrayList<>();

    protected Index() {
    }

    public static class Name extends org.keithkim.stixdb.core.Name {
        public Name(String name) {
            super(name);
        }
    }
}