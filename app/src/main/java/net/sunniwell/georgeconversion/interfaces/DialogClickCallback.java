package net.sunniwell.georgeconversion.interfaces;

/**
 * Created by admin on 17/11/22.
 * 自定义Dialog点击按钮的Callback接口
 */

public interface DialogClickCallback {
    /**
     * 按钮点击后触发的接口
     * @param position 0－点击了取消按钮 1－点击了确定按钮
     */
    public void onButtonClicked(int position);
}
