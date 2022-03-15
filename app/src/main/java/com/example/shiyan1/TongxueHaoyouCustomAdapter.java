package com.example.shiyan1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TongxueHaoyouCustomAdapter extends RecyclerView.Adapter<TongxueHaoyouCustomAdapter.RvViewHolder> {
    Context context;
    ArrayList<TongxueHaoyou> tongxueHaoyous;

    public TongxueHaoyouCustomAdapter(Context context, ArrayList<TongxueHaoyou> tongxueHaoyous) {
        this.context = context;
        this.tongxueHaoyous = tongxueHaoyous;
    }

    View view;

    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.tongxue_haoyou_row, parent, false);
        RvViewHolder rvViewHolder = new RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final TongxueHaoyou tongxueHaoyou = tongxueHaoyous.get(position);
        holder.cb_tongxueHaoyou.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    TongxueHaoyouActivity.haoyouIdCheckedList.add(tongxueHaoyou.getId());
//                    Toast.makeText(view.getContext(), "selected pos:"+position +", getid() : "+tongxueHaoyou.getId(), Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(view.getContext(), "unSelected", Toast.LENGTH_SHORT).show();
                    TongxueHaoyouActivity.haoyouIdCheckedList.removeIf(n -> (n == tongxueHaoyou.getId()));
                }
            }
        });

        if (tongxueHaoyou.getName() != null) {
            holder.tv_tongxueHaoyouName.setText(tongxueHaoyou.getName());
        }
        if (tongxueHaoyou.getPhone() != null) {
            holder.tv_tongxueHaoyouPhone.setText(tongxueHaoyou.getPhone());
        }
    }

    @Override
    public int getItemCount() {
        return tongxueHaoyous.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb_tongxueHaoyou;
        TextView tv_tongxueHaoyouName, tv_tongxueHaoyouPhone;

        public RvViewHolder(View itemView) {
            super(itemView);
            cb_tongxueHaoyou = itemView.findViewById(R.id.cb_tongxueHaoyou);
            tv_tongxueHaoyouName = itemView.findViewById(R.id.tv_tongxueHaoyouName);
            tv_tongxueHaoyouPhone = itemView.findViewById(R.id.tv_tongxueHaoyouPhone);
        }
    }
}
