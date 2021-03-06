package org.elastos.wallet.ela.ui.Assets.fragment;


import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.allen.library.SuperButton;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.ElaWallet.MyWallet;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.db.RealmUtil;
import org.elastos.wallet.ela.db.listener.RealmTransactionAbs;
import org.elastos.wallet.ela.db.table.Wallet;
import org.elastos.wallet.ela.ui.Assets.presenter.CommonCreateSubWalletPresenter;
import org.elastos.wallet.ela.ui.Assets.presenter.ImportKeystorePresenter;
import org.elastos.wallet.ela.ui.Assets.viewdata.CommonCreateSubWalletViewData;
import org.elastos.wallet.ela.ui.Assets.viewdata.ImportKeystoreViewData;
import org.elastos.wallet.ela.utils.AppUtlis;
import org.elastos.wallet.ela.utils.ClearEditText;
import org.elastos.wallet.ela.utils.RxEnum;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImportKeystoreFragment extends BaseFragment implements ImportKeystoreViewData, CommonCreateSubWalletViewData {


    @BindView(R.id.et_keystore)
    AppCompatEditText etKeystore;
    @BindView(R.id.et_walletname)
    ClearEditText etWalletname;
    @BindView(R.id.et_walletpws)
    ClearEditText etWalletpws;
    @BindView(R.id.et_walletpws_agin)
    ClearEditText etWalletpwsAgin;
    @BindView(R.id.sb_sure)
    SuperButton sbSure;
    Unbinder unbinder;
    @BindView(R.id.et_keystore_pwd)
    ClearEditText etKeystorePwd;

    private ImportKeystorePresenter presenter;
    private String keystore;
    private String walletName;
    private String masterWalletID;
    private RealmUtil realmUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_keystore;
    }


    @Override
    protected void initView(View view) {
        mRootView.setBackgroundResource(R.color.transparent);
        realmUtil = new RealmUtil();
        presenter = new ImportKeystorePresenter();
    }

    @OnClick({R.id.sb_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sb_sure:
                //确认导入钱包

                keystore = etKeystore.getText().toString().trim();
                if (TextUtils.isEmpty(keystore)) {
                    showToastMessage(getString(R.string.keystoreinputwrong));
                    return;
                }
                walletName = etWalletname.getText().toString().trim();
                if (TextUtils.isEmpty(walletName)) {
                    showToastMessage(getString(R.string.walletnamenotnull));
                    return;
                }
              /*  if (!MatcherUtil.isRightName(walletName)) {
                    showToastMessage(getString(R.string.walletnameruler));
                    return;

                }*/
                String keystorePwd = etKeystorePwd.getText().toString().trim();

                String payPassword = etWalletpws.getText().toString().trim();
                if (TextUtils.isEmpty(payPassword)) {
                    showToastMessage(getString(R.string.pwdnoempty));
                    return;
                }
                if (!AppUtlis.chenckString(payPassword)) {
                    showToast(getString(R.string.mmgsbd));
                    return;
                }
                String walletPwdAgin = etWalletpwsAgin.getText().toString().trim();

                if (TextUtils.isEmpty(walletPwdAgin)) {
                    showToastMessage(getString(R.string.aginpwdnotnull));
                    return;
                }
                if (!payPassword.equals(walletPwdAgin)) {
                    showToastMessage(getString(R.string.keynotthesame));
                    return;
                }

                masterWalletID = AppUtlis.getStringRandom(8);
                presenter.importWalletWithKeystore(masterWalletID, keystore, keystorePwd,
                        payPassword, this);
                break;

        }
    }

    @Override
    public void onImportKeystore(String data) {
        new CommonCreateSubWalletPresenter().createSubWallet(masterWalletID, MyWallet.ELA, this,null);
    }

    @Override
    public void onCreateSubWallet(String data,Object o) {

        if (data != null) {
            //创建Mainchain子钱包
            Wallet masterWallet = realmUtil.updateWalletDetial(walletName, masterWalletID, data);
            realmUtil.updateSubWalletDetial(masterWalletID, data, new RealmTransactionAbs() {
                @Override
                public void onSuccess() {
                    realmUtil.updateWalletDefault(masterWalletID, new RealmTransactionAbs() {
                        @Override
                        public void onSuccess() {
                            post(RxEnum.ONE.ordinal(), null, masterWallet);
                            toMainFragment();
                        }
                    });
                }
            });


        }
    }
}


