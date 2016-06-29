package io.ganguo.library.util;

import io.ganguo.library.ApplicationTest;

/**
 * Created by Wilson on 7/12/15.
 */
public class RReflectionsTest extends ApplicationTest {

    public void testString() {
        int sid = RReflections.getString(getContext(), "gg_test_R_reflections");
        String target = getContext().getString(sid);
        assertEquals("this is for r reflections.", target);
    }
}
