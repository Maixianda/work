package com.gzliangce.http;


import com.gzliangce.AppEnv;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 请求API转换加密
 * <p>
 * Created by aaron on 11/23/15.
 */
public class HttpMd5Encrypt {
    private static HashMap<String, String> apiMap;
    private static Logger logger = LoggerFactory.getLogger(HttpMd5Encrypt.class);

    /**
     * 判断参数名的ASCII大小
     */
    public static class LoginComparator implements Comparator<String> {
        @Override
        public int compare(String source, String others) {
            if (source.compareTo(others) > 0) {
                return 1;
            } else if (source.compareTo(others) == 0) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * 根据ASCII大小重新从小到大排序
     *
     * @param map
     * @return
     */
    public static String Sequence(HashMap<String, String> map) {
        apiMap = new HashMap<>();
        List<String> list = new ArrayList<String>();
        list.addAll(map.keySet());

        StringBuilder sb = new StringBuilder();
        Collections.sort(list, new LoginComparator());
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i);
            apiMap.put(list.get(i), map.get(key));
            String value = map.get(key);
            sb.append(key).append("=").append(value).append("&");
        }
        return sb.toString();
    }

    /**
     * 进行MD5加密
     *
     * @param string
     * @return
     */
    public static String toMd5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }
        return hex.toString();
    }

    /**
     * 将sign的值加入到原来的map中
     *
     * @param map
     * @return
     */
    public static HashMap<String, String> encryptyMap(HashMap<String, String> map) {
        String signValues = toMd5(Sequence(map));
        apiMap.put("sign", signValues);
        logger.e("md5over : " + apiMap.toString());

        return apiMap;
    }

}
