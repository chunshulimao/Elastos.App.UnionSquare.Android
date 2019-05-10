package org.elastos.wallet.ela.ui.Assets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.db.table.Contact;
import org.elastos.wallet.ela.ui.common.listener.CommonRvListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 资产页面的rv
 */

public class ChooseContactRecAdapter extends RecyclerView.Adapter<ChooseContactRecAdapter.ViewHolder> {


    public void setCommonRvListener(CommonRvListener commonRvListener) {
        this.commonRvListener = commonRvListener;
    }

    private CommonRvListener commonRvListener;
    private List<Contact> list;

    private Context context;

    public ChooseContactRecAdapter(Context context, List<Contact> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_choose, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    CheckBox temp;

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Contact data = list.get(position);
        holder.tvName.setText(data.getName());
        if (commonRvListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (temp != null && holder.cbRadiobutton == temp) {
                        return;
                    }
                    if (temp != null) {
                        temp.setChecked(false);
                    }

                    holder.cbRadiobutton.setChecked(true);
                    temp = holder.cbRadiobutton;
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
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.cb_radiobutton)
        CheckBox cbRadiobutton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
