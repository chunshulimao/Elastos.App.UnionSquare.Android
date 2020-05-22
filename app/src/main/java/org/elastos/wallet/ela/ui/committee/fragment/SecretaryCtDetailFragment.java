package org.elastos.wallet.ela.ui.committee.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.db.RealmUtil;
import org.elastos.wallet.ela.db.table.Wallet;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.NewBaseViewData;
import org.elastos.wallet.ela.ui.Assets.presenter.WalletManagePresenter;
import org.elastos.wallet.ela.ui.committee.bean.CtDetailBean;
import org.elastos.wallet.ela.ui.committee.presenter.CtDetailPresenter;
import org.elastos.wallet.ela.ui.did.fragment.AuthorizationFragment;
import org.elastos.wallet.ela.utils.AppUtlis;
import org.elastos.wallet.ela.utils.DateUtil;
import org.elastos.wallet.ela.utils.svg.GlideApp;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * secretary general detail fragment
 */
public class SecretaryCtDetailFragment extends BaseFragment implements NewBaseViewData {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    CtDetailPresenter presenter;

    private String id;
    private String did;
    @Override
    protected void setExtraData(Bundle data) {
        super.setExtraData(data);
        id = data.getString("id");
        did = data.getString("did");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ct_secretary_detail;
    }

    @Override
    protected void initView(View view) {
        setToobar(toolbar, toolbarTitle, getContext().getString(R.string.secretarydetail));
        presenter = new CtDetailPresenter();
        presenter.getCurrentCouncilInfo(this, did);
    }

    @Override
    public void onGetData(String methodName, BaseEntity baseEntity, Object o) {
        switch (methodName) {
            case "getCurrentCouncilInfo":
                setInfo((CtDetailBean) baseEntity);
                break;
        }
    }

    @BindView(R.id.head_ic)
    AppCompatImageView headIc;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.secretary_did)
    TextView didTv;
    @BindView(R.id.end_time)
    TextView endTime;
    @BindView(R.id.from_time)
    TextView fromTime;
    @BindView(R.id.birth_time)
    TextView birthDay;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.personal_homepage)
    TextView homepage;
    @BindView(R.id.wechat_account)
    TextView wechat;
    @BindView(R.id.blog_account)
    TextView weibo;
    @BindView(R.id.facebook_account)
    TextView facebook;
    @BindView(R.id.microsoft_account)
    TextView microsoft;
    @BindView(R.id.personal_profile)
    TextView introduce;
    private void setInfo(CtDetailBean ctDetailBean) {
        try {
            CtDetailBean.DataBean dataBean = ctDetailBean.getData();
            GlideApp.with(getContext()).load(dataBean.getAvatar()).error(R.mipmap.icon_ela).circleCrop().into(headIc);
            name.setText(dataBean.getDidName());
            location.setText(AppUtlis.getLoc(getContext(), String.valueOf(dataBean.getLocation())));
            didTv.setText(dataBean.getDid());
            String startDate = dataBean.getStartDate()==0?"":String.valueOf(dataBean.getStartDate());
            String endDate = dataBean.getEndDate()==0?"":String.valueOf(dataBean.getEndDate());
            endTime.setText(String.format("%1$s - %2$s",
                    DateUtil.formatTimestamp(startDate, "yyyy.MM.dd HH:mm:ss"),
                    DateUtil.formatTimestamp(endDate, "yyyy.MM.dd HH:mm:ss")));
            String brithday = dataBean.getBirthday()==0?"":String.valueOf(dataBean.getBirthday());
            birthDay.setText(DateUtil.formatTimestamp(brithday, "yyyy.MM.dd"));
            if(AppUtlis.isNullOrEmpty(brithday)){
                birthdayTitle.setVisibility(View.GONE);
                birthDay.setVisibility(View.GONE);
            }
            email.setText(dataBean.getEmail());
            if(AppUtlis.isNullOrEmpty(dataBean.getEmail())) {
                email.setVisibility(View.GONE);
                emailTitle.setVisibility(View.GONE);
            }
            homepage.setText(dataBean.getAddress());
            if(AppUtlis.isNullOrEmpty(dataBean.getAddress())) {
                homepage.setVisibility(View.GONE);
                homepageTitle.setVisibility(View.GONE);
            }
            wechat.setText(dataBean.getWechat());
            if(AppUtlis.isNullOrEmpty(dataBean.getWechat())) {
                wechatTitle.setVisibility(View.GONE);
                wechat.setVisibility(View.GONE);
            }
            weibo.setText(dataBean.getWeibo());
            if(AppUtlis.isNullOrEmpty(dataBean.getWeibo())) {
                weiboTitle.setVisibility(View.GONE);
                weibo.setVisibility(View.GONE);
            }
            facebook.setText(dataBean.getFacebook());
            if(AppUtlis.isNullOrEmpty(dataBean.getFacebook())) {
                facebookTitle.setVisibility(View.GONE);
                facebook.setVisibility(View.GONE);
            }
            microsoft.setText(dataBean.getMicrosoft());
            if(AppUtlis.isNullOrEmpty(dataBean.getMicrosoft())) {
                microsoftTitle.setVisibility(View.GONE);
                microsoft.setVisibility(View.GONE);
            }
            introduce.setText(dataBean.getIntroduction());
            if(AppUtlis.isNullOrEmpty(dataBean.getIntroduction())) {
                introduce.setVisibility(View.GONE);
                personalprofileTitle.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RealmUtil realmUtil = new RealmUtil();
    private Wallet wallet = realmUtil.queryDefauleWallet();

    @OnClick({R.id.refresh_ct_did})
    public void onClick(View view) {
        //TODO 待确认
        Bundle bundle = new Bundle();
        bundle.putString("type", "authorization");
        bundle.putParcelable("wallet", wallet);
        start(AuthorizationFragment.class, bundle);
        //先绑定did  再更新到服务器
        new WalletManagePresenter().DIDResolveWithTip(did, this, "1");
    }

    @BindView(R.id.birth_time_title)
    TextView birthdayTitle;
    @BindView(R.id.email_title)
    TextView emailTitle;
    @BindView(R.id.personal_homepage_title)
    TextView homepageTitle;
    @BindView(R.id.wechat_account_title)
    TextView wechatTitle;
    @BindView(R.id.blog_account_title)
    TextView weiboTitle;
    @BindView(R.id.facebook_account_title)
    TextView facebookTitle;
    @BindView(R.id.microsoft_account_title)
    TextView microsoftTitle;
    @BindView(R.id.personal_profile_title)
    TextView personalprofileTitle;
}
