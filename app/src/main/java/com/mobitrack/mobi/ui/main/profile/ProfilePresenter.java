package com.mobitrack.mobi.ui.main.profile;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.mobitrack.mobi.model.User;
import com.mobitrack.mobi.singleton.MyDatabaseRef;
import com.mobitrack.mobi.singleton.MyStorageRef;

public class ProfilePresenter implements ProfileContract.Presenter {
    private ProfileContract.View mView;
    private MyDatabaseRef myDatabaseRef;
    private FirebaseUser mCurrentUser;
    private MyStorageRef myStorageRef;

    public ProfilePresenter(ProfileContract.View mView) {
        this.mView = mView;
        this.myDatabaseRef = MyDatabaseRef.getInstance();
        this.mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.myStorageRef = MyStorageRef.getInstance();
    }

    @Override
    public void requestForCurrentUser() {
        myDatabaseRef.getUserRef().child(mCurrentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if(user!=null){
                            mView.updateUI(user);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void browseClick() {
        mView.openCropImageActivity();
    }

    @Override
    public boolean validate(User user,boolean isImageSelect) {
        mView.clearPreErrors();

        if(user.getName().equals("")){
            mView.showErrorMessage(1,"Empty Field is not Allowed");
            return false;
        }

        if(user.getPhone().equals("")){
            mView.showErrorMessage(2,"Empty Field is not Allowed");
            return false;
        }

        if(user.getAddress().equals("")){
            mView.showErrorMessage(3,"Empty Field is not Allowed");
            return false;
        }

        if(user.getCompanyName().equals("")){
            mView.showErrorMessage(4,"Empty Field is not Allowed");
            return false;
        }

        if(user.getPhotoUri().equals("") && !isImageSelect){
            mView.showErrorMessage(5,"Please Select Profile Photo");
            return false;
        }

        return true;
    }

    @Override
    public void saveUser(User user) {
        myDatabaseRef.getUserRef()
                .child(mCurrentUser.getUid())
                .setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mView.complete();
                    }
                });
    }

    @Override
    public void saveUserWithImage(final User user, byte[] bytes) {
        mView.showDialog();
        myStorageRef.getUserStoreRef().child(mCurrentUser.getUid()).putBytes(bytes)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        user.setPhotoUri(url);
                        mView.hideDialog();
                        saveUser(user);
                    }
                });
    }
}
