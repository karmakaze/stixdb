package org.keithkim.stixdb.core;

import org.keithkim.stixdb.core.util.RowValues;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Index<T extends Comparable<T>> {
    private NavigableMap<T, List<RowValues>> valueRows = new ConcurrentSkipListMap<>();
    private List<RowValues> nullValuedRows = new ArrayList<>();

    protected Index() {
    }

    public static class Name extends org.keithkim.stixdb.core.Name {
        public Name(String name) {
            super(name);
        }
    }
}