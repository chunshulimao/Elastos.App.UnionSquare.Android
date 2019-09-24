package org.elastos.wallet.ela.ui.crvote.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.ui.crvote.bean.CRListBean;
import org.elastos.wallet.ela.ui.vote.SuperNodeList.NodeDotJsonViewData;
import org.elastos.wallet.ela.ui.vote.SuperNodeList.NodeInfoBean;
import org.elastos.wallet.ela.ui.vote.SuperNodeList.SuperNodeListPresenter;
import org.elastos.wallet.ela.utils.AppUtlis;
import org.elastos.wallet.ela.utils.GlideApp;
import org.elastos.wallet.ela.utils.GlideRequest;
import org.elastos.wallet.ela.utils.NumberiUtil;

import java.math.BigDecimal;
import java.util.List;

public class CRListAdapter extends CRListAdapterFather {

    private final GlideRequest<Bitmap> glideRequest;

    public CRListAdapter(BaseFragment context, @Nullable List<CRListBean.DataBean.ResultBean.ProducersBean> data, int pos, boolean is) {
        super(R.layout.item_cr_list, context, data, is);
        glideRequest = GlideApp.with(context).asBitmap().error(R.mipmap.found_vote_initial).placeholder(R.mipmap.found_vote_initial);


    }

    @Override
    protected void convert(BaseViewHolder helper, CRListBean.DataBean.ResultBean.ProducersBean bean) {
        super.convert(helper, bean);
        helper.setGone(R.id.view, !showCheckbox);

        helper.setBackgroundColor(R.id.tv_rank, context.getResources().getColor(R.color.white));
        helper.setTextColor(R.id.tv_rank, context.getResources().getColor(R.color.surebutton));
        if (is && 0 == helper.getLayoutPosition()) {
            helper.setBackgroundColor(R.id.tv_rank, context.getResources().getColor(R.color.blue1));
            helper.setTextColor(R.id.tv_rank, context.getResources().getColor(R.color.white));
        }
        helper.setText(R.id.tv_rank, "No." + (bean.getIndex() + 1));
        helper.setText(R.id.tv_name, bean.getNickname());
        helper.setText(R.id.tv_address, AppUtlis.getLoc(context.getContext(), bean.getLocation() + ""));
        helper.setText(R.id.tv_zb, NumberiUtil.numberFormat(Double.parseDouble(bean.getVoterate()) * 100 + "", 5) + "%");
        helper.setText(R.id.tv_num, new BigDecimal(bean.getVotes()).intValue() + " " + context.getString(R.string.ticket));

        ImageView iv = helper.getView(R.id.iv_icon);
        iv.setImageResource(R.mipmap.found_vote_initial);
        String baseUrl = bean.getUrl();
        iv.setTag(R.id.error_tag_empty, baseUrl);
        GlideApp.with(context).clear(iv);
        if (baseUrl == null) {
            return;
        }
        if (map.get(baseUrl) != null) {
            if ("".equals(map.get(baseUrl))) {
                return;
            }
            glideRequest.load(map.get(baseUrl)).into(iv);
            return;
        }

        new SuperNodeListPresenter().getUrlJson(iv, baseUrl, context, new NodeDotJsonViewData() {
            @Override
            public void onError(String url) {
                map.put(url, "");
            }

            @Override
            public void onGetNodeDotJsonData(ImageView iv1, NodeInfoBean t, String url) {
                //这个时候的iv已经不是那个iv了  所有传递iv试试
                if (iv1.getTag(R.id.error_tag_empty) == null || !(iv1.getTag(R.id.error_tag_empty).toString()).equals(url)) {
                    return;
                }
                if (t == null || t.getOrg() == null || t.getOrg().getBranding() == null || t.getOrg().getBranding().getLogo_256() == null) {
                    map.put(url, "");
                    return;
                }

                String imgUrl = t.getOrg().getBranding().getLogo_256();
                map.put(url, imgUrl);
                glideRequest.load(imgUrl).into(iv1);
                //CustomViewTarget clear不了
         /*       glideRequest.load(imgUrl).into(new CustomViewTarget<ImageView, Bitmap>(iv1) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        //glideRequest.load(R.mipmap.found_vote_initial).into(iv);
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (iv1.getTag(R.id.error_tag_empty) != null && (url).equals(iv1.getTag(R.id.error_tag_empty).toString())) {
                            //glideRequest.load(resource).into(iv);
                            iv1.setImageBitmap(resource);
                            map.put(iv1.getTag(R.id.error_tag_empty).toString(), resource);
                        }
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                        // glideRequest.load(placeholder).into(iv);
                    }
                });*/


            }
        });
    }


}