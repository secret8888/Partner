package com.partner.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.partner.activity.activity.ActivityDetailActivity;
import com.partner.activity.GuideActivity;
import com.partner.activity.activity.ActivitySignActivity;
import com.partner.activity.activity.ContentActivity;
import com.partner.activity.activity.InstitutionInfoActivity;
import com.partner.activity.activity.InviteActivity;
import com.partner.activity.activity.MarkActivity;
import com.partner.activity.activity.PublishActivity;
import com.partner.activity.activity.PublishedActivity;
import com.partner.activity.activity.SignedUserActivity;
import com.partner.activity.info.InfoItemEditActivity;
import com.partner.activity.info.MessageCenterActivity;
import com.partner.activity.info.MessageDetailActivity;
import com.partner.activity.info.MyInfoActivity;
import com.partner.activity.info.MyQrCodeActivity;
import com.partner.activity.info.RegistrationEditActivity;
import com.partner.activity.info.RegistrationInfoActivity;
import com.partner.activity.login.LawActivity;
import com.partner.activity.setting.AboutActivity;
import com.partner.activity.setting.FeedbackActivity;
import com.partner.activity.setting.ModifyPhoneActivity;
import com.partner.activity.setting.ModifyPhoneNextActivity;
import com.partner.activity.setting.SettingActivity;
import com.partner.activity.info.UserInfoEditActivity;
import com.partner.activity.info.LeaveMessageActivity;
import com.partner.activity.login.InstitutionRegisterActivity;
import com.partner.activity.login.LoginActivity;
import com.partner.activity.MainActivity;
import com.partner.activity.login.ModifyPsdActivity;
import com.partner.activity.login.RegisterActivity;
import com.partner.common.constant.IntentConsts;
import com.partner.model.ActivityInfo;
import com.partner.model.FriendInfo;
import com.partner.model.MessageInfo;
import com.partner.model.RegistrationInfo;
import com.partner.qrcode.activity.CaptureActivity;

/**
 * intent manager for the whole application
 * Created by yuym on 4/15/15.
 */
public class IntentManager {


    /**
     * start guide activity
     *
     * @param context
     */
    public static void startGuideActivity(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

    /**
     * start login activity
     *
     * @param context
     */
    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * start register activity
     *
     * @param context
     */
    public static void startRegisterActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    /**
     * start institution register activity
     *
     * @param context
     */
    public static void startInstitutionRegisterActivity(Context context) {
        Intent intent = new Intent(context, InstitutionRegisterActivity.class);
        context.startActivity(intent);
    }

    /**
     * start main activity
     *
     * @param context
     */
    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * start modify psd activity
     *
     * @param context
     */
    public static void startModifyPsdActivity(Context context, boolean isSettingModify) {
        Intent intent = new Intent(context, ModifyPsdActivity.class);
        intent.putExtra(IntentConsts.SETTING_MODIFY_KEY, isSettingModify);
        context.startActivity(intent);
    }

    /**
     * start info edit activity
     *
     * @param fragment
     */
    public static void startInfoEditActivity(Fragment fragment, FriendInfo info, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), UserInfoEditActivity.class);
        intent.putExtra(IntentConsts.INFO_KEY, info);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * start leave message activity
     *
     * @param context
     */
    public static void startLeaveMessageActivity(Context context, int friendId, int activityId) {
        Intent intent = new Intent(context, LeaveMessageActivity.class);
        intent.putExtra(IntentConsts.INFO_KEY, friendId);
        intent.putExtra(IntentConsts.ID_KEY, activityId);
        context.startActivity(intent);
    }

    /**
     * my info activity
     * @param context
     */
    public static void startMyInfoActivity(Context context) {
        Intent intent = new Intent(context, MyInfoActivity.class);
        context.startActivity(intent);
    }

    public static void startInfoItemEditActivity(Context context, int type) {
        Intent intent = new Intent(context, InfoItemEditActivity.class);
        intent.putExtra(IntentConsts.UPDATE_ITEM_KEY, type);
        context.startActivity(intent);
    }

    public static void startMyQrCodeActivity(Context context) {
        Intent intent = new Intent(context, MyQrCodeActivity.class);
        context.startActivity(intent);
    }

    public static void startRegistrationInfoActivity(Context context) {
        Intent intent = new Intent(context, RegistrationInfoActivity.class);
        context.startActivity(intent);
    }

    public static void startRegistrationEditActivity(Context context, RegistrationInfo info) {
        Intent intent = new Intent(context, RegistrationEditActivity.class);
        intent.putExtra(IntentConsts.REFISTRATION_KEY, info);
        context.startActivity(intent);
    }

    public static void startSettingActivity(Activity context, int requestCode) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startFeedbackActivity(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    public static void startModifyPhoneActivity(Context context) {
        Intent intent = new Intent(context, ModifyPhoneActivity.class);
        context.startActivity(intent);
    }

    public static void startModifyPhoneNextActivity(Activity context, String phone, int requestCode) {
        Intent intent = new Intent(context, ModifyPhoneNextActivity.class);
        intent.putExtra(IntentConsts.PHONE_KEY, phone);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startAboutActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    public static void startCaptureActivity(Activity context, int requestCode) {
        Intent intent = new Intent(context, CaptureActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startDetailActivity(Activity context, int activityId) {
        Intent intent = new Intent(context, ActivityDetailActivity.class);
        intent.putExtra(IntentConsts.ID_KEY, activityId);
        context.startActivity(intent);
    }

    public static void startMarkActivity(Activity context, MessageInfo info) {
        Intent intent = new Intent(context, MarkActivity.class);
        intent.putExtra(IntentConsts.INFO_KEY, info);
        context.startActivity(intent);
    }

    public static void startInstitutionInfoActivity(Activity context, int id) {
        Intent intent = new Intent(context, InstitutionInfoActivity.class);
        intent.putExtra(IntentConsts.ID_KEY, id);
        context.startActivity(intent);
    }

    public static void startSignedUserActivity(Activity context, int activityId) {
        Intent intent = new Intent(context, SignedUserActivity.class);
        intent.putExtra(IntentConsts.ID_KEY, activityId);
        context.startActivity(intent);
    }

    public static void startActivitySignActivity(Activity context, int activityId) {
        Intent intent = new Intent(context, ActivitySignActivity.class);
        intent.putExtra(IntentConsts.ID_KEY, activityId);
        context.startActivity(intent);
    }

    public static void startInviteActivity(Activity context) {
        Intent intent = new Intent(context, InviteActivity.class);
        context.startActivity(intent);
    }

    public static void startContentActivity(Activity context, String title, String content,
                                            boolean isEditModel, int inputType, int requestCode) {
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra(IntentConsts.TITLE_KEY, title);
        intent.putExtra(IntentConsts.INFO_KEY, content);
        intent.putExtra(IntentConsts.EDIT_KEY, isEditModel);
        intent.putExtra(IntentConsts.TYPE_KEY, inputType);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startMessageCenterActivity(Activity context, int activityId) {
        Intent intent = new Intent(context, MessageCenterActivity.class);
        intent.putExtra(IntentConsts.ID_KEY, activityId);
        context.startActivity(intent);
    }

    public static void startPublishedActivity(Activity context, int friendId) {
        Intent intent = new Intent(context, PublishedActivity.class);
        intent.putExtra(IntentConsts.ID_KEY, friendId);
        context.startActivity(intent);
    }

    public static void startPublishActivity(Activity context, ActivityInfo info) {
        Intent intent = new Intent(context, PublishActivity.class);
        intent.putExtra(IntentConsts.INFO_KEY, info);
        context.startActivity(intent);
    }

    public static void startMessageDetailActivity(Activity context, MessageInfo messageInfo) {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.putExtra(IntentConsts.INFO_KEY, messageInfo);
        context.startActivity(intent);
    }

    public static void startLawActivity(Activity context) {
        Intent intent = new Intent(context, LawActivity.class);
        context.startActivity(intent);
    }
}
