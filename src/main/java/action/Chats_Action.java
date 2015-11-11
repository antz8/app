package action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import xmpp.Connect;
import xmpp.XMPPLogic;

import com.example.p_talk.R;

import database.DBAdapter;

import activity.MenuTabActivity;
import adapter.Chats_Action_Adapter;
import adapter.Friends_Adapter;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Chats_Action extends Activity {

	String nama, nim;
	TextView txtnama, txtnim;
	EditText send_text;
	ImageButton send;

	Connect c;

	HashMap<String, String> hash;
	ListView lv;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();

	private XMPPConnection connection;
	private Handler mHandler = new Handler();

	// private final Context CONTEXT = this;

	String recipient_notif;
	String message;
	String recipient_history;
	Date then;

	DBAdapter db = new DBAdapter(this);
	String rec = "";

	PacketFilter filter = new MessageTypeFilter(Message.Type.chat);

	String cekNim = null;
	private boolean consumedIntent;
	private final String SAVED_INSTANCE_STATE_CONSUMED_INTENT = "SAVED_INSTANCE_STATE_CONSUMED_INTENT";

	int a =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chats_action);
		if (savedInstanceState != null) {
			consumedIntent = savedInstanceState
					.getBoolean(SAVED_INSTANCE_STATE_CONSUMED_INTENT);
		}
		connection = XMPPLogic.getInstance().getConnection();

		// array = new ArrayList<HashMap<String, String>>();
		txtnama = (TextView) findViewById(R.id.chats_actionName);
		txtnim = (TextView) findViewById(R.id.chats_actionNim);
		send = (ImageButton) findViewById(R.id.chats_Action_Send);
		send_text = (EditText) findViewById(R.id.chat_action_formmessage);

		lv = (ListView) findViewById(R.id.list_chats_action);

		getParameter();

		getHistoryChat();

		// Pengaturan edittext
		setConnection(connection);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(send_text, InputMethodManager.SHOW_IMPLICIT);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// String to = mRecipient.getText().toString();
				// String text = mSendText.getText().toString();
				Connect c = new Connect();

				String text = send_text.getText().toString();
				String to = txtnim.getText().toString();
				Log.i("XMPPClient", "Sending text [" + text + "] to [" + to
						+ "]");
				Message msg = new Message(to, Message.Type.chat);
				msg.setBody(text);
				connection.sendPacket(msg);
				insertChat(connection.getUser(), to, text, "0");
				getHistoryChat();
				send_text.setText("");

			}
		});

	}

	private void getParameter() {
		Bundle extra = getIntent().getExtras();

		if (extra != null) {
			if (extra.getString("name") != null) {
				nama = extra.getString("name");
				Log.i("nama", "ini nama " + nama);

			}
			if (extra.getString("nim") != null) {
				nim = extra.getString("nim");
				Log.i("nim", "ini nim" + nim);

			}
			if (extra.getString("recipient") != null) {
				recipient_notif = extra.getString("recipient");
				Log.i("rec", "ini rec " + recipient_notif);
			}

			if (extra.getString("recipient_chat") != null) {
				recipient_history = extra.getString("recipient_chat");

			}

			if (extra.getString("message") != null) {
				message = extra.getString("message");
				Log.i("mess", "ini mess " + message);
			}

			if (recipient_notif != null) {
				// onRestart();
				insertChat(Chats_Action.this.connection.getUser(),
						recipient_notif, "0", message);

			}

		}

	}

	public void listChat(ArrayList<HashMap<String, String>> array) {
		Chats_Action_Adapter adapter = new Chats_Action_Adapter(
				this.getBaseContext(), array);
		lv.setAdapter(adapter);
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
		if (connection != null) {
			connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					if (message.getBody() != null) {
						final String fromName = StringUtils
								.parseBareAddress(message.getFrom());
						Log.i("XMPPClient", "Got text [" + message.getBody()
								+ "] from [" + fromName + "]");
						Chats_Action.this.message = message.getBody();
						Log.i("NIM :", "NIM :" + fromName);
						a = a + 1;
						Log.d("Ngetes", "Ngetes"+a);
						insertChat(Chats_Action.this.connection.getUser(),
								fromName, "0", Chats_Action.this.message);
						mHandler.post(new Runnable() {
							public void run() {
								// listChat(array);	
								getHistoryChat();
								Log.i("NIM :", "NIM :" + fromName);
							}
						});

					}

				}
			}, filter);
		}
	}

	public void getHistoryChat() {

		array.clear();
		if (recipient_notif != null) {
			rec = recipient_notif;
			txtnama.setText(nama);
			txtnim.setText(nim);
			setCekNim(txtnim.getText().toString());
			recipient_notif = null;

		} else if (nim != null) {
			rec = nim;
			txtnama.setText(nama);
			txtnim.setText(nim);
			setCekNim(txtnim.getText().toString());

		} else {
			rec = recipient_history;
			txtnama.setText(nama);
			txtnim.setText(recipient_history);
			setCekNim(txtnim.getText().toString());
		}
		db.open();

		Cursor c = db.getChat(rec);

		if (c.moveToFirst()) {
			do {
				then = new Date(c.getString(c.getColumnIndex("time"))
						.toString());
				String sTime = subStract(dateNow(), then);
				hash = new HashMap<String, String>();
				if (c.getString(c.getColumnIndex("msg_recipient")).toString()
						.equals("0")) {
					hash.put("recipient", "0");
				} else {
					hash.put("recipient",
							c.getString(c.getColumnIndex("recipient"))
									.toString());
					hash.put("message",
							c.getString(c.getColumnIndex("msg_recipient"))
									.toString());
					hash.put("time", sTime);
				}

				if (c.getString(c.getColumnIndex("msg_user")).toString()
						.equals("0")) {
					hash.put("user", "0");
				} else {
					hash.put("user", c.getString(c.getColumnIndex("user"))
							.toString());
					hash.put("message",
							c.getString(c.getColumnIndex("msg_user"))
									.toString());
					hash.put("time", sTime);
				}

				array.add(hash);

				// do what ever you want here
			} while (c.moveToNext());

		}

		listChat(array);

		db.close();

	}

	public void insertChat(String user, String rec, String msg_user,
			String msg_rec) {
		db.open();
		db.insertChat(user, rec, msg_user, msg_rec, dateNow().toString());
		db.close();
	}

	public Date dateNow() {

		Date currentDate = new Date(System.currentTimeMillis());

		return currentDate;
	}

	public String subStract(Date now, Date then) {
		long diff = Math.abs(now.getTime() - then.getTime());
		long diffMinute = diff / (60 * 1000);
		long diffHour = diff / (60 * 60 * 1000);
		long diffDays = diff / (24 * 60 * 60 * 1000);
		long diffWeek = diff / (7 * 24 * 60 * 60 * 1000);

		if (diffMinute < 60) {
			return String.valueOf(diffMinute + " minute ago");
		} else if (diffHour < 60) {
			return String.valueOf(diffHour + " hour ago");
		} else if (diffDays < 7) {
			return String.valueOf(diffDays + " day ago");
		} else {
			return String.valueOf(diffWeek + " week ago");
		}

	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	// // Log.d(this.getClass().getName(), "back button pressed");
	// // Toast.makeText(getApplicationContext(), "Tidak Bisa Back",
	// // Toast.LENGTH_LONG).show();
	// Intent i = new Intent(Chats_Action.this, MenuTabActivity.class);
	// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	// i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	// startActivity(i);
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	// // finish();
	// // finishActivity(0);
	// }
	//
	// @Override
	// protected void onRestart() {
	// // TODO Auto-generated method stub
	// super.onRestart();
	// }

	@Override
	public void onBackPressed() {

		// connection.disconnect();
		// onDestroy();
		// finishActivity(0);
		finish();
		// connection.disconnect();
		//connection.addPacketListener(packetListener, packetFilter)
		setConnection(null);
		Intent i = getIntent();
		i.setClass(getApplicationContext(), MenuTabActivity.class);
		startActivity(i);
	}

	// public String getCekNim() {
	// return cekNim;
	// }

	public void setCekNim(String cekNim) {
		this.cekNim = cekNim;
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVED_INSTANCE_STATE_CONSUMED_INTENT,
				consumedIntent);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// check if this intent should run your code
		// for example, check the Intent action
		// boolean shouldThisIntentTriggerMyCode = true;
		// Intent intent = getIntent();
		// boolean launchedFromHistory = intent != null ? (intent.getFlags() &
		// Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0
		// : false;
		// if (!launchedFromHistory && shouldThisIntentTriggerMyCode
		// && !consumedIntent) {
		// consumedIntent = true;
		// // execute the code that should be executed if the activity was not
		// // launched from history
		//
		// }

		// connection = XMPPLogic.getInstance().getConnection();
		// setConnection(connection);

		// array.clear();
		// getHistoryChat();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		consumedIntent = false;
	}

}
