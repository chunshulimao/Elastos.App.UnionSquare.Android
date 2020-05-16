package org.elastos.wallet.ela.ui.committee.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.common.StringUtils;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.ui.committee.bean.PastCtBean;
import org.elastos.wallet.ela.ui.common.listener.CommonRvListener;
import org.elastos.wallet.ela.utils.AppUtlis;
import org.elastos.wallet.ela.utils.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PastCtRecAdapter extends RecyclerView.Adapter<PastCtRecAdapter.ViewHolder>{

    public PastCtRecAdapter(Context context, List<PastCtBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        if(i==0) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ct_past_normal, viewGroup, false);
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ct_past_manager, viewGroup, false);
        }
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PastCtBean.DataBean data = list.get(i);
//        String state = context.getString(R.string.voting);
        viewHolder.time.setText(data.getStartDate() + "-" + data.getEndDate());
        String status = data.getStatus();
        viewHolder.manager.setVisibility(View.GONE);
        if(AppUtlis.isNullOrEmpty(status) || status.equalsIgnoreCase("HISTORY")) {
            viewHolder.title.setText(String.format(context.getString(R.string.pastitemtitle), data.getIndex(), ""));
        } else if(status.equalsIgnoreCase("CURRENT")) {
            viewHolder.title.setText(String.format(context.getString(R.string.pastitemtitle), data.getIndex(), "("+context.getString(R.string.current)+")"));
            viewHolder.manager.setVisibility(View.VISIBLE);
            viewHolder.manager.setText(context.getString(R.string.ctmanager));
        } else if(status.equalsIgnoreCase("VOTING")) {
            viewHolder.title.setText(String.format(context.getString(R.string.pastitemtitle), data.getIndex(), "("+context.getString(R.string.voting)+")"));
            viewHolder.manager.setVisibility(View.VISIBLE);
            viewHolder.manager.setText(context.getString(R.string.votemanager));
        } else {
            viewHolder.title.setText(String.format(context.getString(R.string.pastitemtitle), data.getIndex(), ""));
        }
        if(null != managerListener) {
            viewHolder.manager.setOnClickListener(v ->
                    managerListener.onManagerClick(i)
            );
        }
        if (commonRvListener != null) {
            viewHolder.itemView.setOnClickListener(v -> commonRvListener.onRvItemClick(i, data));
        }
    }

    @Override
    public int getItemViewType(int position) {
        String status = list.get(position).getStatus();
        if(!AppUtlis.isNullOrEmpty(status) && !status.equalsIgnoreCase("HISTORY")) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    public interface ManagerListener {
        void onManagerClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.manager_btn)
        TextView manager;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setManagerListener(ManagerListener listener) {
        this.managerListener = listener;
    }

    public void setCommonRvListener(CommonRvListener commonRvListener) {
        this.commonRvListener = commonRvListener;
    }

    private Context context;
    private ManagerListener managerListener;
    private CommonRvListener commonRvListener;
    private List<PastCtBean.DataBean> list;
}
