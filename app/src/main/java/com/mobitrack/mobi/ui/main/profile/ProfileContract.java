package com.mobitrack.mobi.ui.main.profile;

import com.mobitrack.mobi.model.User;

public interface ProfileContract {

    interface Presenter{
        void requestForCurrentUser();
        void browseClick();
        boolean validate(User user,boolean isImageSelect);
        void saveUser(User user);
        void saveUserWithImage(User user,byte[] bytes);
    }

    interface View{
        void updateUI(User user);
        void openCropImageActivity();
        void showErrorMessage(int fieldId, String message);
        void clearPreErrors();
        void showDialog();
        void hideDialog();
        void complete();
    }
}
