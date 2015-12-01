package pl.merskip.ogamemobile.game;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test narzędzi pomocniczych
 */
public class UtilsTest {

    @Test
    public void testPrettyText() {
        testValue("1",                1);
        testValue("12",              12);
        testValue("123",            123);
        testValue("1 234",         1234);
        testValue("12 345",       12345);
        testValue("123,4k",      123411);
        testValue("1 234k",     1234111);
        testValue("12 345k",   12345111);
        testValue("123,4M",   123411111);
        testValue("1 234M",  1234111111);
    }

    private void testValue(String excepted, int value) {
        // Zamiana twardej spacji na zwykłą
        String text = Utils.toPrettyText(value).replaceAll("\\u00a0", " ");

        Assert.assertEquals(excepted, text);
        System.out.printf("%10d: %7s\n", value, text);
    }

    @Test
    public void testTimeCountdown() {
        testTime("1s", 1);
        testTime("15s", 15);
        testTime("59s", 59);
        testTime("1m 01s", 60 + 1);
        testTime("1m 13s", 60 + 13);
        testTime("1h 1m", 3600 + 61 + 1);
        testTime("1d 1h", 24 * 3600 + 3600 + 61 + 1);
        testTime("1d 1h", 24 * 3600 + 3600 + 61 + 1);
        testTime("31d 12h", 31 * 24 * 3600 + 12 * 3600 + 61 + 1);
    }

    private void testTime(String excepted, int seconds) {
        String time = Utils.timeCountdownConvert(seconds);

        Assert.assertEquals(excepted, time);
        System.out.printf("%d -> %s\n", seconds, time);
    }
}
