package com.example.chat_simple_application;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chat_simple_application.model.UserModel;
import com.example.chat_simple_application.utils.AndroidUtil;
import com.example.chat_simple_application.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.UploadTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {
    ImageView profile_image;
    EditText username_upd,phone_upd;
    Button update,logout;
    UserModel currentUserModel;
    ProgressBar progbar;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImage;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImage = data.getData();
                            if (getContext() != null) {
                                AndroidUtil.setProfilePic(getContext(), selectedImage, profile_image);
                            }
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        profile_image=view.findViewById(R.id.profile_img);
        username_upd=view.findViewById(R.id.username_edit_text);
        phone_upd=view.findViewById(R.id.phone_edit_txt);
        update=view.findViewById(R.id.profile_update);
        progbar=view.findViewById(R.id.Profile_progress_circular);
        logout=view.findViewById(R.id.logout);


        if (profile_image != null) {

            FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if ((task.isSuccessful())) {
                                Uri uri = task.getResult();
                                if (uri != null) {
                                    AndroidUtil.setProfilePic(getContext(), uri, profile_image);
                                }
                            }
                        }
                    });
        }
        getUserData();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdateProfileButton();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseUtil.logout();
                            Intent intent= new Intent(getContext(),SplashActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });



            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ProfileFragment.this).cropSquare().compress(512).maxResultSize(512,512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLauncher.launch(intent);
                                return null;
                            }
                        });
            }
        });
        
        return view;
    }
    void setUpdateProfileButton(){
        String newUsername= username_upd.getText().toString();
        if (newUsername.isEmpty() || newUsername.length()<3){
            username_upd.setError("username length should be more than 3 characters");
            return;
        }
        if (currentUserModel == null) {
            Log.e("ProfileFragment", "currentUserModel is null");
            return;
        }
        currentUserModel.setUsername(newUsername);
        setInProgress(true);

        if(selectedImage!=null){
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImage)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            updateToFirestore();

                        }
                    });
        }else{
            updateToFirestore();

        }

    }
    void updateToFirestore(){
        FirebaseUtil.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        setInProgress(false);
                        if(task.isSuccessful()){
                            AndroidUtil.showToast(getContext(),"Profile updated");

                        }else{
                            AndroidUtil.showToast(getContext(),"Updated failed");
                        }
                    }
                });

    }

    private void getUserData() {
        setInProgress(true);
        // current profile reference is set above in onCreateView
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
               currentUserModel=task.getResult().toObject(UserModel.class);
               username_upd.setText(currentUserModel.getUsername());
               phone_upd.setText(currentUserModel.getMobile());

            }
        });
    }
    void setInProgress(boolean inProgress){
        if(inProgress) { // true
            progbar.setVisibility(View.VISIBLE);
            update.setVisibility(View.GONE);
        }    else{
            progbar.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
        }

    }

}