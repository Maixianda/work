package com.gzliangce.seccond_ver.checkHourse;

import java.util.Map;
import java.util.Set;

/**
 * Created by Ktoy on 16/6/24.
 */
public final class JSUtils {

    public static String getChangeElementJs(Map<String, String> params) {
        StringBuilder sb=new StringBuilder();
        sb.append("javascript:(function(){");
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for(Map.Entry<String, String> child:entrySet){
            sb.append("document.getElementById(\""+child.getKey()+"\").value ='" + child.getValue() + "';");
        }
        sb.append("})()");
//        String js = "javascript:(function(){\n" +
//                "document.getElementById(\"tokenId\").value =' " + "我爱!!罗明通" + "'" +
//                "})()";
//        Log.e("ceshi-:",sb.toString());
        return sb.toString();
    }
}
