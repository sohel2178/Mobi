package com.mobitrack.mobi.ui.admin.customers;



import com.mobitrack.mobi.model.User;

import java.util.List;

public interface CustomerContract {

    interface Presenter{
        void requestForAllUser();
        void filterUser(List<User> userList, String filterText);
        void deleteUser(User user);

    }

    interface View{
        void updateUserList(List<User> userList);
        void addUser(User user);
        void startCustomerActivity(User user);
        void deleteUser(User user);
        void showToastAndRemove(User user,String message);
    }
}
