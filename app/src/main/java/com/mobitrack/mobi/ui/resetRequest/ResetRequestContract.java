package com.mobitrack.mobi.ui.resetRequest;

public interface ResetRequestContract {

    interface Presenter{
        boolean validate(String email);
        void sendResetRequest(String email);
    }

    interface View {
        void clearPreErrors();
        void hideDialog();
        void showDialog();
        void showMessageDialog();

        void showErrorMessage(int fieldId,String message);
    }
}
