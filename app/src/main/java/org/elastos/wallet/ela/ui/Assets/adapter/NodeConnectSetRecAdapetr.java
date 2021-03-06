package org.elastos.wallet.ela.ui.Assets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.db.table.SubWallet;
import org.elastos.wallet.ela.ui.common.listener.CommonRvListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 资产页面的rv
 */

public class NodeConnectSetRecAdapetr extends RecyclerView.Adapter<NodeConnectSetRecAdapetr.ViewHolder> {


    public void setCommonRvListener(CommonRvListener commonRvListener) {
        this.commonRvListener = commonRvListener;
    }

    private CommonRvListener commonRvListener;


    private List<SubWallet> list;

    private Context context;


    public NodeConnectSetRecAdapetr(Context context, List<SubWallet> list) {
        this.list = list;
        this.context = context;


    }

    public void setList(List<SubWallet> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nodeconnectset, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SubWallet data = list.get(position);
        holder.tvChainid.setText(data.getChainId());
        if (commonRvListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonRvListener.onRvItemClick(position, data);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_chainid)
        TextView tvChainid;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
