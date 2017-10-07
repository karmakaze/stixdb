package org.keithkim.stixdb.core;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RowTest {
    @Test
    public void sameValues_haveSameHashCode_andEquals() {
        Row row1 = new Row();
        Row row2 = new Row();

        assertThat(row1.hashCode()).describedAs("hashCode").isEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isTrue();

        row1 = new Row(null, "a", 123456789.0);
        row2 = new Row(null, new StringBuilder().append('a').toString(), 123456000 + 789.0);

        assertThat(row1.hashCode()).describedAs("hashCode").isEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isTrue();
    }

    @Test
    public void differentValues_haveDifferentHashCode_andNotEqual() {
        Row row1 = new Row(null, null);
        Row row2 = new Row(null);

        assertThat(row1.hashCode()).describedAs("hashCode").isNotEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isFalse();

        row1 = new Row("a", 123456789f);
        row2 = new Row(new StringBuilder().append('a').toString(), 123456000 + 789.0);

        assertThat(row1.hashCode()).describedAs("hashCode").isNotEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isFalse();
    }

    @Test
    public void eachType_canRenderToString() {
        Row row = new Row();
        assertThat(row.toString()).isEqualTo("");

        row = new Row(null);
        assertThat(row.toString()).isEqualTo("null");

        row = new Row(false);
        assertThat(row.toString()).isEqualTo("false");

        row = new Row(true);
        assertThat(row.toString()).isEqualTo("true");

        row = new Row(Byte.MAX_VALUE);
        assertThat(row.toString()).isEqualTo("127");

        row = new Row(Short.MAX_VALUE);
        assertThat(row.toString()).isEqualTo("32767");

        row = new Row(Integer.MAX_VALUE);
        assertThat(row.toString()).isEqualTo("2147483647");

        row = new Row(Long.MAX_VALUE);
        assertThat(row.toString()).isEqualTo("9223372036854775807");

        row = new Row(0f);
        assertThat(row.toString()).isEqualTo("0.0");

        row = new Row(0d);
        assertThat(row.toString()).isEqualTo("0.0");

        row = new Row('a');
        assertThat(row.toString()).isEqualTo("'a'");

        row = new Row("");
        assertThat(row.toString()).isEqualTo("\"\"");

        row = new Row("str");
        assertThat(row.toString()).isEqualTo("\"str\"");
    }
}