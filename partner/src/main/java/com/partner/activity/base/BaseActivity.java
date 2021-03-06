package com.partner.activity.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.common.annotation.Injector;
import com.partner.common.annotation.ViewId;
import com.partner.common.util.ViewUtils;
import com.partner.listener.OnLoadingViewListener;
import com.partner.listener.OnTitleClickListener;
import com.partner.view.TitleView;

public abstract class BaseActivity extends Activity implements OnLoadingViewListener, OnTitleClickListener {

    /* 加载等待dialog*/
    private Dialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        if(layoutId != -1){
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
        if(loadingDialog != null){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    public void onTitleBackClick() {
        onBackPressed();
    }

    @Override
    public void onTitleOperateClick() {

    }

    /**
     * get layout id for fragment
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * read intent from last object
     * @return
     */
    protected abstract void readIntent();

    /**
     * init base values
     * @return
     */
    protected abstract void initControls(Bundle savedInstanceState);

    /**
     * set listener for fragment views
     */
    protected abstract void setListeners();

}
