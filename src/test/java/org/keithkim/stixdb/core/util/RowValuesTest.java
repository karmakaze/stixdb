package org.keithkim.stixdb.core.util;

import org.junit.Test;
import org.keithkim.stixdb.core.util.RowValues;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.keithkim.stixdb.core.util.Values.values;

public class RowValuesTest {
    @Test
    public void select() throws Exception {
        RowValues rowValues1 = RowValues.with(values("a", 1, "x"));
        RowValues rowValues2 = RowValues.with(values("b", 2, "y"));
    }

    @Test
    public void hashCode_shouldReturnHash() throws Exception {
    }

    @Test
    public void equals_shouldReturnBoolean() throws Exception {
    }

    @Test
    public void toString_shouldStringify() throws Exception {
    }

    @Test
    public void concatFullOrdered_shouldConcatWithNoIndices() throws Exception {
        RowValues rowValues1 = RowValues.with(values("a", 1, "x"));
        RowValues rowValues2 = RowValues.with(values("b", 2, "y"));

        RowValues concat = RowValues.concat(rowValues1, rowValues2);

        assertThat(concat.size()).isEqualTo(6);
        assertThat(concat.values).isEqualTo(asList("a", 1, "x", "b", 2, "y"));
        assertThat(concat.indices).isNull();
    }
}