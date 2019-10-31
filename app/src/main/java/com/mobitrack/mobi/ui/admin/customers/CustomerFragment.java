package com.mobitrack.mobi.ui.admin.customers;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.ui.admin.AdminActivity;
import com.mobitrack.mobi.ui.customer.CustomerActivity;
import com.mobitrack.mobi.utility.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerFragment extends Fragment implements CustomerContract.View,SearchView.OnQueryTextListener{

    private CustomerPresenter mPresenter;

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;

    private List<User> userList;
    private List<User> filteredList;

    private SearchUserAdapter adapter;



    public CustomerFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new CustomerPresenter(this);
        adapter = new SearchUserAdapter(this);

        userList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mSearchView = view.findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(this);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);


    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() instanceof AdminActivity){
            AdminActivity aa = (AdminActivity) getActivity();
            aa.setTitle("Customer List");
        }

        mPresenter.requestForAllUser();
    }

    @Override
    public void updateUserList(List<User> userList) {
        this.userList = userList;

        adapter.clear();

        for (User x: userList){
            adapter.addUser(x);
        }
    }

    @Override
    public void addUser(User user) {
        adapter.addUser(user);
    }

    @Override
    public void startCustomerActivity(User user) {

        Intent intent = new Intent(getContext(), CustomerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER,user);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void deleteUser(User user) {
        mPresenter.deleteUser(user);
    }

    @Override
    public void showToastAndRemove(User user, String message) {
        adapter.removeUser(user);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.clear();
        mPresenter.filterUser(userList,newText);
        return false;
    }
}
