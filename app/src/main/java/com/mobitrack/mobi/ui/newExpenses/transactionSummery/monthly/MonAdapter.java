package com.mobitrack.mobi.ui.newExpenses.transactionSummery.monthly;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.utility.MyUtil;

import java.util.ArrayList;
import java.util.List;

public class MonAdapter extends RecyclerView.Adapter<MonAdapter.TransactionHolder> {

    private List<Tran> transactionList;
    private LayoutInflater inflater;

    public MonAdapter(Fragment fragment) {
        this.transactionList = new ArrayList<>();
        this.inflater = LayoutInflater.from(fragment.getContext());
    }


    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_transaction,parent,false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        Tran tran = transactionList.get(position);
        holder.bind(tran);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void addTransaction(Tran transaction){
        transactionList.add(0,transaction);
        notifyItemInserted(0);
    }

    public void clear(){
        transactionList.clear();
        notifyDataSetChanged();
    }

    class TransactionHolder extends RecyclerView.ViewHolder{
        TextView purpose,transaction_date,head,amount;

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);
            head =itemView.findViewById(R.id.head);
            purpose =itemView.findViewById(R.id.purpose);
            transaction_date =itemView.findViewById(R.id.date);
            amount =itemView.findViewById(R.id.amount);

        }

        public void bind(Tran transaction){
            head.setText(transaction.getHead().getName());
            purpose.setText(transaction.getPurpose());
            transaction_date.setText(MyUtil.getStringDate(transaction.getDate()));
            amount.setText(String.valueOf(transaction.getAmount()));
        }
    }
}
