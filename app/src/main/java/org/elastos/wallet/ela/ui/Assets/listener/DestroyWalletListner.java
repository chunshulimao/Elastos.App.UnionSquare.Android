package org.elastos.wallet.ela.ui.Assets.listener;

import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.SubscriberOnNextLisenner;
import org.elastos.wallet.ela.ui.Assets.viewdata.WalletManageViewData;
import org.elastos.wallet.ela.ui.common.bean.CommmonStringEntity;

public class DestroyWalletListner extends SubscriberOnNextLisenner {
    @Override
    protected void onNextLisenner(BaseEntity t) {
        WalletManageViewData viewData = (WalletManageViewData) getViewData();
        viewData.onDestoryWallet(((CommmonStringEntity) t).getData());
    }
}
