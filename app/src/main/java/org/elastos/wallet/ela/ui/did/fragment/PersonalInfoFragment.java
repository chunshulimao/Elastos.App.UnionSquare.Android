package org.elastos.wallet.ela.ui.did.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.bean.BusEvent;
import org.elastos.wallet.ela.ui.did.entity.DIDInfoEntity;
import org.elastos.wallet.ela.ui.vote.bean.Area;
import org.elastos.wallet.ela.ui.vote.fragment.AreaCodeFragment;
import org.elastos.wallet.ela.utils.AppUtlis;
import org.elastos.wallet.ela.utils.DateUtil;
import org.elastos.wallet.ela.utils.DialogUtil;
import org.elastos.wallet.ela.utils.RxEnum;
import org.elastos.wallet.ela.utils.SPUtil;
import org.elastos.wallet.ela.utils.listener.WarmPromptListener;
import org.elastos.wallet.ela.utils.widget.TextConfigDataPicker;
import org.elastos.wallet.ela.utils.widget.TextConfigNumberPicker;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalInfoFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    String[] sexs;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.et_headurl)
    EditText etHeadurl;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    private DIDInfoEntity didInfo;
    private DIDInfoEntity.CredentialSubjectBean credentialSubjectBean;
    private long birthday;
    private long code;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personalinfo;
    }

    @Override
    protected void setExtraData(Bundle data) {

        didInfo = data.getParcelable("didInfo");
        credentialSubjectBean = didInfo.getCredentialSubject();
        if (credentialSubjectBean == null) {
            credentialSubjectBean = new DIDInfoEntity.CredentialSubjectBean();
            didInfo.setCredentialSubject(credentialSubjectBean);
        }
        if (data.getBoolean("useDraft"))
            putData();
    }

    @Override
    protected void initView(View view) {
        tvTitle.setText(getString(R.string.adddid));
        tvTitleRight.setVisibility(View.VISIBLE);
        sexs = new String[]{getString(R.string.man), getString(R.string.woman)};
    }


    @OnClick({R.id.rl_selectsex, R.id.rl_selectbirthday, R.id.rl_selectarea, R.id.tv_title_right, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_selectsex:
                new DialogUtil().showCommonSelect(getBaseActivity(), sexs, new WarmPromptListener() {
                    @Override
                    public void affireBtnClick(View view) {
                        tvSex.setText(sexs[((TextConfigNumberPicker) view).getValue()]);
                    }
                });
                break;
            case R.id.rl_selectbirthday:
                Calendar calendar = Calendar.getInstance();
                long minData = calendar.getTimeInMillis();
                int year = calendar.get(Calendar.YEAR);
                calendar.set(Calendar.YEAR, year - 100);
                new DialogUtil().showTime(getBaseActivity(), calendar.getTimeInMillis(), minData, new WarmPromptListener() {
                    @Override
                    public void affireBtnClick(View view) {
                        String date = ((TextConfigDataPicker) view).getYear() + "-" + (((TextConfigDataPicker) view).getMonth() + 1)
                                + "-" + ((TextConfigDataPicker) view).getDayOfMonth();
                        birthday = DateUtil.parseToLong(date) / 1000L;

                        tvBirthday.setText(DateUtil.timeNYR(birthday, getContext()));
                    }
                });
                break;
            case R.id.rl_selectarea:
                start(AreaCodeFragment.class);
                registReceiver();
                break;
            case R.id.tv_title_right:
            case R.id.tv_next:
                setData();
                start(PersonalIntroFragment.class, getArguments());
                break;


        }
    }

    private void putData() {
        etName.setText(credentialSubjectBean.getName());
        etNick.setText(credentialSubjectBean.getNickname());
        tvSex.setText(("n/a".equals(credentialSubjectBean.getGender()) || credentialSubjectBean.getGender() == null) ? null : (credentialSubjectBean.getGender().equals("male") ? getString(R.string.man) : getString(R.string.woman)));
        birthday = credentialSubjectBean.getBirthday();
        tvBirthday.setText(DateUtil.time(birthday, getContext()));
        etHeadurl.setText(credentialSubjectBean.getAvatar());
        etEmail.setText(credentialSubjectBean.getEmail());
        etCode.setText(credentialSubjectBean.getCode());
        etPhone.setText(credentialSubjectBean.getPhone());
        tvArea.setText(AppUtlis.getLoc(getContext(), credentialSubjectBean.getNation()));
    }

    private void setData() {
        credentialSubjectBean.setName(getText(etName));
        credentialSubjectBean.setNickname(getText(etNick));
        credentialSubjectBean.setGender(null == (getText(tvSex)) ? "n/a" : (getString(R.string.man).equals(getText(tvSex)) ? "male" : "female"));
        credentialSubjectBean.setBirthday(birthday);
        credentialSubjectBean.setAvatar(getText(etHeadurl));
        credentialSubjectBean.setEmail(getText(etEmail));
        credentialSubjectBean.setCode(getText(etCode));
        credentialSubjectBean.setPhone(getText(etPhone));
        credentialSubjectBean.setNation(code + "");

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(BusEvent result) {
        int integer = result.getCode();
        if (integer == RxEnum.AREA.ordinal()) {
            Area area = (Area) result.getObj();
            code = area.getCode();
            int Language = new SPUtil(getContext()).getLanguage();
            String name;
            if (Language == 0) {
                name = area.getZh();
            } else {
                name = area.getEn();
            }
            tvArea.setText(name);
        }
    }

}
