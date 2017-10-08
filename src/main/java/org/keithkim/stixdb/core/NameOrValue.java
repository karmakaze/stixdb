package org.keithkim.stixdb.core;

public class NameOrValue {
    private final Column.Name name;
    private final Object value;

    public static NameOrValue ofName(Column.Name value) {
        return new NameOrValue(value, null);
    }

    public static NameOrValue ofValue(Object value) {
        return new NameOrValue(null, value);
    }

    protected NameOrValue(Column.Name name, Object value) {
        this.name = name;
        this.value = value;
    }

    public boolean isName() {
        return name != null;
    }

    public boolean isValue() {
        return name == null;
    }

    public Column.Name name() {
        return name;
    }

    public Object value() {
        return value;
    }
}