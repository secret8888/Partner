package com.partner.activity.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.common.annotation.Injector;
import com.partner.common.util.ViewUtils;
import com.partner.listener.OnLoadingViewListener;

public abstract class BaseFragmentActivity extends FragmentActivity implements OnLoadingViewListener {

    /* 加载等待dialog*/
    private Dialog loadingDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        if (layoutId != -1) {
            setContentView(layoutId);
        }
        Injector.inject(this, this);
        readIntent();
        initControls(savedInstanceState);
        setListeners();

    }

    @Override
    protected void onDestroy() {
        onDismissLoadingDialog();
        super.onDestroy();
    }

    @Override
    public void onShowLoadingDialog() {
        loadingDialog = ViewUtils.createLoadingDialog(this,
                PartnerApplication.getInstance().getString(R.string.wating_hint));
        loadingDialog.show();
    }

    @Override
    public void onShowLoadingDialog(String loadingText) {
        loadingDialog = ViewUtils.createLoadingDialog(this, loadingText);
        loadingDialog.show();
    }

    @Override
    public void onDismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    /**
     * get layout id for fragment
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * read intent from last object
     *
     * @return
     */
    protected abstract void readIntent();

    /**
     * init base values
     *
     * @return
     */
    protected abstract void initControls(Bundle savedInstanceState);

    /**
     * set listener for fragment views
     */
    protected abstract void setListeners();
}
