package activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import service.BackgroundService;

import xmpp.XMPPLogic;

import com.example.p_talk.R;

import action.Chats_Action;
import action.Friends_Invitation;
import action.Profile;
import adapter.Friends_Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsActivity extends Activity {

	HashMap<String, String> hash;
	ListView lv;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();

	String nama_action, idd_action;
	

	private XMPPConnection connection;
	 BackgroundService b = new BackgroundService();
	
	Dialog popup;
	Button popup_add,logout;
	
	TextView profilname;
	ImageButton btnProfile;
	
	// Add friends
	EditText username,nickname;
	Button add;
	
	// Konfirm
	
	Button konfirm,btnInvitation;
	
	String u ;
	
	// Delete Friend
	Button ya, tidak;
	TextView pesanHapus;
	
	public FriendsActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends);

		MenuTabActivity myTabs = (MenuTabActivity) this.getParent();
		myTabs.setTitle("Friends");
		// BackgroundService b = new BackgroundService();

		
		
		profilname = (TextView)findViewById(R.id.profil_name);
		btnProfile = (ImageButton)findViewById(R.id.btn_Profile);
		
		

		lv = (ListView) findViewById(R.id.list_friends);

		connection = XMPPLogic.getInstance().getConnection();
		
		profilname.setText(connection.getUser());
		
		popup_add = (Button)findViewById(R.id.btn_PopupAddfriends);
		btnInvitation = (Button)findViewById(R.id.btn_Invitation);
		
		
		btnInvitation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();
			Intent i = new Intent(FriendsActivity.this,Friends_Invitation.class);
			startActivity(i);
			}
		});

		//
		listFriends2();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				nama_action = array.get(position).get("name");
				idd_action = array.get(position).get("idd");
				
				finish();
				Intent i = getIntent();//new Intent(getApplicationContext(), Chats_Action.class);
//				i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				i.setClass(getApplicationContext(), Chats_Action.class);
				i.putExtra("name", nama_action);
				i.putExtra("idd", idd_action);
				startActivity(i);
			}

		});
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String u = array.get(arg2).get("idd");
				formPopup(u.replace("@ptalk", ""));
				return false;
			}
		});
		
//		popup_add.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			formPopupAdd();	
//			}
//		});
		
		btnProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent i = getIntent();
				i.setClass(getApplicationContext(), Profile.class);
				startActivity(i);
			}
		});

		
	}
	
	
//	@Override
//	  public boolean onCreateOptionsMenu(Menu menu) {
//	    MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.menu, menu);
//	    return true;
//	  }
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			Intent intent = new Intent(this, MenuTabActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			return true;
//		case R.id.logout:
//			finish(); // finish activity
//           connection.disconnect();
//	           XMPPLogic.getInstance().setConnection(null);
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}

	
	public void listFriends2() {

		Roster roster = connection.getRoster();

		Collection<RosterEntry> entries = roster.getEntries();
		int j = 0;

		for (RosterEntry entry : entries) {

			
			
			hash = new HashMap<String, String>();
			if (entry.getName() != null){
			hash.put("name", entry.getName());
			hash.put("idd", entry.getUser());
			array.add(hash);
			}else{
				j=j+1;
				
			}
			
			if (j > 0){
				btnInvitation.setVisibility(View.VISIBLE);
				btnInvitation.setText("Invitation("+j+")");
			}else{
				btnInvitation.setVisibility(View.INVISIBLE);
				btnInvitation.setText("");
			}
			
			

			Presence entryPresence = roster.getPresence(entry.getUser());

			// hash.put("status",""+entryPresence.getType());

			
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

	public void setConnection() {

		Log.i("XMPPClient", "onCreate called");

		// String host = "10.91.4.8";
		String host = "192.168.1.122";
		String port = "5222";
		String service = "ptalk";
		String username = "6301122194@ptalk";
		String password = "nggakpake";

		// Create a connection
		ConnectionConfiguration connConfig = new ConnectionConfiguration(host,
				Integer.parseInt(port), service);
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
			if (connection.isAuthenticated() == false) {
				connection.login(username, password);
				Log.i("XMPPClient", "Logged in as " + connection.getUser());

				// Set the status to available
				Presence presence = new Presence(Presence.Type.available);
				connection.sendPacket(presence);

				Roster roster = connection.getRoster();
				roster.addRosterListener(new RosterListener() {
					// Ignored events public void
					// entriesAdded(Collection<String> addresses) {}
					public void entriesDeleted(Collection<String> addresses) {
					}

					public void entriesUpdated(Collection<String> addresses) {
					}

					public void presenceChanged(Presence presence) {

						Log.i("Friend",
								"Presence changed: " + presence.getFrom() + " "
										+ presence);
						// Log.i("Friend","Presence changed: " + presence);
					}

					@Override
					public void entriesAdded(Collection<String> arg0) {
						// TODO Auto-generated method stub

					}

				});
			}

		} catch (XMPPException ex) {
			Log.e("XMPPClient", "[SettingsDialog] Failed to log in as "
					+ username);
			Log.e("XMPPClient", ex.toString());

		}

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
    
    public boolean removeUser(String userName) {  
        if (connection == null)  
            return false;  
        try {  
            RosterEntry entry = null;  
            if (userName.contains("@"))  
                entry = connection.getRoster().getEntry(userName);  
            else 
                entry = connection.getRoster().getEntry(  
                        userName + "@" + connection.getServiceName());  
            if (entry == null)  
                entry = connection.getRoster().getEntry(userName);  
           connection.getRoster().removeEntry(entry);  
   
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  

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
				addUser(username.getText().toString(), nickname.getText().toString(), "Friends");
				array.clear();
				listFriends2();
				popup.dismiss();
			}
		});
		popup.show();

	}
    
   


    private Boolean exit = false;
	@Override
	    public void onBackPressed() {
	        if (exit) {
	            finish(); // finish activity
	            connection.disconnect();
		           XMPPLogic.getInstance().setConnection(null);
//	        	android.os.Process.killProcess(android.os.Process.myPid());
//	            System.exit(1);
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
	            
	          
		           
	          
	        }
	        

	    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//connection = XMPPLogic.getInstance().getConnection();
		array.clear();
		listFriends2();
		MenuTabActivity myTabs = (MenuTabActivity) this.getParent();
		myTabs.setTitle("Friends");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	public void formPopup(final String username) {

		popup = new Dialog(this, android.R.style.Theme_Translucent);
		popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popup.setCancelable(true);
		popup.setContentView(R.layout.popup_konfirmasi_hapus);

		ya = (Button) popup.findViewById(R.id.konfirmasi_ya);
		tidak = (Button) popup.findViewById(R.id.konfirmasi_tidak);
		pesanHapus = (TextView) popup.findViewById(R.id.pesan_hapus);
		
		pesanHapus.setText("Anda yakin akan menghapus :"+username+" dari list friends?");

		ya.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				removeUser(username);
				popup.dismiss();
				array.clear();
				listFriends2();
			}
		});

		tidak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popup.dismiss();
			}
		});

		popup.show();

	}
	
//	@Override
//	  public boolean onCreateOptionsMenu(Menu menu) {
//	    MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.menu, menu);
//	    return true;
//	  }
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			Intent intent = new Intent(this, MenuTabActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			return true;
//		case R.id.adduser:
//			formPopupAdd();
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
	
}
