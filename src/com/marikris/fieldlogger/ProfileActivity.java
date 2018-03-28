package com.marikris.fieldlogger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.marikris.fieldlogger.MainActivity.PlaceholderFragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A login screen that offers login via user name/password.
 * 
 */
public class ProfileActivity extends Fragment {

	// UI references.
	private EditText mUserView;
	private EditText mPasswordView;
	private EditText mRePassword;
	private Button saveProfile;
	private Button cancelProfile;
	ProfileEntry userProfle;
	Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_profile, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		mUserView = (EditText) getActivity().findViewById(R.id.editUsername);
		mPasswordView = (EditText) getActivity().findViewById(R.id.editPassword1);
		mRePassword = (EditText) getActivity().findViewById(R.id.editPassword2);
		saveProfile = (Button) getActivity().findViewById(R.id.save_profile);
		cancelProfile = (Button) getActivity().findViewById(R.id.cancel_profile);
		// to saved in local variables
		saveProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// retrieve the text entered from the EditText
				String username = mUserView.getText().toString();
				String password = mPasswordView.getText().toString();
				String confirmPass = mRePassword.getText().toString();
				try {

					if (username.equals("") && password.equals("") && confirmPass.equals(""))
						Toast.makeText(getActivity(), "Error: Please complete the fields!", Toast.LENGTH_LONG).show();
					else {
						// validate the password if it matched
						if (password.equals(confirmPass)) {
							userProfle = new ProfileEntry(username, password);
							getUser();
							storeProfile(userProfle);
							Toast.makeText(getActivity(), "Profile Saved Successfully", Toast.LENGTH_SHORT).show();
							mUserView.setText("");
							mPasswordView.setText("");
							mRePassword.setText("");
						} else {
							Toast.makeText(getActivity(), "Password did not match that for the login provided",
									Toast.LENGTH_LONG).show();
						}
					}
				} catch (Exception e) {
				}
			}
		});
		// it will returned to home page immediately
		cancelProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction().replace(R.id.container_main, new PlaceholderFragment())
						.commit();
				MainActivity.current = 0;
			}
		});
	}

	public void storeProfile(ProfileEntry profileList) {
		// to store user data to an internal storage
		try {
			FileOutputStream fileO = getActivity().openFileOutput("userProfile", Context.MODE_PRIVATE);
			ObjectOutputStream objO = new ObjectOutputStream(fileO);
			objO.writeObject(profileList);
			objO.close();
			fileO.close();
		} catch (IOException ioE) {
		}
	}

	public ProfileEntry getUser() {
		// to get user data from internal storage
		userProfle = new ProfileEntry();
		try {
			FileInputStream fileI = getActivity().openFileInput("userProfile");
			ObjectInputStream objI = new ObjectInputStream(fileI);
			userProfle = (ProfileEntry) objI.readObject();

		} catch (IOException ioE) {
		} catch (ClassNotFoundException cnfe) {
		}
		return userProfle;
	}

}