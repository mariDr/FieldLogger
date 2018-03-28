package com.marikris.fieldlogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Intent intent;
	static int current;
	Button preB, nextB, homeB;
	static LogAdapter logAdapter;
	Fragment newfragment;
	List<ProfileEntry> userList = null;
	Context ctx;
	ProfileActivity profileActivity;
	ProfileEntry profileEntry;
	private static final String TAG = "Message";
	ArrayList<String> list = new ArrayList<String>();
	String logName;
	List<LogEntry> listFieldLogs = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container_main, new PlaceholderFragment()).commit();
			current = 0;
		}

		homeB = (Button) findViewById(R.id.homeButton);
		// ---event handler for the home button---
		homeB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.container_main, new PlaceholderFragment());
				ft.addToBackStack(null);
				ft.commit();
				current = 0;
			}
		});

		preB = (Button) findViewById(R.id.prevButton);
		// ---event handler for the previous button---
		preB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				switch (current) {
				case 0:
					newfragment = new FragCon();
					current = 5;
					break;
				case 1:
					newfragment = new PlaceholderFragment();
					current = 0;
					break;
				case 2:
					newfragment = new FragHp1();
					current = 1;
					break;
				case 3:
					newfragment = new FragHp2();
					current = 2;
					break;
				case 4:
					newfragment = new FragFo();
					current = 3;
					break;
				case 5:
					newfragment = new FragMz();
					current = 4;
					break;
				}
				ft.replace(R.id.container_main, newfragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		nextB = (Button) findViewById(R.id.nextButton);
		// ---event handler for the next button---
		nextB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				switch (current) {
				case 0:
					newfragment = new FragHp1();
					current = 1;
					break;
				case 1:
					newfragment = new FragHp2();
					current = 2;
					break;
				case 2:
					newfragment = new FragFo();
					current = 3;
					break;
				case 3:
					newfragment = new FragMz();
					current = 4;
					break;
				case 4:
					newfragment = new FragCon();
					current = 5;
					break;
				case 5:
					newfragment = new PlaceholderFragment();
					current = 0;
					break;
				}
				ft.replace(R.id.container_main, newfragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		});

	}

	// ---to display the fragment buttons---
	public static class PlaceholderFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_buttons, container, false);
			return rootView;
		}

		@Override
		public void onStart() {
			super.onStart();
			Button hp1Btn = (Button) getActivity().findViewById(R.id.hp1button);
			Button hp2Btn = (Button) getActivity().findViewById(R.id.hp2button);
			Button foBtn = (Button) getActivity().findViewById(R.id.fobutton);
			Button mzBtn = (Button) getActivity().findViewById(R.id.mzbutton);
			Button conBtn = (Button) getActivity().findViewById(R.id.controlbutton);
			hp1Btn.setOnClickListener(onClick);
			hp2Btn.setOnClickListener(onClick);
			foBtn.setOnClickListener(onClick);
			mzBtn.setOnClickListener(onClick);
			conBtn.setOnClickListener(onClick);
		}

		// ---create an anonymous class to act as a button click listener---
		private OnClickListener onClick = new OnClickListener() {
			Fragment newfragment;

			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				switch (v.getId()) {
				case R.id.hp1button:
					newfragment = new FragHp1();
					current = 1;
					break;

				case R.id.hp2button:
					newfragment = new FragHp2();
					current = 2;
					break;

				case R.id.fobutton:
					newfragment = new FragFo();
					current = 3;
					break;

				case R.id.mzbutton:
					newfragment = new FragMz();
					current = 4;
					break;

				case R.id.controlbutton:
					newfragment = new FragCon();
					current = 5;
					break;
				}
				ft.replace(R.id.container_main, newfragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		};

	}

	// ---show progress bar when storing data on database---
	private class writeDatabase extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(MainActivity.this, "Writing data..", "Please wait");
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			if (progressDialog != null) {
				progressDialog.dismiss();

			}
			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// data insertion operation in each table name
				logAdapter = new DbOperations(MainActivity.this, "hp1");
				logAdapter.insertEntry();
				logAdapter.close();
				logAdapter = new DbOperations(MainActivity.this, "hp2");
				logAdapter.insertEntry();
				logAdapter.close();
				logAdapter = new DbOperations(MainActivity.this, "fo");
				logAdapter.insertEntry();
				logAdapter.close();
				logAdapter = new DbOperations(MainActivity.this, "mz");
				logAdapter.insertEntry();
				logAdapter.close();
				logAdapter = new DbOperations(MainActivity.this, "control");
				logAdapter.insertEntry();
				logAdapter.close();
				Thread.sleep(5000); // sleep for 5 seconds
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			return null;
		}
	}

	// ---show progress bar when deleting data on database---
	private class deleteDatabase extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(MainActivity.this, "Sending data..", "Please wait");
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// delete all data from hp1, hp2 and so on...
				logAdapter = new DbOperations(MainActivity.this, "hp1");
				logAdapter.deleteEntry();
				logAdapter.close();
				logAdapter = new DbOperations(MainActivity.this, "hp2");
				logAdapter.deleteEntry();
				logAdapter.close();
				logAdapter = new DbOperations(MainActivity.this, "fo");
				logAdapter.deleteEntry();
				logAdapter.close();
				logAdapter = new DbOperations(MainActivity.this, "mz");
				logAdapter.deleteEntry();
				logAdapter.close();
				logAdapter = new DbOperations(MainActivity.this, "control");
				logAdapter.deleteEntry();
				logAdapter.close();
				Thread.sleep(5000); // sleep for 5 seconds
			} catch (InterruptedException e) {

				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (progressDialog != null) {
				progressDialog.dismiss();

			}
			super.onPostExecute(result);
		}

	}

	// ---Options Menu---
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.profile:
			// to run when the profile item is clicked
			getFragmentManager().beginTransaction().replace(R.id.container_main, new ProfileActivity()).commit();
			return true;
		case R.id.save:
			// to run when the save item is clicked
			new writeDatabase().execute();
			return true;
		case R.id.send:

			// to run when the send item is clicked
			// to confirm user before deleting all data
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Exit");
			builder.setMessage("Are you sure? This will delete all entries.");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					String[] to = { "marikrismb@yahoo.com" };
					String[] cc = { "marikrismb@yahoo.com" };
					String msg = getName() + "\n" + getLogs("hp1") + getLogs("hp2") + getLogs("fo") + getLogs("mz") + getLogs("control");

					sendEmail(to, cc, "New Logger Data", msg);
					new deleteDatabase().execute(); // delete all the data after the email sent
				}
			});

			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					dialog.dismiss();
				}
			});

			builder.show();
			break;
		default:
			return true;
		}
		return false;
	}

	// ---sends a message---
	private void sendEmail(String[] emailAddresses, String[] carbonCopies, String subject, String userN) {
		// initialize a new intent
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		String[] to = emailAddresses; // holding e-mail addresses that should be delivered to.
		String[] cc = carbonCopies;

		emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
		emailIntent.putExtra(Intent.EXTRA_CC, cc);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, userN);
		// need this to prompts email client only
		emailIntent.setType("message/rfc822");
		startActivity(Intent.createChooser(emailIntent, "Send Email"));
		
	}

	// ---get the user name---
	public String getName() {
		String user = "";
		try {
			if (checkFile("userProfile")) {
				user = profileActivity.getUser().toString();
				//user = profileEntry.getUsername();
				Log.d(TAG, "Data From Internal Storage");
			}
		} catch (Exception e) {
		}
		return user;

	}

	// ---get all the data of the logs---
	public String getLogs(String logName) {
		String m = "";
		logAdapter = new DbOperations(this, logName);
		listFieldLogs = new ArrayList<LogEntry>();
		if (checkFile(logName)) {
			listFieldLogs = logAdapter.getList();
			Log.d(TAG, "Data From Internal Storage");
		} else {
			listFieldLogs = logAdapter.getDatabase();
			Log.d(TAG, "Data From Database");
		}
		if (!listFieldLogs.isEmpty()) {

			for (int i = 0; i < listFieldLogs.size(); i++) {
				m = m + logName + " - " + (i + 1) + ".)  " + listFieldLogs.get(i).getDate() + "   "
						+ listFieldLogs.get(i).getConduct() + "  " + listFieldLogs.get(i).getPh() + "  "
						+ listFieldLogs.get(i).getMoist() + "  " + listFieldLogs.get(i).getOxygen() + "\n";
			}
		}
		return m;
	}

	@Override
	public void onBackPressed() {
		// exit a fragment by using the tag name of fragment
		checkFile("hp1");
		checkFile("hp2");
		checkFile("fo");
		checkFile("mz");
		checkFile("control");
		// check if the data has been stored to database
		if (!checkFile("hp1") && !checkFile("hp2") && !checkFile("fo") && !checkFile("mz") && !checkFile("control")) {
			finish();
			Log.d(TAG, "File does not exists");
		} else {
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
					.setMessage("Save entries to DB first?")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							new writeDatabase().execute();
							finish();
						}
					}).setNegativeButton("No", null).show();
			Log.d(TAG, "File exists");
		}

	}

	// ---check data on database---
	public boolean checkFile(String fileName) {
		File file = getFileStreamPath(fileName);
		return file.exists();
	}
}
