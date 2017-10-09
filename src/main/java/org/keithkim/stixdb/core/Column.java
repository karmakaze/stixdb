package org.keithkim.stixdb.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public static class NameOrValue {
        private final Name name;
        private final Object value;

        public static NameOrValue ofName(Name value) {
            return new NameOrValue(value, null);
        }

        public static NameOrValue ofValue(Object value) {
            return new NameOrValue(null, value);
        }

        protected NameOrValue(Name name, Object value) {
            this.name = name;
            this.value = value;
        }

        public boolean isName() {
            return name != null;
        }

        public boolean isValue() {
            return name == null;
        }

        public Name name() {
            return name;
        }

        public Object value() {
            return value;
        }

        @Override
        public int hashCode() {
            if (isName()) {
                return name.hashCode();
            } else {
                return value.hashCode();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof NameOrValue)) {
                return false;
            }
            NameOrValue that = (NameOrValue) o;
            if (this.isName()) {
                if (that.isName()) {
                    return name.equals(that.name);
                } else {
                    return false;
                }
            } else {
                if (that.isName()) {
                    return false;
                } else {
                    return Objects.equals(value, that.value);
                }
            }
        }

        @Override
        public String toString() {
            return isName() ? name.toString() : Objects.toString(value);
        }
    }
}
