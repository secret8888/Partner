package com.partner.listener;

/**
 * Created by yuym on 4/2/15.
 */
public interface OnLoadingViewListener {

    /**
     * show loading dialog
     */
    public void onShowLoadingDialog();

    /**
     * show loading dialog
     * @param loadingText
     */
    public void onShowLoadingDialog(String loadingText);

    /**
     * dismiss loading dialog
     */
    public void onDismissLoadingDialog();
}
