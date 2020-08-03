package com.mr.chatassistant.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mr.chatassistant.R;
import com.mr.chatassistant.StartPage;
import com.mr.chatassistant.core.registration.RegisterContract;
import com.mr.chatassistant.core.registration.RegisterPresenter;
import com.mr.chatassistant.core.users.add.AddUserContract;
import com.mr.chatassistant.core.users.add.AddUserPresenter;
import com.mr.chatassistant.ui.activities.UserListingActivity;
import com.google.firebase.auth.FirebaseUser;



public class RegisterFragment extends Fragment implements View.OnClickListener, RegisterContract.View, AddUserContract.View {
    private static final String TAG = RegisterFragment.class.getSimpleName();

    private RegisterPresenter mRegisterPresenter;
    private AddUserPresenter mAddUserPresenter;

    private EditText mETxtEmail, mETxtPassword;
    private Button mBtnRegister, Verify;

    private ProgressDialog mProgressDialog;

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_register, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mETxtEmail = (EditText) view.findViewById(R.id.edit_text_email_id);
        mETxtPassword = (EditText) view.findViewById(R.id.edit_text_password);
        mBtnRegister = (Button) view.findViewById(R.id.button_register);
        //Verify = (Button) view.findViewById( R.id.button_verify );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mRegisterPresenter = new RegisterPresenter(this);
        mAddUserPresenter = new AddUserPresenter(this);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mBtnRegister.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_register:
                onRegister(view);
                break;
        }
    }


    private void onRegister(View view) {
        String emailId = mETxtEmail.getText().toString();
        String password = mETxtPassword.getText().toString();
        String pattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*+=?-]).{8,15}$";
        if (emailId.length() == 0){
            mETxtEmail.setError( "The field cannot be left blank" );
            return;
        }
        if (password.length() == 0){
            mETxtPassword.setError("The field cannot be left blank" );
            return;
        }
        if (password.length() < 8 || password.length() >15 ) {
            mETxtPassword.setError( "pass too short or too long" );
            return;
        }
        if (!password.matches(".*\\d.*")){
            mETxtPassword.setError("no digits found");
            return;
        }

        if (!password.matches(".*[A-Z].*")) {
            mETxtPassword.setError("no Uppercase letters found");
            return;
        }
        if (!password.matches(".*[!@#$%^&*+=?-].*")) {
            mETxtPassword.setError("no special chars found");
            return;
        }
        /*if (!password.matches( pattern )){
            mETxtPassword.setError( "The length of the password must be between 8 to 15 characters and must contain a uppercase, special character and a number" );
            return;
        }*/


        mRegisterPresenter.register(getActivity(), emailId, password);
        mProgressDialog.show();
    }



    @Override
    public void onRegistrationSuccess(FirebaseUser firebaseUser) {
        mProgressDialog.setMessage(getString(R.string.adding_user_to_db));
        sendEmailVerification();
        Toast.makeText(getActivity(), "Email Sent", Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Registration Successful!", Toast.LENGTH_SHORT).show();
        mAddUserPresenter.addUser(getActivity().getApplicationContext(), firebaseUser);
    }
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener( new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText( getActivity(), "Check your email for verification", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            } );
        }
    }



    @Override
    public void onRegistrationFailure(String message) {
        mProgressDialog.dismiss();
        mProgressDialog.setMessage(getString(R.string.please_wait));
        Log.e(TAG, "onRegistrationFailure: " + message);
        Toast.makeText(getActivity(), "Registration failed!+\n" + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAddUserSuccess(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent( getActivity(), StartPage.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity( intent );
        UserListingActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onAddUserFailure(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
