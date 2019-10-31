package com.mobitrack.mobi.ui.login;

public interface LoginContract {

    interface Presenter{
        boolean validate(String email,String password);
        void signIn(String email,String password);
        void google_click();
        void facebookClick();
        void twitterClick();
        void linkedinClick();
        void phoneClick();
    }

    interface View{
        void googleSignIn();
        void showDialog();
        void hideDialog();
        void handledatabaseError();
        void hideDialogandFinish();
        void showAutheticationFailureToast();

        void showProDialog();
        void hideProDialog();
        void showToastMessage(String message,int fieldId);
        void showVarificationDialog();
        void complete();

        void openFacebookPage();
        void openTwitterPage();
        void openLinkedInPage();
        void callCusmonerCare();

    }
}
