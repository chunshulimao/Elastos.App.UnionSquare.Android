package org.elastos.wallet.ela.ui.Assets.presenter;

import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.NewPresenterAbstract;
import org.elastos.wallet.ela.rxjavahelp.ObservableListener;
import org.elastos.wallet.ela.ui.Assets.listener.DestroyWalletListner;
import org.elastos.wallet.ela.ui.common.listener.CommonStringWithiMethNameListener;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class WallletManagePresenter extends NewPresenterAbstract {


    public void exportWalletWithMnemonic(String walletId, String pwd, BaseFragment baseFragment) {
        Observer observer = createObserver(CommonStringWithiMethNameListener.class, baseFragment);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().exportWalletWithMnemonic(walletId, pwd);
            }
        });
        subscriberObservable(observer, observable);
    }

    public void destroyWallet(String walletId, BaseFragment baseFragment) {
        Observer observer = createObserver(DestroyWalletListner.class, baseFragment);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().destroyWallet(walletId);
            }
        });
        subscriberObservable(observer, observable);
    }

    public void exportReadonlyWallet(String walletId, BaseFragment baseFragment) {
        Observer observer = createObserver(CommonStringWithiMethNameListener.class, baseFragment);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().exportReadonlyWallet(walletId);
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }

    public void getPubKeyInfo(String walletId, BaseFragment baseFragment) {
        Observer observer = createObserver(baseFragment, "getPubKeyInfo");
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().getPubKeyInfo(walletId);
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }

    public void getMasterWalletBasicInfo(String walletId, BaseFragment baseFragment) {
        Observer observer = createObserver(baseFragment, "getMasterWalletBasicInfo");
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().getMasterWalletBasicInfo(walletId);
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }

    public void verifyPayPassword(String walletId, String payPasswd, BaseFragment baseFragment) {
        Observer observer = createObserver(baseFragment, "verifyPayPassword");
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().verifyPayPassword(walletId, payPasswd);
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }

    public void verifyPrivateKey(String walletId, String mnemonic, String passphrase, BaseFragment baseFragment) {
        Observer observer = createObserver(baseFragment, "verifyPrivateKey");
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().verifyPrivateKey(walletId, mnemonic, passphrase);
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    } public void verifyPassPhrase(String walletId, String passphrase, String payPasswd, BaseFragment baseFragment) {
        Observer observer = createObserver(baseFragment, "verifyPassPhrase");
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().verifyPassPhrase(walletId, passphrase, payPasswd);
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }
}
