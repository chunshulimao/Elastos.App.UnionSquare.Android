package org.elastos.wallet.ela.ui.main.presenter;

import org.elastos.wallet.ela.utils.Log;

import org.elastos.wallet.ela.ElaWallet.MyWallet;
import org.elastos.wallet.ela.MyApplication;
import org.elastos.wallet.ela.base.BaseActivity;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.net.RetrofitManager;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.ObservableListener;
import org.elastos.wallet.ela.rxjavahelp.PresenterAbstract;
import org.elastos.wallet.ela.ui.common.bean.CommmonObjEntity;
import org.elastos.wallet.ela.ui.common.bean.CommmonObjectWithMethNameEntity;
import org.elastos.wallet.ela.ui.common.listener.CommonObjectWithMethNameListener;
import org.elastos.wallet.ela.ui.main.entity.ServerListEntity;
import org.elastos.wallet.ela.ui.main.listener.MyWalletListener;
import org.elastos.wallet.ela.utils.PingUtil;
import org.elastos.wallet.ela.utils.SPUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainPresenter extends PresenterAbstract {

    private static final String TAG = MainPresenter.class.getSimpleName();

    public void getWallet(BaseActivity baseActivity) {
        Observer observer = createObserver(MyWalletListener.class, baseActivity, false);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                MyWallet myWallet = baseActivity.getWallet();
                return new CommmonObjEntity(MyWallet.SUCCESSCODE, myWallet);
            }
        });
        subscriberObservable(observer, observable);
    }


    /*public void getServerList(BaseFragment baseFragment) {
        Observable<ServerListEntity> observable = RetrofitManager.specialCreate().getServerList();
        Observer observer = createObserver(ServerCommonObjectWithMNListener.class, baseFragment, false, "getServerList");
        subscriberObservable(observer, observable, baseFragment);
    }*/

    public void ping(BaseFragment baseFragment, List<String> list, String defaultAdd) {
        Observer observer = createObserver(CommonObjectWithMethNameListener.class, baseFragment, false, "ping");
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                String result = PingUtil.ping(list, defaultAdd);
                return new CommmonObjectWithMethNameEntity(MyWallet.SUCCESSCODE, result, "ping");

            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }

    public void getServerList(BaseFragment baseFragment) {

        Observer observer = new Observer<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(BaseEntity value) {
                ServerListEntity serverListEntity = (ServerListEntity) value;
                if ("0".equals(serverListEntity.getCode())) {
                    List<String> list = serverListEntity.getData();
                    MyApplication.serverList = new HashSet<>(list);
                    new SPUtil(baseFragment.getContext()).setDefaultServerList(MyApplication.serverList);
                    ping(baseFragment, list, MyApplication.REQUEST_BASE_URL);

                } else {
                    ping(baseFragment, new ArrayList<>(MyApplication.serverList), MyApplication.REQUEST_BASE_URL);
                }

            }

            @Override
            public void onError(Throwable e) {
                ping(baseFragment, new ArrayList<>(MyApplication.serverList), MyApplication.REQUEST_BASE_URL);
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete()");
            }
        };
        Observable observable = RetrofitManager.specialCreate().getServerList();
        subscriberObservable(observer, observable, baseFragment);
    }
}
