package com.marikris.fieldlogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * This fragment contains a list view with the fields date-time information
 * followed by the corresponding log values
 * 
 */
public class DisplayList extends ListFragment {

	private static final String TAG = "DisplayList";
	ArrayList<String> list = new ArrayList<String>();
	Button returnBtn;
	List<LogEntry> listFieldLogs = null;
	String backBtn = "";
	LogAdapter logAdapter;
	Fragment fragment;

	public DisplayList() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_display_list, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		int btn = bundle.getInt("btn");

		switch (btn) {
		case 1:
			showList("hp1");
			fragment = new FragHp1();
			backBtn = "Return to HP1";
			break;
		case 2:
			showList("hp2");
			fragment = new FragHp2();
			backBtn = "Return to HP2";
			break;
		case 3:
			showList("fo");
			fragment = new FragFo();
			backBtn = "Return to FO";
			break;
		case 4:
			showList("mz");
			fragment = new FragMz();
			backBtn = "Return to MZ";
			break;
		case 5:
			showList("control");
			fragment = new FragCon();
			backBtn = "Return to Control";
			break;
		}
		// to display a list of items contained within an array.
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list));
	}

	public void showList(String logName) {
		logAdapter = new DbOperations(getActivity(), logName);
		listFieldLogs = new ArrayList<LogEntry>();
		if (isExist(logName)) {
			listFieldLogs = logAdapter.getList();
			Log.d(TAG, "Data From Internal Storage");
		} else {
			listFieldLogs = logAdapter.getDatabase();
			Log.d(TAG, "Data From Database");
		}
		// --- add data to list ---
		for (int i = 0; i < listFieldLogs.size(); i++) {
			list.add(i + 1 + ".)  " + listFieldLogs.get(i).getDate() + "\n      " + listFieldLogs.get(i).getConduct() + "   "
					+ listFieldLogs.get(i).getPh() + "   " + listFieldLogs.get(i).getMoist() + "   "
					+ listFieldLogs.get(i).getOxygen());
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// return the activity with which the current fragment is currently
		// associated
		returnBtn = (Button) getActivity().findViewById(R.id.returnBtn);
		returnBtn.setText(backBtn);
		returnBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.container_main, fragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
	}

	// ---check whether the file is exist---
	public boolean isExist(String fileName) {
		File file = getActivity().getFileStreamPath(fileName);
		return file.exists();
	}

}
