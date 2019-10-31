package com.mobitrack.mobi.ui.newExpenses.transactionSummery.headwise;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mobitrack.mobi.R;

import java.util.ArrayList;
import java.util.List;

public class HeadAdapter extends RecyclerView.Adapter<HeadAdapter.HeadHolder> {

    private List<MyHead> myHeadList;
    private LayoutInflater inflater;

    public HeadAdapter(Fragment fragment) {
        this.inflater = LayoutInflater.from(fragment.getContext());
        myHeadList = new ArrayList<>();
    }

    @NonNull
    @Override
    public HeadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_head,parent,false);

        return new HeadHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeadHolder holder, int position) {

        MyHead myHead = myHeadList.get(position);
        holder.bind(myHead);

    }

    @Override
    public int getItemCount() {
        return myHeadList.size();
    }

    public void addItem(MyHead myHead){
        this.myHeadList.add(myHead);
        int position = myHeadList.indexOf(myHead);
        notifyItemInserted(position);
    }

    public void clearAdapter(){
        this.myHeadList.clear();
        notifyDataSetChanged();
    }

    class HeadHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvTotal;

        public HeadHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            tvTotal = itemView.findViewById(R.id.total);
        }

        public void bind(MyHead myHead){
            tvName.setText(myHead.getName());
            tvTotal.setText(String.valueOf(myHead.getTotal()));
        }
    }
}
