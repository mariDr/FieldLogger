package com.marikris.fieldlogger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FragCon extends Fragment {
	// UI references.
		private EditText mConduct;
		private EditText mPh;
		private EditText mMoist;
		private EditText mOxygen;
		List<LogEntry> listEntry = null;
		LogAdapter logAdapter;
		View focusView = null;;

		public FragCon() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_control, container, false);
			return rootView;
		}

		@Override
		public void onStart() {
			super.onStart();
			// Initialize variables
			mConduct = (EditText) getActivity().findViewById(R.id.editConduct);
			mPh = (EditText) getActivity().findViewById(R.id.editPh);
			mMoist = (EditText) getActivity().findViewById(R.id.editMoist);
			mOxygen = (EditText) getActivity().findViewById(R.id.editOxygen);

			Button saveLog = (Button) getActivity().findViewById(R.id.saveButton);
			saveLog.setOnClickListener(onClick);
			Button showLog = (Button) getActivity().findViewById(R.id.showButton);
			showLog.setOnClickListener(onClick);
			logAdapter = new DbOperations(getActivity(), "control");
			logAdapter.createDB();
			logAdapter.openDB();
			logAdapter.close();
		}

		private OnClickListener onClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.saveButton:
					attemptSave();
					break;
				case R.id.showButton:
					attemptShow();
					break;
				}
			}
		};

		@SuppressLint("SimpleDateFormat")
		public void attemptSave() {
			// reset errors
			mConduct.setError(null);
			mPh.setError(null);
			mMoist.setError(null);
			mOxygen.setError(null);

			float conduct = 0;
			float ph = 0;
			int moist = 0;
			int oxygen = 0;
			// get current time and date
			Calendar today = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// store values
			try {
			String conductString = mConduct.getText().toString();
			String phString = mPh.getText().toString();
			String moistString = mMoist.getText().toString();
			String oxygenString = mOxygen.getText().toString();

			String currentDate = formatter.format(today.getTime());
			conduct = Float.parseFloat(conductString);
			ph = Float.parseFloat(phString);
			moist = Integer.parseInt(moistString);
			oxygen = Integer.parseInt(oxygenString);

			if (isValid(conduct, ph, moist, oxygen)) {
				LogEntry logEntry = new LogEntry(currentDate, conduct, ph, moist, oxygen);
				if (isExist("control")) {
					logAdapter.getList();
					logAdapter.addList(logEntry);
				} else {
					logAdapter.getDatabase();
					logAdapter.addList(logEntry);
				}
				Toast.makeText(getActivity(), "Entry saved.", Toast.LENGTH_LONG).show();
				mConduct.setText("");
				mPh.setText("");
				mMoist.setText("");
				mOxygen.setText("");
				mConduct.requestFocus();
			} else {
				Toast.makeText(getActivity(), "Entry not saved due to conversion error/s.Fix them and try again.",
						Toast.LENGTH_LONG).show();
				focusView.requestFocus();
			}
			}catch(NumberFormatException e){
				Toast.makeText(getActivity(), "Fields cannot be empty. Please try again!", Toast.LENGTH_LONG).show();
			}
		}

		public boolean isValid(float c, float p, int m, int o) {
			boolean isCValid = false;
			boolean isPValid = false;
			boolean isMValid = false;
			boolean isOValid = false;

			if (c >= 0 && c <= 1000) {
				isCValid = true;
			} else
				mConduct.setError(getString(R.string.error_invalid_conductivity));
			focusView = mConduct;
			if (p >= 0 && p <= 14) {
				isPValid = true;
			} else
				mPh.setError(getString(R.string.error_invalid_ph));
			focusView = mPh;
			if (m >= 0 && m <= 100) {
				isMValid = true;
			} else
				mMoist.setError(getString(R.string.error_invalid_moisture));
			focusView = mMoist;
			if (o >= 0 && o <= 100) {
				isOValid = true;
			} else
				mOxygen.setError(getString(R.string.error_invalid_oxygen));
			focusView = mOxygen;
			if (isCValid && isPValid && isMValid && isOValid)
				return true;
			else
				return false;
		}

		public boolean isExist(String fileName) {
			File file = getActivity().getFileStreamPath(fileName);
			return file.exists();
		}

		public void attemptShow() {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			DisplayList display = new DisplayList();
			Bundle args = new Bundle();
			args.putInt("btn", 5);
			display.setArguments(args);
			ft.replace(R.id.container_main, display);
			ft.addToBackStack(null);
			ft.commit();
		}
}
