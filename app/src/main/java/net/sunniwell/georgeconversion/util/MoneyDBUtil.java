package net.sunniwell.georgeconversion.util;

import android.content.Context;
import android.util.Log;

import net.sunniwell.georgeconversion.R;
import net.sunniwell.georgeconversion.db.Money;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Money数据库操作的封装类，定义了一些获取接口
 */
public class MoneyDBUtil {
    private static final String TAG = "jpd-MoneyDBUtil";

    public static List<Money> getAllMoney() {
        return DataSupport.findAll(Money.class);
    }

    public static List<Money> getMain4Money() {
        List<Money> list = DataSupport
                .where("ismain4money == ? and firstletter != ?", "1", "#")
                .find(Money.class);
        Log.d(TAG, "getMain4Money: size:" + list.size());
        for (int i = 0; i < list.size(); i++) {
            Money money = list.get(i);
            Log.d(TAG, "getMain4Money: " + money);
        }
        return list;
    }

    /**
     * 设置新的4种主要货币并保存数据库
     */
    public static void setMain4Money(String oldMoneyName, String newMoneyName, int swipePosition) {
        List<Money> lists = DataSupport.findAll(Money.class);
        Log.d(TAG, "setMain4Money: size:" + lists.size());
        for (int i = 0; i < lists.size(); i++) {
            Money money = lists.get(i);
            if (oldMoneyName.equals(money.getName())) {
                money.setMain4Money(false);
                money.setSortField(-1);
            }
            if (newMoneyName.equals(money.getName())) {
                money.setMain4Money(true);
                money.setSortField(Integer.valueOf(swipePosition));
                if (!isInEverchooseList(newMoneyName)) {
                    Money chooseMoney = null;
                    try {
                        chooseMoney = (Money)money.clone();
                        chooseMoney.setEverChoosed(true);
                        chooseMoney.setFirstLetter("#");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    boolean save1 = chooseMoney.save();
                }
            }
            boolean save2 = money.save();
        }
        Log.d(TAG, "setMain4Money: size:" + getAllMoney().size());
    }

    /**
     * 判断一种货币是否已经添加到收藏list中
     * @param moneyName
     * @return
     */
    public static boolean isInEverchooseList(String moneyName) {
        Log.d(TAG, "isInEverchooseList: moneyName:" + moneyName);
        List<Money> everChooseList = DataSupport.where("firstletter == ?", "#").find(Money.class);
        for (int i = 0; i < everChooseList.size(); i++) {
            Log.d(TAG, "isInEverchooseList: " + everChooseList.get(i));
            if (moneyName.equals(everChooseList.get(i).getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置默认货币的各种信息
     */
    public static void setMoneyList(Context context) {
        Log.d(TAG, "setMoneyList: ");

        String[] nameArrays = context.getResources().getStringArray(R.array.name);
        String[] codeArrays = context.getResources().getStringArray(R.array.code);
        int[] isMain4MoneyArrays = context.getResources().getIntArray(R.array.isMain4Money);
        int[] isEverChoosedArrays = context.getResources().getIntArray(R.array.isEverChoosed);
        int[] orderFieldArrays = context.getResources().getIntArray(R.array.sortField);
        String[] base1CNYToCurrentArrays = context.getResources().getStringArray(R.array.base1CNYToCurrent);
        String[] base1CurrentToCNYArrays = context.getResources().getStringArray(R.array.base1CurrentToCNY);
        Log.d(TAG, "setMoneyList: len:" + nameArrays.length);
        for (int i = 0; i < nameArrays.length; i++) {
            boolean isMain4Money = isMain4MoneyArrays[i] == 1 ? true : false;
            boolean isEverChoosed = isEverChoosedArrays[i] == 1 ? true : false;
            int orderField = orderFieldArrays[i];
            double base1CNYToCurrent = Double.parseDouble(base1CNYToCurrentArrays[i]);
            double base1CurrentToCNY = Double.parseDouble(base1CurrentToCNYArrays[i]);
            Money money = new Money(nameArrays[i], codeArrays[i], isMain4Money, base1CNYToCurrent, base1CurrentToCNY
                    , isEverChoosed, orderField);
            String pinyin = PinyinUtils.getPinyin(nameArrays[i]);
//            Log.d(TAG, "initData: pinyin:" + pinyin);
            money.setLetters(pinyin.toLowerCase());
            String letter = pinyin.substring(0, 1).toUpperCase();
            if (letter.matches("[A-Z]")) {
                money.setFirstLetter(letter);
            } else {
                money.setFirstLetter("#");
            }
            if (money.isMain4Money()) {
                Money chooseMoney = null;
                try {
                    chooseMoney = (Money)money.clone();
                    chooseMoney.setEverChoosed(true);
                    chooseMoney.setFirstLetter("#");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                boolean save1 = chooseMoney.save();
                Log.d(TAG, "setMoneyList: save1:" + save1);
            }

            Log.d(TAG, "setMoneyList: money:" + money);
            boolean save2 = money.save();
            Log.d(TAG, "setMoneyList: save2:" + save2);
        }
    }
}