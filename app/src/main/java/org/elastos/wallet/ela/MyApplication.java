package org.elastos.wallet.ela;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;

import org.elastos.wallet.ela.ElaWallet.MyWallet;
import org.elastos.wallet.ela.di.component.ApplicationComponent;
import org.elastos.wallet.ela.di.component.DaggerApplicationComponent;
import org.elastos.wallet.ela.di.moudule.ApplicationModule;

import io.realm.Realm;


public class MyApplication extends MultiDexApplication {

    private static MyApplication myApplication;
    public static int chainID = 0;//默认0正式或者alpha  1testnet 2 regtest

    protected static MyWallet myWallet;
    private ApplicationComponent mApplicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        initApplicationComponent();
        Utils.init(this);
        Realm.init(getApplicationContext());
        String pachageName = getPackageName();
        if (pachageName.endsWith("testnet")) {
            chainID = 1;
        }
        if (pachageName.endsWith("regtest")) {
            chainID = 2;
        }
    }


    public static Context getAppContext() {
        return myApplication.getApplicationContext();
    }


    /**
     * 初始化ApplicationComponent
     */
    private void initApplicationComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    public static MyWallet getMyWallet() {
        return myWallet;
    }

    public static void setMyWallet(MyWallet myWallet) {
        MyApplication.myWallet = myWallet;
    }

}
