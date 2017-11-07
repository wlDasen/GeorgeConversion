package net.sunniwell.georgeconversion.db;

import org.litepal.crud.DataSupport;

/**
 * LitePal数据库对应的数据模型，用来存储跟货币相关的所有信息
 */

public class Money extends DataSupport implements Cloneable {
    /**
     * 货币中文全称 如人民币
     */
    private String name;
    /**
     * 货币英文代号 如CNY
     */
    private String code;
    /**
     * 货币是否是主界面4种货币或者是货币选择界面的收藏列表货币
     */
    private boolean isMain4Money;
    /**
     * 是否曾经被用户选择过，选择过得货币会被添加到货币选择界面的收藏货币列表
     */
    private boolean isEverChoosed;
    /**
     * 基于1人民币所能兑换的本货币的数量
     */
    private double base1CNYToCurrent;
    /**
     * 基于1本货币能兑换的人民币的数量
     */
    private double base1CurrentToCNY;
    /**
     * 排序字段：主界面依据此字段来显示在不同的位置
     * -1-非主界面货币 0-主界面第一个位置 1-第二个位置 2-第三个位置
     */
    private int sortField;
    /**
     * item的拼音大写首字母显示在货币选择界面的表头位置 如人民币对应的是R
     */
    private String firstLetter;
    /**
     * item全部小写拼音 如人民币对应的renminbi
     */
    private String letters;

    public int getSortField() {
        return sortField;
    }

    public void setSortField(int sortField) {
        this.sortField = sortField;
    }

    public boolean isEverChoosed() {
        return isEverChoosed;
    }

    public void setEverChoosed(boolean everChoosed) {
        isEverChoosed = everChoosed;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public Money() {}

    public Money(String name, String code, boolean isMain4Money, double base1CNYToCurrent,
                 double base1CurrentToCNY, boolean isEverChoosed, int sortField) {
        this.name = name;
        this.code = code;
        this.isMain4Money = isMain4Money;
        this.base1CNYToCurrent = base1CNYToCurrent;
        this.base1CurrentToCNY = base1CurrentToCNY;
        this.isEverChoosed = isEverChoosed;
        this.sortField = sortField;
    }

    public double getBase1CNYToCurrent() {
        return base1CNYToCurrent;
    }

    public void setBase1CNYToCurrent(double base1CNYToCurrent) {
        this.base1CNYToCurrent = base1CNYToCurrent;
    }

    public boolean isMain4Money() {
        return isMain4Money;
    }

    public void setMain4Money(boolean main4Money) {
        isMain4Money = main4Money;
    }

    public double getBase1CurrentToCNY() {
        return base1CurrentToCNY;
    }

    public void setBase1CurrentToCNY(double base1CurrentToCNY) {
        this.base1CurrentToCNY = base1CurrentToCNY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "[Money]name:" + name + ",code:" + code + ",isMain4Money:" + isMain4Money
                + ",base1CNYToCurrent:" + base1CNYToCurrent + ",base1CurrentToCNY:" + base1CurrentToCNY
                + ",firstLetter:" + firstLetter + ",letters:" + letters + ",isEverChoosed:" + isEverChoosed
                + ",sortField:" + sortField;
    }

    /**
     * 重写clone方法实现深克隆，默认super.clone原理是浅克隆
      */

    @Override
    public Object clone() throws CloneNotSupportedException {
        Money money = new Money(new String(this.name), new String(code), this.isMain4Money
            , this.base1CNYToCurrent, this.base1CurrentToCNY, this.isEverChoosed, this.sortField);
        return money;
    }
}