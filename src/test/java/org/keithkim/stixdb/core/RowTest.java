package org.keithkim.stixdb.core;

import org.junit.Test;
import org.keithkim.stixdb.core.util.RowValues;

import static org.assertj.core.api.Assertions.assertThat;
import static org.keithkim.stixdb.core.util.Values.values;

public class RowTest {
    @Test
    public void sameValues_haveSameHashCode_andEquals() {
        RowValues row1 = RowValues.with(values());
        RowValues row2 = RowValues.with(values());

        assertThat(row1.hashCode()).describedAs("hashCode").isEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isTrue();

        row1 = RowValues.with(values(null, "a", 123456789.0));
        row2 = RowValues.with(values(null, new StringBuilder().append('a').toString(), 123456000 + 789.0));

        assertThat(row1.hashCode()).describedAs("hashCode").isEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isTrue();
    }

    @Test
    public void differentValues_haveDifferentHashCode_andNotEqual() {
        RowValues row1 = RowValues.with(values(null, null));
        RowValues row2 = RowValues.with(values(null));

        assertThat(row1.hashCode()).describedAs("hashCode").isNotEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isFalse();

        row1 = RowValues.with(values("a", 123456789f));
        row2 = RowValues.with(values(new StringBuilder().append('a').toString(), 123456000 + 789.0));

        assertThat(row1.hashCode()).describedAs("hashCode").isNotEqualTo(row2.hashCode());
        assertThat(row1.equals(row2)).describedAs("equals").isFalse();
    }

    @Test
    public void eachType_canRenderToString() {
        RowValues row = RowValues.with(values());
        assertThat(row.toString()).isEqualTo("");

        row = RowValues.with(values(null));
        assertThat(row.toString()).isEqualTo("null");

        row = RowValues.with(values(false));
        assertThat(row.toString()).isEqualTo("false");

        row = RowValues.with(values(true));
        assertThat(row.toString()).isEqualTo("true");

        row = RowValues.with(values(Byte.MAX_VALUE));
        assertThat(row.toString()).isEqualTo("127");

        row = RowValues.with(values(Short.MAX_VALUE));
        assertThat(row.toString()).isEqualTo("32767");

        row = RowValues.with(values(Integer.MAX_VALUE));
        assertThat(row.toString()).isEqualTo("2147483647");

        row = RowValues.with(values(Long.MAX_VALUE));
        assertThat(row.toString()).isEqualTo("9223372036854775807");

        row = RowValues.with(values(0f));
        assertThat(row.toString()).isEqualTo("0.0");

        row = RowValues.with(values(0d));
        assertThat(row.toString()).isEqualTo("0.0");

        row = RowValues.with(values('a'));
        assertThat(row.toString()).isEqualTo("'a'");

        row = RowValues.with(values(""));
        assertThat(row.toString()).isEqualTo("\"\"");

        row = RowValues.with(values("str"));
        assertThat(row.toString()).isEqualTo("\"str\"");
    }
}