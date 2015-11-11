package action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xmpp.XMPPLogic;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.example.p_talk.R;

import activity.MenuTabActivity;
import adapter.Friends_Adapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Friends_Invitation extends Activity {

	ListView lv;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();;
	HashMap<String, String> hash;



	int row;

	private XMPPConnection connection;

	Dialog popup;
	AQuery aq;

	String name, username;

	TextView nicknameK, user;

	Button konfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.received_invitation);

		lv = (ListView) findViewById(R.id.listInvitation);

		connection = XMPPLogic.getInstance().getConnection();
		aq = new AQuery(this);

		listFriends2();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				username = array.get(arg2).get("idd");
				asyncNamaUser(username);

			}
		});
	}

	public void listFriends2() {

		Roster roster = connection.getRoster();

		Collection<RosterEntry> entries = roster.getEntries();

		for (RosterEntry entry : entries) {

			hash = new HashMap<String, String>();
			if (entry.getName() == null) {
				hash.put("name", "New Friend");
				hash.put("idd", entry.getUser());
				array.add(hash);
			}
			// hash.put("name", entry.getName());
			// Log.i("Roster ", "Roster "+entry.getName());

			Presence entryPresence = roster.getPresence(entry.getUser());

			Log.i("XMPPChatDemoActivity",
					"--------------------------------------");
			Log.i("XMPPChatDemoActivity", "RosterEntry " + entry);
			Log.i("XMPPChatDemoActivity", "User: " + entry.getUser());
			Log.i("XMPPChatDemoActivity", "Name: " + entry.getName());
			Log.i("XMPPChatDemoActivity", "Status: " + entry.getStatus());
			Log.i("XMPPChatDemoActivity", "Type: " + entry.getType());
			// Presence entryPresence = roster.getPresence(entry
			// .getUser());

			Log.i("XMPPChatDemoActivity",
					"Presence Status: " + entryPresence.getStatus());
			Log.i("XMPPChatDemoActivity",
					"Presence Type: " + entryPresence.getType());

			Presence.Type type = entryPresence.getType();
			if (type == Presence.Type.available)
				Log.i("XMPPChatDemoActivity", "Presence AVAILABLE");
			Log.i("XMPPChatDemoActivity", "Presence : " + entryPresence);
		}

		Friends_Adapter adapter = new Friends_Adapter(this.getBaseContext(),
				array);
		lv.setAdapter(adapter);

	}

	public void formPopupKonfirm(String nama, String username) {

		popup = new Dialog(this, android.R.style.Theme_Translucent);
		popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popup.setCancelable(true);
		popup.setContentView(R.layout.friends_konfirmasi);

		nicknameK = (TextView) popup.findViewById(R.id.konfirmasi_nama);
		user = (TextView) popup.findViewById(R.id.konfirmasi_id);

		konfirm = (Button) popup.findViewById(R.id.friends_btnKonfirmasi);

		nicknameK.setText(nama);
		user.setText(username);

		konfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addUser(user.getText().toString().replace("@ptalk", ""),
						nicknameK.getText().toString(), "Friends");
				array.clear();
				listFriends2();
				popup.dismiss();
			}
		});
		popup.show();

	}

	public boolean addUser(String userName, String name, String groupName) {
		if (connection == null)
			return false;
		try {
			Presence subscription = new Presence(Presence.Type.subscribed);
			subscription.setTo(userName);
			userName += "@" + connection.getServiceName();

			connection.sendPacket(subscription);
			connection.getRoster().createEntry(userName, name,
					new String[] { groupName });
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void asyncNamaUser(String username) {
		String url = "http://" + XMPPLogic.getInstance().getHost()
				+ ":8112/ptalk/ptalk/nama_user&"
				+ username.replace("@ptalk", "").toString()+"&a";
		// String url =
		// "http://10.0.3.2:8112/ptalk/praktikum/"+password+"&"+nama_mk+"&"+kelas;
		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		// aq.progress(makeProgressDialog("Loading..")).ajax(url,String.class,this,"OutputPemesanan");
		aq.ajax(url, String.class, this, "OutputName");
	}

	public void OutputName(String url, String txt, AjaxStatus status)
			throws JSONException {
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");
			row = 0;
			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");

			for (Element e : itm) {

				name = e.getElementsByTag("name").html();

			}

			if (name != null) {
				formPopupKonfirm(name, username);
			}

		} else {
			Log.d("tes", "null");
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		Intent i = new Intent(Friends_Invitation.this,MenuTabActivity.class);
		startActivity(i);
	}
}
