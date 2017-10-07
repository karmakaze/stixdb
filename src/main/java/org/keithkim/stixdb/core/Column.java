package org.keithkim.stixdb.core;

import java.util.ArrayList;
import java.util.List;

public class Column {

    public static Name name(String name) {
        return new Column.Name(name);
    }

    public static List<Name> names(String... names) {
        List<Name> columnNames = new ArrayList<>(names.length);
        for (String name : names) {
            columnNames.add(new Name(name));
        }
        return columnNames;
    }

    public static class Name extends org.keithkim.stixdb.core.Name {
        public Name(String name) {
            super(name);
        }
    }
}
