package com.mobitrack.mobi.ui.newExpenses.transactionHistory;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Head;
import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.ui.newExpenses.BaseFragment;
import com.mobitrack.mobi.ui.newExpenses.helper.head.HeadDialogFragment;
import com.mobitrack.mobi.ui.newExpenses.helper.head.HeadListener;
import com.mobitrack.mobi.ui.newExpenses.helper.transaction.TransactionDialogFragment;
import com.mobitrack.mobi.utility.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionHistoryFragment extends BaseFragment implements View.OnClickListener,HistoryContract.View,
        HeadListener {

    private Button btnHead,btnTransaction;

    //private List<Head> headList;

    private RecyclerView mRecyclerView;
    private TranAdapter adapter;

    private FrameLayout mDeleteContainer;
    private TextView tvCancel,tvDelete;

    private HistoryPresenter mPresenter;

    private ProgressBar mProgressBar;

    private Tran deleteTran;
    public TransactionHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = new HistoryPresenter(this);
        this.adapter = new TranAdapter(this);

        Log.d("HHHHH",getHeadList().size()+"");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btnHead = view.findViewById(R.id.head);
        btnTransaction = view.findViewById(R.id.transaction);
        btnHead.setOnClickListener(this);
        btnTransaction.setOnClickListener(this);

        mDeleteContainer = view.findViewById(R.id.delete_container);
        tvCancel = view.findViewById(R.id.cancel);
        tvDelete = view.findViewById(R.id.delete);
        mProgressBar = view.findViewById(R.id.progressBar);

        tvDelete.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        if(getHeadList().size()==0){
            mPresenter.hideTransactionButton();
        }

        mRecyclerView = view.findViewById(R.id.rv_transaction);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);

       // mPresenter.getAllHeads();

        mPresenter.initializeData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head:
                HeadDialogFragment headDialogFragment = new HeadDialogFragment();
                headDialogFragment.setCancelable(false);
                headDialogFragment.show(getChildFragmentManager(),"JJJJJ");
                break;

            case R.id.transaction:
               showForm(null);
                break;

            case R.id.cancel:
                mPresenter.hideDeleteContainer();
                break;

            case R.id.delete:
                mPresenter.deleteTransaction(deleteTran);
                break;
        }
    }


    @Override
    public void initData() {
        for (Tran x: getTransactions()){
            adapter.addTransaction(x);
        }
    }

    @Override
    public void hideTransactionButton() {
        btnTransaction.setVisibility(View.GONE);
    }

    @Override
    public void showTransactionButton() {
        btnTransaction.setVisibility(View.VISIBLE);
    }

   /* @Override
    public void setDataList(List<Tran> tranList) {
        // Log.d("HMMMMM",tranList.size()+"   JJJ");
        adapter.clear();
        for (Tran x: tranList){
            adapter.addTransaction(x);
        }
    }*/


    @Override
    public void deletedTransaction(Tran tran) {
        hideDeleteContainer();
        removeTransaction(tran);
        adapter.removeItem(tran);

    }


    @Override
    public void onItemClick(Tran tran) {
        Log.d("YYYY",tran.getPurpose());
        showForm(tran);
    }

    @Override
    public void onItemLongClick(Tran tran) {
        this.deleteTran = tran;
        mDeleteContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDeleteContainer() {
        mDeleteContainer.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onHeadInserted(Head head) {
        addHead(head);
        //this.headList.add(head);
        mPresenter.showTransactionButton();
    }

    @Override
    public void transactionAdded(Tran transaction) {
        addTransaction(transaction);
        adapter.addTransaction(transaction);
        mRecyclerView.getLayoutManager().scrollToPosition(0);
    }

    @Override
    public void transactionUpdated(Tran transaction) {
        updateTransaction(transaction);
        adapter.updateTransaction(transaction);
    }

    private void showForm(Tran tran){
        TransactionDialogFragment transactionDialogFragment = new TransactionDialogFragment();
        transactionDialogFragment.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TRANSACTION,tran);
        transactionDialogFragment.setArguments(bundle);

        transactionDialogFragment.show(getChildFragmentManager(),"TTTT");
    }
}
