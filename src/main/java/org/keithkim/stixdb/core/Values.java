package org.keithkim.stixdb.core;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Values {
    public static Value[] values(Object... values) {
        if (values == null) {
            values = new Object[] { null };
        }
        Value[] vs = new Value[values.length];

        int i = 0;
        for (Object value : values) {
            Value v = null;
            if (value instanceof String) {
                v = Value.value((String) value);
            } else if (value instanceof Number) {
                v = Value.value((Number) value);
            } else if (value instanceof Character) {
                v = Value.value((Character) value);
            } else if (value instanceof Boolean) {
                v = Value.value((Boolean) value);
            } else if (value instanceof Date) {
                v = Value.value((Date) value);
            } else if (value instanceof Temporal) {
                v = Value.value((Temporal) value);
            } else if (value instanceof TemporalAmount) {
                v = Value.value((TemporalAmount) value);
            } else if (value == null) {
                v = new Value(null);
            } else {
                throw new StixException("Unsupported Value type "+ value.getClass().getCanonicalName());
            }
            vs[i] = v;
            i++;
        }
        return vs;
    }
}
