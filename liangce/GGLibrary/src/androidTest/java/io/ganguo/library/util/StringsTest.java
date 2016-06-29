package io.ganguo.library.util;

import java.util.List;

import io.ganguo.library.ApplicationTest;
import io.ganguo.library.R;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by Tony on 9/30/15.
 */
public class StringsTest extends ApplicationTest {

    private Logger logger = LoggerFactory.getLogger(StringsTest.class);

    public void testEmpty() throws Exception {
        assertTrue(Strings.isEmpty(null, ""));
        assertTrue(Strings.isNotEmpty("a"));
    }

    public void testEquals() throws Exception {
        assertTrue(Strings.isEquals("a", "a"));
        assertFalse(Strings.isEquals("a", "b"));
        assertTrue(Strings.isEqualsIgnoreCase("a", "A"));
        assertFalse(Strings.isEqualsIgnoreCase("a", "b"));
    }

    public void testFormat() throws Exception {
        String pattern = "{0} is {1}";
        String result = Strings.format(pattern, "apple", "fruit");

        assertEquals(result, "apple is fruit");
    }

    public void testRandomUuId() {
        logger.i("testRandomUuId: " + Strings.randomUUID());
    }

    public void testAssets() {
        String str = Strings.getStringFromAssets(getContext(), "pro_gg.txt");
        assertEquals("this is line 0.this is line 1.", str);

        List<String> arr = Strings.getStringListFromAssets(getContext(), "pro_gg.txt");
        assertEquals("this is line 0.", arr.get(0));
        assertEquals("this is line 1.", arr.get(1));
    }

    public void testRaw() {
        String str = Strings.getStringFromRaw(getContext(), R.raw.pro_gg);
        assertEquals("this is line 3.this is line 4.", str);

        List<String> arr = Strings.getStringListFromRaw(getContext(), R.raw.pro_gg);
        assertEquals("this is line 3.", arr.get(0));
        assertEquals("this is line 4.", arr.get(1));
    }
}
