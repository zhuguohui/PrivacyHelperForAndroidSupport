package com.trs.app.privacy.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trs.app.privacy.CheckApp;
import com.trs.app.privacy.R;
import com.trs.app.privacy.util.SpUtil;


public class UserProtocolUtil {
    private static FragmentActivity sContext;
    static String userAgreementUrl;
    static String userPrivacyPolicyUrl;
    static String appName;
    static String linkColor = "#b1070d";//超链接颜色，只需修改这里就行
    static Action showUAAction;
    static Action showPPAction;
    static Action successAction;
    private static final String htmlContent =
            "感谢您的信任与使用!" +
            "<br>%s非常重视用户的个人信息和隐私保护。我们依据最新的法律法规要求，更新并制定了<a href=\"a.html\" >《用户协议》</a>和<a href=\"b.html\">《隐私政策》</a>，其中有对您权利义务的特别规定以及管辖约定等重要条款，在您使用%sAPP及服务前，请务必仔细阅读并透彻理解该协议与政策，同意并接受全部条款后可开始使用我们的服务。您可点击下述链接阅读完整《用户协议》和《隐私政策》了解具体内容。如您对协议与隐私内容有任何疑问意见或建议，可与我们联系。"+
            "<br>当您点击“同意”即表示您已充分阅读理解并接受《用户协议》和《隐私政策》的全部内容。" ;

    public static final String KEY_HAVE_SHOW_USER_PROTOCOL = "key_have_show_user_protocol";

    private static volatile boolean haveShow = false;
    private static UserProtocolFragmentDialog userProtocalFragmentDialog;

    public static void show(FragmentActivity context, String appName, Action showUAAction, Action showPPAction, Action successAction) {
        sContext = context;
        UserProtocolUtil.userAgreementUrl = userAgreementUrl;
        UserProtocolUtil.userPrivacyPolicyUrl = userPrivacyPolicyUrl;
        UserProtocolUtil.appName = appName;
        UserProtocolUtil.showUAAction = showUAAction;
        UserProtocolUtil.showPPAction = showPPAction;
        UserProtocolUtil.successAction = successAction;
        if (haveShow(context)) {
            if (successAction != null) successAction.call();
            return;
        }

        if (userProtocalFragmentDialog == null || userProtocalFragmentDialog.isHidden()) {
            if (userProtocalFragmentDialog != null) {
                userProtocalFragmentDialog.dismiss();
                userProtocalFragmentDialog = null;
            }
            userProtocalFragmentDialog = new UserProtocolFragmentDialog();
        }
        userProtocalFragmentDialog.show(context.getSupportFragmentManager(), "userProtocal");


    }

    /**
     * 显示用户协议的虚浮窗
     *
     * @param context              上下文
     * @param appName              应用名称
     * @param userAgreementUrl     用户协议地址
     * @param userPrivacyPolicyUrl 隐私政策地址
     * @param action               如果已经显示用户协议，或者点击仅浏览、同意继续后需要执行的操作
     */
    public static void show(FragmentActivity context, String appName, String userAgreementUrl, String userPrivacyPolicyUrl, Action action) {
        show(context, appName, new Action() {
            @Override
            public void call() {
             //   ToastUtils.show("显示网页:"+userAgreementUrl);
                openUrl(context,userAgreementUrl);
            }
        }, new Action() {
            @Override
            public void call() {
                openUrl(context,userPrivacyPolicyUrl);
            }
        },action);
    }

    private static void openUrl(Context context,String url){
        Uri uri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID,context .getPackageName());
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w("URLSpan", "Actvity was not found for intent, " + intent.toString());
        }
    }

    public interface Action {
        void call();
    }


    public static boolean haveShow(Context context) {
        return CheckApp.getApp().isUserAgree();
//        return SpUtil.getBoolean(context, KEY_HAVE_SHOW_USER_PROTOCOL, false);
    }

    public static void setIsShow(Context context, boolean isShow) {
        SpUtil.putBoolean(context, KEY_HAVE_SHOW_USER_PROTOCOL, isShow);
    }

    public static class UserProtocolFragmentDialog extends DialogFragment {


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.layout_dialog_user_protocol, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            TextView tvContent = view.findViewById(R.id.tv_content);

            String content = String.format(htmlContent, appName, appName);
            tvContent.setText(getClickableHtml(content));
            tvContent.setClickable(true);
            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
            view.findViewById(R.id.btn_agree).setOnClickListener(v -> {

                this.dismissAllowingStateLoss();
                if (successAction != null) successAction.call();

            });
            view.findViewById(R.id.btn_disagree).setOnClickListener(v -> {
                this.dismissAllowingStateLoss();
                //用户修改为不同意就关闭
                getActivity().finish();

            });

        }

        @Override
        public void onStart() {
            super.onStart();
            getDialog().setCancelable(false);
        }

    }


    // 确定可点区域，并设置点击事件
    private static void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder,
                                         final URLSpan urlSpan, int index) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);

        PhoneClickSpan phoneClickSpan = new PhoneClickSpan(new PhoneClickSpan.OnLinkClickListener() {
            @Override
            public void onLinkClick(View view) {
                // do something
                //   doSomething();
                if(index==0){
                    if(showUAAction!=null){
                        showUAAction.call();
                    }
                }else if(index==1){
                    if(showPPAction!=null){
                        showPPAction.call();
                    }
                }
            }
        });
        clickableHtmlBuilder.removeSpan(urlSpan);
        clickableHtmlBuilder.setSpan(phoneClickSpan, start, end, flags);
    }

    // 为所有超链接设置样式
    private static CharSequence getClickableHtml(String html) {
        Spanned spannedHtml = Html.fromHtml(html);
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        // 获取所有超链接
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        int index=0;
        for (final URLSpan span : urls) {
            setLinkClickable(clickableHtmlBuilder, span,index++); // 为每个超链接样式设置
        }
        return clickableHtmlBuilder;
    }

    static class PhoneClickSpan extends ClickableSpan {
        public interface OnLinkClickListener {
            void onLinkClick(View view);
        }

        private OnLinkClickListener listener;

        public PhoneClickSpan(OnLinkClickListener listener) {
            super();
            this.listener = listener;
        }

        @Override
        public void onClick(View widget) {
            listener.onLinkClick(widget);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor(linkColor)); // 设置字体颜色
            ds.setUnderlineText(false); //去掉下划线
        }

    }

}
