package org.keithkim.stixdb.core;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Date;

public class Value {
    protected final Object value;

    public static Value value(String value) {
        return new Value(value);
    }

    public static Value value(Character value) {
        return new Value(value);
    }

    public static Value value(Number value) {
        return new Value(value);
    }

    public static Value value(Boolean value) {
        return new Value(value);
    }

    public static Value value(Date value) {
        return new Value(value);
    }

    public static Value value(Temporal value) {
        return new Value(value);
    }

    public static Value value(TemporalAmount value) {
        return new Value(value);
    }

    protected Value(Object value) {
        this.value = value;
    }
}
