package org.keithkim.stixdb.core;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<Name, Table> tables = new HashMap<>();

    public static class Name extends org.keithkim.stixdb.core.Name {
        public Name(String name) {
            super(name);
        }
    }
}
