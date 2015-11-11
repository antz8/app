package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collection;
import java.util.logging.SimpleFormatter;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import xmpp.Connect;
import xmpp.XMPPLogic;

import com.example.p_talk.R;

import database.DBAdapter;

import action.Chats_Action;
import activity.MenuTabActivity;
import alarm.AlarmManagerHelper;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class Login extends Activity {
	String username, password;
	private XMPPConnection connection;
	EditText user, pass;
	TextView err;

	Button btnLogin;

	Context context;

	DBAdapter db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		this.context = this;

		db = new DBAdapter(this);

		String tampilan = "yyyy-MM-dd HH:mm:ss";

		// SimpleDateFormat formatLamaSewa = new
		// SimpleDateFormat(tampilanLamaSewa);
		SimpleDateFormat format = new SimpleDateFormat(tampilan);

		Date currentDate = new Date(System.currentTimeMillis());

		String tanggal = format.format(currentDate);

		Log.i("Date", "Tanggal" + tanggal.substring(0, 10));
		tanggal.substring(0, 10);
		Log.i("Date", "Date" + currentDate.getDate());
		Log.i("Date", "Day" + currentDate.getDay());
		Log.i("Date", "Hours" + currentDate.getHours());
		Log.i("Date", "Minutes" + currentDate.getMinutes());
		Log.i("Date", "Minutes" + currentDate);

		user = (EditText) findViewById(R.id.login_Username);
		pass = (EditText) findViewById(R.id.login_NoTelepon);
		err = (TextView) findViewById(R.id.login_erorr);

		btnLogin = (Button) findViewById(R.id.login_btnLogin);

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (user.getText().toString().equals("")) {
					err.setText("Username Harus diisi");
					err.setVisibility(View.VISIBLE);
				} else if (pass.getText().toString().equals("")) {
					err.setText("Password Harus diisi");
					err.setVisibility(View.VISIBLE);
				} else {

					setConnection();
					if (connection != null) {
						XMPPLogic.getInstance().setConnection(connection);
					}
					db.open();
					db.insertLogin(username, password);
					db.close();
				}

			}
		});
	}

	public void setConnection() {

		Log.i("XMPPClient", "onCreate called");

		username = user.getText().toString();
		password = pass.getText().toString();

		// Create a connection
		ConnectionConfiguration connConfig = new ConnectionConfiguration(
				XMPPLogic.getInstance().getHost(), Integer.parseInt(XMPPLogic
						.getInstance().getPort()), XMPPLogic.getInstance()
						.getService());

		connection = new XMPPConnection(connConfig);

		try {
			connection.connect();
			Log.i("XMPPClient",
					"[SettingsDialog] Connected to " + connection.getHost());
		} catch (XMPPException ex) {
			Log.e("XMPPClient", "[SettingsDialog] Failed to connect to "
					+ connection.getHost());
			Log.e("XMPPClient", ex.toString());

		}
		try {

			connection.login(username, password);

			Log.i("XMPPClient", "Logged in as " + connection.getUser());

			// Set the status to available
			Presence presence = new Presence(Presence.Type.available);
			connection.sendPacket(presence);
			Roster roster = connection.getRoster();
			roster.addRosterListener(new RosterListener() {
				// Ignored events public void entriesAdded(Collection<String>
				// addresses) {}
				public void entriesDeleted(Collection<String> addresses) {
				}

				public void entriesUpdated(Collection<String> addresses) {
				}

				public void presenceChanged(Presence presence) {

					Log.i("Friend", "Presence changed: " + presence.getFrom()
							+ " " + presence);
					// Log.i("Friend","Presence changed: " + presence);
				}

				@Override
				public void entriesAdded(Collection<String> arg0) {
					// TODO Auto-generated method stub
				}
			});

			finish();
			Intent i = getIntent();// new
									// Intent(Login.this,MenuTabActivity.class);
			i.setClass(getApplicationContext(), MenuTabActivity.class);
			startActivity(i);
			user.setText("");
			pass.setText("");

			Intent alarm2 = new Intent(this.context, AlarmManagerHelper.class);
			boolean alarmRunning2 = (PendingIntent.getBroadcast(this.context,
					0, alarm2, PendingIntent.FLAG_NO_CREATE) != null);
			if (alarmRunning2 == false) {
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						this.context, 0, alarm2, 0);
				AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
						SystemClock.elapsedRealtime(), 3000, pendingIntent);
			}

		} catch (XMPPException ex) {
			err.setText("Kesalahan Kombinasi Username dan Password");
			err.setVisibility(View.VISIBLE);
			Log.e("XMPPClient", "[SettingsDialog] Failed to log in as "
					+ username);
			Log.e("XMPPClient", ex.toString());

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// Log.d(this.getClass().getName(), "back button pressed");
			// Toast.makeText(getApplicationContext(), "Tidak Bisa Back",
			// Toast.LENGTH_LONG).show();

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
