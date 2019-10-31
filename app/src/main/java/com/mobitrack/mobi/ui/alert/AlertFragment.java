package com.mobitrack.mobi.ui.alert;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.api.model.Fence;
import com.mobitrack.mobi.ui.main.MainActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertFragment extends Fragment implements AlertContract.View ,RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    RecyclerView rvAlert;

    private AlertPresenter mPresenter;
    private AlartAdapter adapter;


    public AlertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AlertPresenter(this);
        adapter = new AlartAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvAlert = view.findViewById(R.id.rv_alert);
        rvAlert.setLayoutManager(new LinearLayoutManager(getContext()));

        rvAlert.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAlert.setItemAnimator(new DefaultItemAnimator());
        rvAlert.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvAlert.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvAlert);

        mPresenter.requestForAllAlert();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.callForChangeTitle();

    }

    @Override
    public void changeTitle() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.setTitle("Alert");
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateAdapter(List<Fence> fenceList) {
        for(Fence x: fenceList){
            adapter.addItem(x);
        }
    }

    @Override
    public void deleteFenceAlart(int position) {
        adapter.removeItem(position);

        Snackbar snackbar = Snackbar
                .make(getView(), "Deleted Successfully", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo is selected, restore the deleted item
                // adapter.restoreItem(fe, deletedIndex);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        Fence fe = adapter.getFence(position);

        mPresenter.deleteFenceAlart(fe.get_id(),position);




    }
}
