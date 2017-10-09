package org.keithkim.stixdb.core;

import org.junit.Test;
import org.keithkim.stixdb.core.util.ArraysList;
import org.keithkim.stixdb.core.util.RowValues;

import static org.assertj.core.api.Assertions.assertThat;
import static org.keithkim.stixdb.core.util.Values.values;

public class ArraysListTest {
    @Test
    public void sameValues_haveSameHashCode_andEquals() {
        ArraysList arraysList1 = ArraysList.of();
        ArraysList arraysList2 = ArraysList.of();

        assertThat(arraysList1.hashCode()).describedAs("hashCode").isEqualTo(arraysList2.hashCode());
        assertThat(arraysList1.equals(arraysList2)).describedAs("equals").isTrue();

        arraysList1 = ArraysList.of(null, "a", 123456789.0);
        arraysList2 = ArraysList.of(null, new StringBuilder().append('a').toString(), 123456000 + 789.0);

        assertThat(arraysList1.hashCode()).describedAs("hashCode").isEqualTo(arraysList2.hashCode());
        assertThat(arraysList1.equals(arraysList2)).describedAs("equals").isTrue();
    }

    @Test
    public void differentValues_haveDifferentHashCode_andNotEqual() {
        ArraysList arraysList1 = ArraysList.of(null, null);
        ArraysList arraysList2 = ArraysList.of(null);

        assertThat(arraysList1.hashCode()).describedAs("hashCode").isNotEqualTo(arraysList2.hashCode());
        assertThat(arraysList1.equals(arraysList2)).describedAs("equals").isFalse();

        arraysList1 = ArraysList.of("a", 123456789f);
        arraysList2 = ArraysList.of(new StringBuilder().append('a').toString(), 123456000 + 789.0);

        assertThat(arraysList1.hashCode()).describedAs("hashCode").isNotEqualTo(arraysList2.hashCode());
        assertThat(arraysList1.equals(arraysList2)).describedAs("equals").isFalse();
    }

    @Test
    public void eachType_canRenderToString() {
        ArraysList arraysList = ArraysList.of();
        assertThat(arraysList.toString()).isEqualTo("[]");

        arraysList = ArraysList.of(null);
        assertThat(arraysList.toString()).isEqualTo("[null]");

        arraysList = ArraysList.of(false);
        assertThat(arraysList.toString()).isEqualTo("[false]");

        arraysList = ArraysList.of(true);
        assertThat(arraysList.toString()).isEqualTo("[true]");

        arraysList = ArraysList.of(Byte.MAX_VALUE);
        assertThat(arraysList.toString()).isEqualTo("[127]");

        arraysList = ArraysList.of(Short.MAX_VALUE);
        assertThat(arraysList.toString()).isEqualTo("[32767]");

        arraysList = ArraysList.of(Integer.MAX_VALUE);
        assertThat(arraysList.toString()).isEqualTo("[2147483647]");

        arraysList = ArraysList.of(Long.MAX_VALUE);
        assertThat(arraysList.toString()).isEqualTo("[9223372036854775807]");

        arraysList = ArraysList.of(0f);
        assertThat(arraysList.toString()).isEqualTo("[0.0]");

        arraysList = ArraysList.of(0d);
        assertThat(arraysList.toString()).isEqualTo("[0.0]");

        arraysList = ArraysList.of('a');
        assertThat(arraysList.toString()).isEqualTo("['a']");

        arraysList = ArraysList.of("");
        assertThat(arraysList.toString()).isEqualTo("[\"\"]");

        arraysList = ArraysList.of("str");
        assertThat(arraysList.toString()).isEqualTo("[\"str\"]");
    }
}