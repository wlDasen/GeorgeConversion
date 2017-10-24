package net.sunniwell.georgeconversion.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by admin on 2017/10/24.
 */

public class PinyinUtils {
    public static String getPinyin(String source) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder builder = new StringBuilder();
        char[] character = source.toCharArray();
        for (char c : character) {
            try {
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]")) {
                    String pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
                    builder.append(pinyin);
                } else {
                    builder.append(Character.toString(c));
                }

            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
