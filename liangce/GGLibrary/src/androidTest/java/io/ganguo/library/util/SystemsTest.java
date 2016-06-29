package io.ganguo.library.util;

import io.ganguo.library.ApplicationTest;

/**
 * Created by Wilson on 8/12/15.
 */
public class SystemsTest extends ApplicationTest {

    public void testScreen() {
        boolean isLandscape = Systems.isLandscape(getContext());
        boolean isPortrait = Systems.isPortrait(getContext());
        boolean isTablet = Systems.isTablet(getContext());

        logger.e("isLandscape:" + isLandscape + ", isPortrait:" + isPortrait + ", isTablet:" + isTablet);
    }
}
