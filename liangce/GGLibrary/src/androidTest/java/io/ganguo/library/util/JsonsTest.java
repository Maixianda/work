package io.ganguo.library.util;

import org.json.JSONObject;

import java.io.Serializable;

import io.ganguo.library.ApplicationTest;
import io.ganguo.library.R;

/**
 * Created by Wilson on 7/12/15.
 */
public class JsonsTest extends ApplicationTest implements Serializable {

    public void testManualJson() {
        String testJ = getContext().getString(R.string.gg_test_json);

        int code = Jsons.getInt(testJ, "code", -1);
        assertEquals(200, code);

        String message = Jsons.getString(testJ, "message", "");
        assertEquals("request ok", message);

        JSONObject dataObj = Jsons.getJSONObject(testJ, "data", null);
        int id = Jsons.getInt(dataObj, "id", -1);
        assertEquals(999, id);

        String pro_name = Jsons.getString(dataObj, "pro_name", "");
        assertEquals("test pro", pro_name);

        String details = Jsons.getString(dataObj, "details", "");
        assertEquals("Android is good.", details);

        logger.e("testJ:\n" + testJ);

    }

}
