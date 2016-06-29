package io.ganguo.library.util;

import io.ganguo.library.ApplicationTest;

/**
 * Created by Wilson on 8/12/15.
 */
public class RegsTest extends ApplicationTest {
    public void testEmail() {
        String text = "tony@ganguo.io";
        String text2 = "tony@ganguo";

        assertTrue(Regs.isEmail(text));
        assertFalse(Regs.isEmail(text2));
    }

    public void testMobile() {
        String text = "13878561400";
        String text2 = "1387856140";

        assertTrue(Regs.isPhone(text));
        assertFalse(Regs.isPhone(text2));
    }

    public void testUrl() {
        String text = "http://baidu.com";
        assertTrue(Regs.isUrl(text));

        String text2 = "baidu.com";
        assertFalse(Regs.isUrl(text2));
    }

    public void testIDCard() {
        assertTrue(Regs.isIDCard("440681199107142299"));//18位
        assertTrue(Regs.isIDCard("44068119910714263X"));//18位带x
        assertTrue(Regs.isIDCard("440681199107142"));//15位

        assertFalse(Regs.isIDCard("4406811991071426X3"));//18位非x结尾
        assertFalse(Regs.isIDCard("44068119910714263"));//17位
        assertFalse(Regs.isIDCard("44068119910714"));//14位
    }

    public void testZipCode() {
        assertTrue(Regs.isZipCode("510000"));

        assertFalse(Regs.isZipCode("5100007"));
        assertFalse(Regs.isZipCode("51009"));
    }

    public void testNumberLetter() {
        assertTrue(Regs.isNumberLetter("hallo123world"));

        assertFalse(Regs.isNumberLetter("hallo123world+"));
        assertFalse(Regs.isNumberLetter("hallo123world*"));
        assertFalse(Regs.isNumberLetter("hallo123world "));
    }

    public void testNumber() {
        assertTrue(Regs.isNumber("123"));

        assertFalse(Regs.isNumber("1.23"));
        assertFalse(Regs.isNumber("123world"));
        assertFalse(Regs.isNumber("123world"));
        assertFalse(Regs.isNumber("123*"));
        assertFalse(Regs.isNumber(" 12"));
    }

    public void testLetter() {
        assertTrue(Regs.isLetter("hallo"));

        assertFalse(Regs.isLetter("hallo1"));
        assertFalse(Regs.isLetter("hallo_"));
        assertFalse(Regs.isLetter(" hallo"));
        assertFalse(Regs.isLetter("hallo."));
    }

    public void testChinese() {
        assertTrue(Regs.isChinese("你好"));
        assertTrue(Regs.isChinese("你好！"));
        assertTrue(Regs.isChinese("你好。"));

        assertFalse(Regs.isChinese("你好!"));
        assertFalse(Regs.isChinese("你好."));
        assertFalse(Regs.isChinese("你好a"));
        assertFalse(Regs.isChinese("你好_"));
    }

    public void testContainChinese() {
        assertTrue(Regs.isContainChinese("你好"));
        assertTrue(Regs.isContainChinese("你好！"));
        assertTrue(Regs.isContainChinese("你好。"));
        assertTrue(Regs.isContainChinese("123！"));
        assertTrue(Regs.isContainChinese("123。"));
        assertTrue(Regs.isContainChinese("你好!"));
        assertTrue(Regs.isContainChinese("你好."));
        assertTrue(Regs.isContainChinese("你好a"));
        assertTrue(Regs.isContainChinese("你好_"));
        assertTrue(Regs.isContainChinese("你好1"));
        assertTrue(Regs.isContainChinese("你好&"));

        assertFalse(Regs.isContainChinese("hallo&"));
        assertFalse(Regs.isContainChinese("123&"));
        assertFalse(Regs.isContainChinese("123hallo"));
    }

}
