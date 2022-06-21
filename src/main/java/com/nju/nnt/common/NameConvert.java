package com.nju.nnt.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameConvert {
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线,最后转为大写
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString().toUpperCase();
    }

    /**
     * 下划线转驼峰,正常输出
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(humpToLine("{\n" +
                "    \"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvd2lBeTQ0bDMyMER6c185azJyMDJEUzJoZ2dRIiwiZ2VuZGVyIjoiMCIsIm5pY2tuYW1lIjpudWxsLCJhdmF0YXJVcmwiOiJodHRwczovL3RoaXJkd3gucWxvZ28uY24vbW1vcGVuL3ZpXzMyL1BPZ0V3aDRtSUhPNG5pYkgwS2xNRUNOampHeFFVcTI0WkVhR1Q0cG9DNmljUmljY1ZHS1N5WHdpYmNQcTRCV21pYUlHdUcxaWN3eGFRWDZnckM5VmVtWm9KOHJnLzEzMiIsImV4cCI6MTY1NDU3MDQ3MH0.oSQ_popDhtpoACk1Ww1PAMkPhDdQiaUU4U2uiwpq1Ts\",\n" +
                "    \"from\":\"1529761522409193474\",\n" +
                "    \"to\":\"owiAy44l320Dzs_9k2r02DS2hggQ\",\n" +
                "    \"msg\":\"这个很贵的\",\n" +
                "    \"from_avatar\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132\",\n" +
                "    \"to_avatar\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132\",\n" +
                "    \"from_name\":\"阿道夫\",\n" +
                "    \"to_name\":\"\",\n" +
                "    \"k\":\"\"\n" +
                "\n" +
                "}"));
    }
}
