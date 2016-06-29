package io.ganguo.library.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.ApplicationTest;

/**
 * Created by Tony on 9/30/15.
 */
public class ColletionsTest extends ApplicationTest {

    public void testEmpty() throws Exception {
        assertTrue(Collections.isEmpty((Collection) null));
        assertTrue(Collections.isEmpty(new ArrayList()));

        List list = new ArrayList();
        list.add("a");
        list.add("b");
        assertTrue(Collections.isNotEmpty(list));
    }

    public void testMapEmpty() throws Exception {
        assertTrue(Collections.isEmpty((HashMap) null));
        assertTrue(Collections.isEmpty(new HashMap()));

        Map map = new HashMap<>();
        map.put("a", "aa");
        assertTrue(Collections.isNotEmpty(map));
    }

}
