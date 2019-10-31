package com.mobitrack.mobi.ui.newExpenses.transactionHistory;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.ui.newExpenses.helper.transaction.TranUtil;
import com.mobitrack.mobi.utility.MyUtil;

import java.util.ArrayList;
import java.util.List;

public class TranAdapter extends RecyclerView.Adapter<TranAdapter.TransactionHolder> {

    private List<Tran> transactionList;
    private LayoutInflater inflater;
    private HistoryContract.View mView;

    public TranAdapter(Fragment fragment) {
        this.transactionList = new ArrayList<>();
        this.inflater = LayoutInflater.from(fragment.getContext());
        this.mView = (HistoryContract.View) fragment;
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_transaction,parent,false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        Tran transaction = transactionList.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void addTransaction(Tran transaction){
        transactionList.add(0,transaction);
        notifyItemInserted(0);
    }

    public void updateTransaction(Tran tran){
        int pos = TranUtil.getTransactionPosition(transactionList,tran);
        transactionList.set(pos,tran);
        notifyItemChanged(pos);
    }

    public void removeItem(Tran tran){
        int pos = TranUtil.getTransactionPosition(transactionList,tran);
        transactionList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void clear(){
        transactionList.clear();
        notifyDataSetChanged();
    }


    class TransactionHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView purpose,transaction_date,head,amount;

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);
            head =itemView.findViewById(R.id.head);
            purpose =itemView.findViewById(R.id.purpose);
            transaction_date =itemView.findViewById(R.id.date);
            amount =itemView.findViewById(R.id.amount);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mView.onItemClick(transactionList.get(getAdapterPosition()));
        }

        public void bind(Tran transaction){
            head.setText(transaction.getHead().getName());
            purpose.setText(transaction.getPurpose());
            transaction_date.setText(MyUtil.getStringDate(transaction.getDate()));
            amount.setText(String.valueOf(transaction.getAmount()));
        }

        @Override
        public boolean onLongClick(View view) {
            mView.onItemLongClick(transactionList.get(getAdapterPosition()));
            return true;
        }
    }
}
