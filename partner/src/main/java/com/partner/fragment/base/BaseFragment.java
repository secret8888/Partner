package com.partner.fragment.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.common.annotation.Injector;
import com.partner.common.util.ViewUtils;
import com.partner.listener.OnLoadingViewListener;

/**
 * Created by yuym on 4/2/15.
 */
public abstract class BaseFragment extends Fragment implements OnLoadingViewListener {

    private Dialog loadingDialog = null;

    protected View rootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), null);
            Injector.inject(this, rootView);
            readIntent();
            initControls(savedInstanceState);
            setListeners();
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onShowLoadingDialog() {
        loadingDialog = ViewUtils.createLoadingDialog(getActivity(),
                PartnerApplication.getInstance().getString(R.string.wating_hint));
        loadingDialog.show();
    }

    @Override
    public void onShowLoadingDialog(String loadingText) {
        loadingDialog = ViewUtils.createLoadingDialog(getActivity(), loadingText);
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
