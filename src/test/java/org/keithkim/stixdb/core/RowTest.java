package org.keithkim.stixdb.core;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.keithkim.stixdb.core.Values.values;

public class RowTest {
    @Test
    public void sameValues_haveSameHashCode_andEquals() {
        Row row1 = new Row();
        Row row2 = new Row();

        assertThat(row1.hashCode()).describedAs("hashCode").isEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isTrue();

        row1 = new Row(values(null, "a", 123456789.0));
        row2 = new Row(values(null, new StringBuilder().append('a').toString(), 123456000 + 789.0));

        assertThat(row1.hashCode()).describedAs("hashCode").isEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isTrue();
    }

    @Test
    public void differentValues_haveDifferentHashCode_andNotEqual() {
        Row row1 = new Row(values(null, null));
        Row row2 = new Row(values(null));

        assertThat(row1.hashCode()).describedAs("hashCode").isNotEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isFalse();

        row1 = new Row(values("a", 123456789f));
        row2 = new Row(values(new StringBuilder().append('a').toString(), 123456000 + 789.0));

        assertThat(row1.hashCode()).describedAs("hashCode").isNotEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isFalse();
    }

    @Test
    public void eachType_canRenderToString() {
        Row row = new Row();
        assertThat(row.toString()).isEqualTo("");

        row = new Row(values(null));
        assertThat(row.toString()).isEqualTo("null");

        row = new Row(values(false));
        assertThat(row.toString()).isEqualTo("false");

        row = new Row(values(true));
        assertThat(row.toString()).isEqualTo("true");

        row = new Row(values(Byte.MAX_VALUE));
        assertThat(row.toString()).isEqualTo("127");

        row = new Row(values(Short.MAX_VALUE));
        assertThat(row.toString()).isEqualTo("32767");

        row = new Row(values(Integer.MAX_VALUE));
        assertThat(row.toString()).isEqualTo("2147483647");

        row = new Row(values(Long.MAX_VALUE));
        assertThat(row.toString()).isEqualTo("9223372036854775807");

        row = new Row(values(0f));
        assertThat(row.toString()).isEqualTo("0.0");

        row = new Row(values(0d));
        assertThat(row.toString()).isEqualTo("0.0");

        row = new Row(values('a'));
        assertThat(row.toString()).isEqualTo("'a'");

        row = new Row(values(""));
        assertThat(row.toString()).isEqualTo("\"\"");

        row = new Row(values("str"));
        assertThat(row.toString()).isEqualTo("\"str\"");
    }
}