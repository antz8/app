package activity;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import xmpp.XMPPLogic;

import com.example.p_talk.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;


public class MenuTabActivity extends TabActivity {

	XMPPConnection connection;
	Dialog popup;
	Button add;
	EditText username, nickname;
	
	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menutab);
		
		TabHost tabHost = getTabHost();

		connection = XMPPLogic.getInstance().getConnection();
		// Tab for Friends
		TabSpec friendspec = tabHost.newTabSpec("Friends");
		// setting Title and Icon for the Tab
		
		friendspec.setIndicator("",
				getResources().getDrawable(R.drawable.icon_friends_tab));
		Intent friendsIntent = new Intent(this, FriendsActivity.class);

		friendspec.setContent(friendsIntent);
		

		// Tab for Group
		TabSpec groupspec = tabHost.newTabSpec("Groups");
		groupspec.setIndicator("",
				getResources().getDrawable(R.drawable.icon_groups_tab));
		Intent groupsIntent = new Intent(this, GroupsActivity.class);
		groupspec.setContent(groupsIntent);

		// Tab for Chat
		TabSpec chatspec = tabHost.newTabSpec("Chats");
		chatspec.setIndicator("",
				getResources().getDrawable(R.drawable.icon_chats_tab));
		Intent chatsIntent = new Intent(this, ChatsActivity.class);
		chatspec.setContent(chatsIntent);

		// Tab for Praktikum
		TabSpec prakspec = tabHost.newTabSpec("Praktikum");
		prakspec.setIndicator("",
				getResources().getDrawable(R.drawable.icon_praktikum_tab));
		Intent prakIntent = new Intent(this, PraktikumActivity.class);
		prakspec.setContent(prakIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(friendspec); // Adding friends tab
		tabHost.addTab(groupspec); // Adding groups tab
		tabHost.addTab(chatspec); // Adding chats tab
		tabHost.addTab(prakspec); // Adding praktikum tab
		
				
		
	}

	@Override
	protected void onChildTitleChanged(Activity childActivity,
			CharSequence title) {
		// TODO Auto-generated method stub
		super.onChildTitleChanged(childActivity, title);
	}
	
	
	private Boolean exit = false;

	@Override
	public void onBackPressed() {
		if (exit) {
			// finish(); // finish activity
			android.os.Process.killProcess(android.os.Process.myPid());
			java.lang.System.exit(1);
		} else {
			Toast.makeText(this, "Press Back again to Exit.",
					Toast.LENGTH_SHORT).show();
			exit = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					exit = false;
				}
			}, 3 * 1000);
			// Intent homeIntent = new Intent(Intent.ACTION_MAIN);
			// homeIntent.addCategory( Intent.CATEGORY_HOME );
			// homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(homeIntent);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MenuTabActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.adduser:
			formPopupAdd();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Revisi
	public void formPopupAdd() {

		popup = new Dialog(this, android.R.style.Theme_Translucent);
		popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popup.setCancelable(true);
		popup.setContentView(R.layout.friends_add);

		username = (EditText) popup.findViewById(R.id.friends_addUsername);
		nickname = (EditText) popup.findViewById(R.id.friends_addNickName);

		add = (Button) popup.findViewById(R.id.friends_btnAdd);

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addUser(username.getText().toString(), nickname.getText()
						.toString(), "Friends");
				popup.dismiss();
				Intent i = new Intent(MenuTabActivity.this,
						MenuTabActivity.class);
				startActivity(i);
				finish();
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

}
