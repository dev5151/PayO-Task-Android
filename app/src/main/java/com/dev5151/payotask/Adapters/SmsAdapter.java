package com.dev5151.payotask.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev5151.payotask.Models.Sms;
import com.dev5151.payotask.R;

import java.util.List;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {

    private Context context;
    private List<Sms> smsList;

    public SmsAdapter(Context context, List<Sms> smsList) {
        this.context = context;
        this.smsList = smsList;
    }

    @NonNull
    @Override
    public SmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SmsViewHolder((LayoutInflater.from(context).inflate(R.layout.message_item, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull SmsViewHolder holder, int position) {
        Sms smsObject = smsList.get(position);
        String smsTitle = smsObject.getAddress();
        String body = smsObject.getMsg();

        holder.tvTitle.setText(smsTitle);
        holder.tvBody.setText(body);
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public class SmsViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvBody, tvHashTag;

        public SmsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title);
            tvBody = itemView.findViewById(R.id.body);
            tvHashTag = itemView.findViewById(R.id.hash_tag);
        }
    }
}
