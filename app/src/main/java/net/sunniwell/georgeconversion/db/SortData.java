package net.sunniwell.georgeconversion.db;

/**
 * Created by admin on 2017/10/24.
 */

public class SortData {
    /**
     * item的内容
     */
    private String name;
    /**
     * item的首字母
     */
    private String firstLetter;
    /**
     * item全部拼音
     */
    private String letters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }
}
