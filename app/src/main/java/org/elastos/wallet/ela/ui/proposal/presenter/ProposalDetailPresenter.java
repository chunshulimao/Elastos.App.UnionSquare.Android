package org.elastos.wallet.ela.ui.proposal.presenter;


import android.text.TextUtils;

import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.net.RetrofitManager;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.NewPresenterAbstract;
import org.elastos.wallet.ela.rxjavahelp.ObservableListener;
import org.elastos.wallet.ela.ui.crvote.bean.CRListBean;
import org.elastos.wallet.ela.ui.proposal.bean.ProposalSearchEntity;
import org.elastos.wallet.ela.ui.vote.bean.VoteListBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class ProposalDetailPresenter extends NewPresenterAbstract {
    public void proposalDetail(int id, BaseFragment baseFragment) {

        Observable observable = RetrofitManager.webApiCreate().getProposalDetail(id);
        Observer observer = createObserver(baseFragment, "proposalDetail");
        subscriberObservable(observer, observable, baseFragment);
    }

    public void createVoteCRCProposalTransaction(String walletId, String votes, String invalidCandidates, BaseFragment baseFragment) {
        Observer observer = createObserver(baseFragment, "createVoteCRCProposalTransaction");
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().createVoteCRCProposalTransaction(walletId, votes, invalidCandidates);
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }

    public void getVoteInfo(String walletId, String type, BaseFragment baseFragment) {
        Observer observer = createObserver(baseFragment, "getVoteInfo", type);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().getVoteInfo(walletId, type);
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }


    public void proposalTrackingOwnerDigest(String walletId, String payload, BaseFragment baseFragment, String pwd) {
        Observer observer = createObserver(baseFragment, "proposalTrackingOwnerDigest", pwd);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().proposalTrackingOwnerDigest(walletId, payload);
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }

    public void proposalTrackingNewOwnerDigest(String walletId, String payload, BaseFragment baseFragment, String pwd) {
        Observer observer = createObserver(baseFragment, "proposalTrackingNewOwnerDigest", pwd);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().proposalTrackingNewOwnerDigest(walletId, payload);
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }

    public void proposalTrackingSecretaryDigest(String walletId, String payload, BaseFragment baseFragment, String pwd) {
        Observer observer = createObserver(baseFragment, "ProposalTrackingSecretaryDigest", pwd);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().proposalTrackingSecretaryDigest(walletId, payload);
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }

    public JSONObject getCRUnactiveData(List<CRListBean.DataBean.ResultBean.CrcandidatesinfoBean> list) {

        JSONArray candidates = new JSONArray();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                CRListBean.DataBean.ResultBean.CrcandidatesinfoBean bean = list.get(i);
                if (!bean.getState().equals("Active")) {
                    candidates.put(bean.getDid());
                }
            }
        }
        return getActiveJson("CRC", candidates);
    }

    public JSONObject getDepositUnactiveData(List<VoteListBean.DataBean.ResultBean.ProducersBean> list) {

        JSONArray candidates = new JSONArray();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                VoteListBean.DataBean.ResultBean.ProducersBean bean = list.get(i);
                if (!bean.getState().equals("Active")) {
                    candidates.put(bean.getOwnerpublickey());
                }
            }
        }
        return getActiveJson("Delegate", candidates);
    }

    /**
     * 提取 origin 里的key 组成 key的JSONObject
     *
     * @param origin
     * @return
     */
    public JSONArray getJsonKeys(JSONObject origin) {
        JSONArray target = new JSONArray();
        if (origin != null) {
            Iterator it = origin.keys();
            while (it.hasNext()) {
                target.put(it.next());

            }
        }
        return target;
    }

    public JSONObject getCRLastVote(JSONObject origin) {
        return getActiveJson("CRC", getJsonKeys(origin));
    }

    public JSONObject getActiveJson(String type, JSONArray jsonArray) {
        JSONObject unActiveVote = new JSONObject();

        try {
            unActiveVote.put("Type", type);
            unActiveVote.put("Candidates", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return unActiveVote;
    }

    /**
     * 只获得数组的第一个 的vote数据
     *
     * @param voteInfo
     * @return
     */
    public JSONObject conversVote(String voteInfo) {
        if (!TextUtils.isEmpty(voteInfo) && !voteInfo.equals("null") && !voteInfo.equals("[]")) {
            try {
                JSONArray lastVoteInfo = new JSONArray(voteInfo);
                if (lastVoteInfo.length() >= 1) {
                    return lastVoteInfo.getJSONObject(0).getJSONObject("Votes");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * @param lastVote 上传投票的信息  key value
     * @param amount   新数据的
     * @throws JSONException
     */
    public JSONObject getPublishDataFromLastVote(JSONObject lastVote, String amount, ArrayList<ProposalSearchEntity.DataBean.ListBean> searchBeanList) {
        JSONObject newVotes = new JSONObject();
        try {
            Iterator it = lastVote.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                for (int i = 0; i < searchBeanList.size(); i++) {
                    if (searchBeanList.get(i).getProposalHash().equals(key)) {
                        newVotes.put(key, amount);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newVotes;
    }
}