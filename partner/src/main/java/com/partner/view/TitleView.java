package com.partner.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partner.R;
import com.partner.common.annotation.Injector;
import com.partner.common.annotation.ViewId;
import com.partner.listener.OnTitleClickListener;

/**
 * Created by emilyu on 6/22/15.
 */
public class TitleView extends RelativeLayout implements View.OnClickListener {

    @ViewId(R.id.im_back)
    private ImageView backView;

    @ViewId(R.id.tv_title)
    private TextView titleView;

    @ViewId(R.id.im_operate)
    private ImageView operateView;

    @ViewId(R.id.tv_operate)
    private TextView operateTextView;

    private OnTitleClickListener mListener;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_title, this);
        Injector.inject(this, this);
        setListeners();
    }

    private void setListeners() {
        backView.setOnClickListener(this);
        operateView.setOnClickListener(this);
        operateTextView.setOnClickListener(this);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setTitle(int resId) {
        titleView.setText(resId);
    }

    public void setOperate(int resId) {
        operateView.setVisibility(View.VISIBLE);
        operateView.setImageResource(resId);
    }

    public void setOperateText(int resId) {
        operateTextView.setVisibility(View.VISIBLE);
        operateTextView.setText(resId);
    }

    public void setListener(OnTitleClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:
                if(mListener != null) {
                    mListener.onTitleBackClick();
                }
                break;
            case R.id.im_operate:
                if(mListener != null) {
                    mListener.onTitleOperateClick();
                }
                break;
            case R.id.tv_operate:
                if(mListener != null) {
                    mListener.onTitleOperateClick();
                }
                break;
        }
    }
}
