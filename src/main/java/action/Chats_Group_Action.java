package action;

import java.util.ArrayList;

import java.util.HashMap;

import org.jivesoftware.smack.PacketListener;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xmpp.XMPPLogic;
//import org.jivesoftware.smackx.muc.MultiUserChat;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.example.p_talk.R;

import adapter.Anggota_Adapter;
import adapter.Chats_Action_Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Chats_Group_Action extends Activity {


	TextView txtnama, txtidd;
	EditText send_text;
	ImageButton send;

	HashMap<String, String> hash;
	ListView lv,lv2;
	ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();

	ArrayList<HashMap<String, String>> array2 = new ArrayList<HashMap<String, String>>();
	String mNickName;
	String mGroupChatName;

	private MultiUserChat mMultiUserChat ;
	// private XMPPConnection mXmppConnection;
	// private ConnectionConfiguration mConnectionConfiguration;

	private XMPPConnection connection;
	private Handler mHandler = new Handler();
	
	Form form;
	Dialog popup;
	String jum;
	AQuery aq;

	String naturalName = "";
	String nameroom = "";
	
	PacketFilter filter = new MessageTypeFilter(Message.Type.groupchat);
	
	Message msg = new Message();
	private boolean consumedIntent;
	private final String SAVED_INSTANCE_STATE_CONSUMED_INTENT = "SAVED_INSTANCE_STATE_CONSUMED_INTENT";

	
	
	ImageButton btnProfile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chats_action);
//		if( savedInstanceState != null ) {
//            consumedIntent = savedInstanceState.getBoolean(SAVED_INSTANCE_STATE_CONSUMED_INTENT);
//        }
		
		aq = new AQuery(this);
		txtnama = (TextView) findViewById(R.id.chats_actionName);
		txtidd = (TextView) findViewById(R.id.chats_actionNim);
		send = (ImageButton) findViewById(R.id.chats_Action_Send);
		send_text = (EditText) findViewById(R.id.chat_action_formmessage);

		btnProfile = (ImageButton)findViewById(R.id.btn_ProfileChat);
		
		getParameter();

		connection = XMPPLogic.getInstance().getConnection();
		setConnection();

		
		lv = (ListView) findViewById(R.id.list_chats_action);
		
		// Pengaturan edittext

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(send_text, InputMethodManager.SHOW_IMPLICIT);
		
		
		// Untuk 
		

		// Untuk Close
		// imm.hideSoftInputFromWindow(send_text.getWindowToken(), 0)

		
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String text = send_text.getText().toString();
				String to = txtidd.getText().toString();

				Log.i("XMPPClient", "Sending text [" + text + "] to [" + to
						+ "]");
				
				msg = new Message(nameroom+"@conference.ptalk",
						Message.Type.groupchat);
				msg.setBody(text);

				mMultiUserChat = new MultiUserChat(connection, nameroom
						+ "@conference.ptalk");
				connection.sendPacket(msg);
			
					

				send_text.setText("");
			}
		});
		
		btnProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			formPopupAnggota(txtnama.getText().toString());	
			}
		});
	}

	public void listChat(ArrayList<HashMap<String, String>> array) {
		Chats_Action_Adapter adapter = new Chats_Action_Adapter(
				this.getBaseContext(), array);
		lv.setAdapter(adapter);
	}

	private void getParameter() {
		Bundle extra = getIntent().getExtras();

		if (extra != null) {
			nameroom = extra.getString("nameroom");
			naturalName = extra.getString("naturalname");
			Log.i("Natural name", "Name :" + naturalName);
		}

		txtnama.setText(naturalName);
		asyncJumGroup(naturalName);
		
	}

	
	public void setConnection() {

    	//connection = this.connection ;
		if (connection != null) {

			try {
				if (nameroom != ""){
					mMultiUserChat = new MultiUserChat(connection, nameroom+"@conference.ptalk");
//					mMultiUserChat = new MultiUserChat(connection, nameroom+"@conference.ptalk");
					Log.i("Msg 1", "Lewat");
					mMultiUserChat.join(connection.getUser());
					Log.i("Msg 1", "Lewat2");
				}
//				mMultiUserChat = new MultiUserChat(connection, nameroom+"@conference.ptalk");
////				mMultiUserChat = new MultiUserChat(connection, nameroom+"@conference.ptalk");
//				Log.i("Msg 1", "Lewat");
//
//				mMultiUserChat.join(connection.getUser());
//				Log.i("Msg 1", "Lewat2");

			} catch (XMPPException e) {
				e.printStackTrace();
			}

			//PacketFilter filter = new MessageTypeFilter(Message.Type.groupchat);
			connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					if (message.getBody() != null) {
						String fromName = message.getFrom();// StringUtils.parseBareAddress(message.getFrom());
						Log.i("XMPPClient", "Got text [" + message.getBody()
								+ "] from [" + fromName + "]");
						// Pesan dari teman
						hash = new HashMap<String, String>();
						// Log.i("from :","From :"+"alprodasar@conference.ptalk/"+mMultiUserChat.getNickname());

						if (fromName.equals(nameroom + "@conference.ptalk/"
								+ Chats_Group_Action.this.connection.getUser())) {
							hash.put("recipient", "0");
							hash.put("user", Chats_Group_Action.this.connection
									.getUser());
							hash.put("message", message.getBody());
							array.add(hash);
							
							mHandler.post(new Runnable() {
								public void run() {
									listChat(array);
								}
							});
							
							// Add the incoming message to the list view
							
						} else {
							hash.put("recipient", fromName);
							hash.put("user", "0");
							hash.put("message", message.getBody());
							array.add(hash);
						
							mHandler.post(new Runnable() {
								public void run() {
									listChat(array);
								}
							});	

						}

					}
				}
			}, filter);
		}

	}

	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		this.finish();
		mMultiUserChat.leave();
		
		
		//connection = null;
		//connection.disconnect();
		//setConnection(null);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	
	

	  @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putBoolean(SAVED_INSTANCE_STATE_CONSUMED_INTENT, consumedIntent);
	    }

//	    @Override
//	    protected void onResume() {
//	        super.onResume();
////	        connection = XMPPLogic.getInstance().getConnection();
////	        setConnection();
//	        //check if this intent should run your code
//	        //for example, check the Intent action
//	        boolean shouldThisIntentTriggerMyCode = true;
//	        Intent intent = getIntent();
//	        boolean launchedFromHistory = intent != null ? (intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0 : false;
//	        if( !launchedFromHistory && shouldThisIntentTriggerMyCode && !consumedIntent ) {
//	            consumedIntent = true;
//	            //execute the code that should be executed if the activity was not launched from history
//	        }
//	    }
	    
	    @Override
	    protected void onNewIntent(Intent intent) {
	        super.onNewIntent(intent);
	        setIntent(intent);
	        consumedIntent = false;
	    }
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//			// Log.d(this.getClass().getName(), "back button pressed");
//			// Toast.makeText(getApplicationContext(), "Tidak Bisa Back",
//			// Toast.LENGTH_LONG).show();
//			//setConnection(null);
//			connection.disconnect();
//			Intent i = new Intent(Chats_Group_Action.this,
//					MenuTabActivity.class);
//			startActivity(i);
//			
//			
//			//onDestroy();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	    public void asyncJumGroup(String username) {
			String url = "http://" + XMPPLogic.getInstance().getHost()
					+ ":8112/ptalk/ptalk/jum_group&"
					+ naturalName+"&a";
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
				//row = 0;
				Document doc = Jsoup.parse(txt, "UTF-8");
				Elements itm = doc.getElementsByTag("row");

				for (Element e : itm) {

					jum = e.getElementsByTag("jum").html();

				}

				if (jum != null) {
//					formPopupKonfirm(name, username);
					txtidd.setText(jum);
				}

			} else {
				Log.d("tes", "null");
			}
		}

	
		public void asyncAnggotaGroup(String naturalname) {
			String url = "http://" + XMPPLogic.getInstance().getHost()
					+ ":8112/ptalk/ptalk/anggota&"+naturalname+"&a";
			// String url = "http://localhost:8112/akbar/json/jadwal/asal";
			aq.ajax(url, String.class, this, "Output");
		}

		public void Output(String url, String txt, AjaxStatus status)
				throws JSONException {
			if (txt != null) {
				// JSONArray listMahasiswa = txt.getJSONArray("");
				Document doc = Jsoup.parse(txt, "UTF-8");
				Elements itm = doc.getElementsByTag("row");

				for (Element e : itm) {

					Elements child = e.getAllElements();
					hash = new HashMap<String, String>();

					for (Element childitem : child) {
						// Log.i("Nama", ""+childitem.nodeName());
						if (childitem.nodeName().equals("nickname")) {
							hash.put("name", childitem.text());

						}
						if (childitem.nodeName().equals("jid")) {
							hash.put("idd", childitem.text().replace("@ptalk",""));

						}
					}

					array2.add(hash);
				}

				Anggota_Adapter adapter = new Anggota_Adapter(this.getBaseContext(),
						array2);
				
				lv2.setAdapter(adapter);

			} else {
				Log.d("tes", "null");
			}
		}
		
		public void formPopupAnggota(String naturalName) {

			popup = new Dialog(this, android.R.style.Theme_Translucent);
			popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
			popup.setCancelable(true);
			popup.setContentView(R.layout.anggota_group);

			lv2 = (ListView)popup.findViewById(R.id.list_anggota);
			
			asyncAnggotaGroup(naturalName);
			
			popup.show();

		}

	
}
