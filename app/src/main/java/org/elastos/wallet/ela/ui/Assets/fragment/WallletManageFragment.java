package org.elastos.wallet.ela.ui.Assets.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.bean.BusEvent;
import org.elastos.wallet.ela.db.RealmUtil;
import org.elastos.wallet.ela.db.table.Wallet;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.NewBaseViewData;
import org.elastos.wallet.ela.ui.Assets.fragment.mulsignwallet.ShowMulWallletPublicKeyFragment;
import org.elastos.wallet.ela.ui.Assets.presenter.WallletManagePresenter;
import org.elastos.wallet.ela.ui.Assets.viewdata.WalletManageViewData;
import org.elastos.wallet.ela.ui.common.bean.CommmonBooleanEntity;
import org.elastos.wallet.ela.ui.common.bean.CommmonStringEntity;
import org.elastos.wallet.ela.ui.common.viewdata.CommmonStringWithMethNameViewData;
import org.elastos.wallet.ela.utils.DialogUtil;
import org.elastos.wallet.ela.utils.RxEnum;
import org.elastos.wallet.ela.utils.listener.WarmPromptListener;
import org.elastos.wallet.ela.utils.listener.WarmPromptListener2;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WallletManageFragment extends BaseFragment implements WarmPromptListener, WalletManageViewData, CommmonStringWithMethNameViewData, NewBaseViewData {

    private static final String DELETE = "delete";

    private static final String OUTPORTMN = "outportmm";
    private static final String OUTPORTMUPK = "outportmupk";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;
    @BindView(R.id.tv_updatename)
    TextView tvUpdatename;
    @BindView(R.id.ll_updatename)
    LinearLayout llUpdatename;
    @BindView(R.id.ll_updatepwd)
    LinearLayout llUpdatepwd;
    @BindView(R.id.ll_exportkeystore)
    LinearLayout llExportkeystore;
    @BindView(R.id.ll_exportmnemonic)
    LinearLayout llExportmnemonic;
    @BindView(R.id.ll_sign)
    LinearLayout llSign;
    @BindView(R.id.ll_exportreadonly)
    LinearLayout llExportreadonly;
    @BindView(R.id.ll_showmulpublickey)
    LinearLayout llShowmulpublickey;
    @BindView(R.id.ll_showwalletpublickey)
    LinearLayout llShowwalletpublickey;
    private DialogUtil dialogUtil;
    String dialogAction = null;
    private Dialog dialog;
    private Wallet wallet;
    private WallletManagePresenter presenter;
    private String payPasswd;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wallet_manage;
    }

    @Override
    protected void setExtraData(Bundle data) {
        super.setExtraData(data);
        wallet = data.getParcelable("wallet");
        if (wallet != null)
            tvUpdatename.setText(wallet.getWalletName());


    }

    @Override
    protected void initView(View view) {
        tvTitle.setText(R.string.walletmanage);
        dialogUtil = new DialogUtil();
        presenter = new WallletManagePresenter();
        registReceiver();
        switch (wallet.getType()) {
            //0 普通单签 1单签只读 2普通多签 3多签只读
            case 0:
                llShowwalletpublickey.setVisibility(View.GONE);
                break;
            case 1:
                llUpdatepwd.setVisibility(View.GONE);
                llExportmnemonic.setVisibility(View.GONE);
                llShowwalletpublickey.setVisibility(View.GONE);
                break;
            case 2:
                llExportmnemonic.setVisibility(View.GONE);
                llExportreadonly.setVisibility(View.GONE);
                llShowmulpublickey.setVisibility(View.GONE);
                break;
            case 3:
                llUpdatepwd.setVisibility(View.GONE);
                llExportmnemonic.setVisibility(View.GONE);
                llExportreadonly.setVisibility(View.GONE);
                llShowmulpublickey.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick({R.id.tv_delete, R.id.ll_updatename, R.id.ll_updatepwd, R.id.ll_exportkeystore, R.id.ll_exportmnemonic,
            R.id.ll_sign, R.id.ll_exportreadonly, R.id.ll_showmulpublickey, R.id.ll_showwalletpublickey, R.id.ll_nodeconect})
    public void onViewClicked(View view) {
        Bundle bundle = null;
        dialogAction = null;
        switch (view.getId()) {
            case R.id.tv_delete:
                //删除钱包弹框
                dialogUtil.showWarmPrompt1(getBaseActivity(), getString(R.string.deletewallletornot), new WarmPromptListener() {
                    @Override
                    public void affireBtnClick(View view) {
                        dialog = dialogUtil.showWarmPromptInput(getBaseActivity(), null, null, WallletManageFragment.this);
                        dialogAction = DELETE;
                    }
                });
                break;
            case R.id.ll_updatename:
                //修改钱包名称
                bundle = new Bundle();
                bundle.putString("walletId", wallet.getWalletId());
                start(WalletUpdateName.class, bundle);
                break;
            case R.id.ll_updatepwd:
                //修改密码
                bundle = new Bundle();
                bundle.putString("walletId", wallet.getWalletId());
                start(WalletUpdataPwdFragment.class, bundle);
                break;
            case R.id.ll_exportkeystore:
                //导出keystore
                bundle = new Bundle();
                bundle.putParcelable("wallet", wallet);
                start(OutportKeystoreFragment.class, bundle);
                break;
            case R.id.ll_exportmnemonic:
                //导出助记词弹框
                dialog = dialogUtil.showWarmPromptInput(getBaseActivity(), null, null, this);
                dialogAction = OUTPORTMN;
                break;
            case R.id.ll_sign:
                //签名
                bundle = new Bundle();
                bundle.putParcelable("wallet", wallet);
                start(SignTransactionFragment.class, bundle);
                break;
            case R.id.ll_exportreadonly:
                //导出只读
                bundle = new Bundle();
                bundle.putParcelable("wallet", wallet);
                start(ExportReadOnlyFragment.class, bundle);
                break;
            case R.id.ll_showmulpublickey:
                //查看多签公钥
                presenter.getPubKeyInfo(wallet.getWalletId(), this);
                dialogAction = OUTPORTMUPK;
                break;
            case R.id.ll_showwalletpublickey:
                //查看多签钱包公钥
                bundle = new Bundle();
                bundle.putParcelable("wallet", wallet);
                start(ShowMulWallletPublicKeyFragment.class, bundle);
                break;
            case R.id.ll_nodeconect:
                //节点连接设置


                start(NodeConnectSetFragment.class, getArguments());
                break;

        }
    }


    @Override
    public void affireBtnClick(View view) {
//这里只见他showWarmPromptInput的确认
        if (wallet.getType() == 1 || wallet.getType() == 3) {
            //0 普通单签 1单签只读 2普通多签 3多签只读
            presenter.destroyWallet(wallet.getWalletId(), this);
            return;
        }
        payPasswd = ((EditText) view).getText().toString().trim();
        if (TextUtils.isEmpty(payPasswd)) {
            showToastMessage(getString(R.string.pwdnoempty));
            return;
        }

        if (OUTPORTMN.equals(dialogAction)) {
            //导出助记词
            presenter.exportWalletWithMnemonic(wallet.getWalletId(), payPasswd, this);

        } else if (DELETE.equals(dialogAction)) {
            //删除钱包  验证密码
            presenter.verifyPayPassword(wallet.getWalletId(), payPasswd, this);
        } else if (OUTPORTMUPK.equals(dialogAction)) {
            //查看多签公钥的兼容  输一遍密码
            presenter.verifyPayPassword(wallet.getWalletId(), payPasswd, this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(BusEvent result) {
        int integer = result.getCode();
        if (integer == RxEnum.UPDATA_WALLET_NAME.ordinal()) {
            wallet.setWalletName(result.getName());
            tvUpdatename.setText(result.getName());

        }
    }


    @Override
    public void onDestoryWallet(String data) {
        dialog.dismiss();
        RealmUtil realmUtil = new RealmUtil();
        realmUtil.deleteWallet(wallet.getWalletId());//删除钱包和其子钱包
        // realmUtil.deleteSubWallet(wallet.getWalletId());
        List<Wallet> wallets = realmUtil.queryUserAllWallet();
        if (wallets == null || wallets.size() == 0) {
            //没有其他钱包了
            toHomeWalletFragment();
            return;
        }
        post(RxEnum.DELETE.ordinal(), null, wallet.getWalletId());
        showToastMessage(getString(R.string.deletewalletsucess));
        popBackFragment();
    }

    @Override
    public void onGetCommonData(String methodname, String data) {
        //exportWalletWithMnemonic
        dialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("mnemonic", data);
        bundle.putParcelable("wallet", wallet);
        start(OutportMnemonicFragment.class, bundle);

    }


    @Override
    public void onGetData(String methodName, BaseEntity baseEntity, Object o) {
        switch (methodName) {
            case "getPubKeyInfo":
                String pubKeyInfo = ((CommmonStringEntity) baseEntity).getData();
                JsonObject pubKeyInfoJsonData = new JsonParser().parse(pubKeyInfo).getAsJsonObject();
                String derivationStrategy = pubKeyInfoJsonData.get("derivationStrategy").getAsString();
                int n = pubKeyInfoJsonData.get("n").getAsInt();
                String requestPubKey;
                if ("BIP44".equals(derivationStrategy) && n > 1) {
                    requestPubKey = pubKeyInfoJsonData.get("xPubKey").getAsString();

                } else {
                    requestPubKey = pubKeyInfoJsonData.get("xPubKeyHDPM").getAsString();
                }
                if (!TextUtils.isEmpty(requestPubKey)) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("wallet", wallet);
                    bundle.putString("requestPubKey", requestPubKey);
                    start(ShowMulsignPublicKeyFragment.class, bundle);
                } else {
                    dialog = dialogUtil.showWarmPromptInput(getBaseActivity(), null, null, this);
                }
                break;
            case "verifyPassPhrase":
                boolean result1 = ((CommmonBooleanEntity) baseEntity).getData();
                if (result1) {
                    //助记词密码正确
                    presenter.destroyWallet(wallet.getWalletId(), this);
                } else {
                    showToastMessage(getString(R.string.error_20003));
                }
                break;
            case "verifyPayPassword":
                //目前只在删除一种情况调用
                boolean result = ((CommmonBooleanEntity) baseEntity).getData();
                if (result) {
                    dialog.dismiss();
                    if (OUTPORTMUPK.equals(dialogAction)) {
                        presenter.getPubKeyInfo(wallet.getWalletId(), this);
                    } else if (DELETE.equals(dialogAction)) {
                        presenter.getMasterWalletBasicInfo(wallet.getWalletId(), this);
                    }
                } else {
                    showToastMessage(getString(R.string.error_20003));
                }

                break;
            case "getMasterWalletBasicInfo":
                //目前只在删除一种情况顺序调用
                /*if (!DELETE.equals(dialogAction)) {
                    return;
                }*/
                String data = ((CommmonStringEntity) baseEntity).getData();
                JsonObject jsonData = new JsonParser().parse(data).getAsJsonObject();
                boolean hasPassPhrase = jsonData.get("HasPassPhrase").getAsBoolean();
                if (hasPassPhrase) {
                    dialog = dialogUtil.showWarmPromptInput3(getBaseActivity(), null, null, new WarmPromptListener2() {
                        @Override
                        public void affireBtnClick(View view) {
                            //先验证助记词密码
                            String passphrase = ((EditText) view).getText().toString().trim();
                            if (TextUtils.isEmpty(passphrase)) {
                                showToastMessage(getString(R.string.pwdnoempty));
                                return;
                            }
                            presenter.verifyPassPhrase(wallet.getWalletId(), passphrase, payPasswd, WallletManageFragment.this);
                        }

                        @Override
                        public void noAffireBtnClick(View view) {
                            //直接删除
                            presenter.destroyWallet(wallet.getWalletId(), WallletManageFragment.this);
                        }
                    });

                } else {

                    if (DELETE.equals(dialogAction)) {
                        //删除的回调
                        presenter.destroyWallet(wallet.getWalletId(), this);
                    }

                }
                break;
        }

    }
}
