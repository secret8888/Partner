package com.partner.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.partner.common.constant.Consts;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.youdao.sharesdk.platform.weixin.WeiXinClient;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WeiXinClient.getInstance(WXEntryActivity.this, Consts.WX_APP_ID).handleIntent(getIntent(), this);
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
		WeiXinClient.getInstance(WXEntryActivity.this, Consts.WX_APP_ID).handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq arg0) {
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {
//		int result = 0;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
//			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
//			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
//			result = R.string.errcode_deny;
			break;
		default:
//			result = R.string.errcode_unknown;
			break;
		}
		
//		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
}
