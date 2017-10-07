package org.keithkim.stixdb.core;

public class Name {
    protected final String name;

    public Name(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass()) return false;

        Name that = (Name) o;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 251;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + name + '}';
    }
}
