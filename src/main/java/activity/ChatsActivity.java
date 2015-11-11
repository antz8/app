package activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.jivesoftware.smack.XMPPConnection;

import service.BackgroundService;

import xmpp.XMPPLogic;

import com.example.p_talk.R;

import database.DBAdapter;

import action.Chats_Action;
import adapter.Chats_Adapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import android.view.View.OnClickListener;

public class ChatsActivity extends Activity {
	HashMap<String, String> hash;
	ListView lv;
	ArrayList<HashMap<String, String>> array;

	XMPPConnection connection;
	 BackgroundService b = new BackgroundService();

	DBAdapter db;

	String nim[] = { "6301122194", "6301122195", "6301122196", "6301122197",
			"6301122198", "6301122199", "6301122191", "6301122192",
			"6301122193", "6301122190" };

	String nama[] = { "Mike Lewis", "L Messi", "Chelsea Islan", "Maudi Ayunda",
			"Sule", "Andre", "Zaskia Sungkar", "Tias Mirasih", "Mario T",
			"Bebi Romeo" };

	String chat[] = { "Hahaha", "Yoi Bro", "Iyaa", "Dikostan nih", "Ntar aja",
			"Oke", "Dimana?", "Sipppp", "Supeerrr", "Jam 9 aja" };

	Dialog popup;
	Button ya, tidak;
	TextView pesanHapus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chats);

		MenuTabActivity myTabs = (MenuTabActivity) this.getParent();
		myTabs.setTitle("History Chats");
		array = new ArrayList<HashMap<String, String>>();

		lv = (ListView) findViewById(R.id.list_chats);
		connection = XMPPLogic.getInstance().getConnection();

		db = new DBAdapter(this);

		//listChats();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				String recipient = array.get(position).get("name").toString();
				Intent intent = new Intent(ChatsActivity.this,
						Chats_Action.class);
				intent.putExtra("recipient_chat", recipient);
				startActivity(intent);

			}
		});
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String user = array.get(arg2).get("name");
				formPopup(user);
				return false;
			}
		});

	}

	public void listChats() {

		// db = new DBAdapter(this);

		db.open();

		Cursor c = db.getAllListChat(connection.getUser());

		if (c.moveToFirst()) {
			do {
				hash = new HashMap<String, String>();
				hash.put("name", c.getString(c.getColumnIndex("recipient"))
						.toString());
				hash.put("nim",getChatakhir(c.getString(c.getColumnIndex("recipient"))));
				array.add(hash);
				Log.i("Jumlah ",
						"User "
								+ c.getString(c.getColumnIndex("recipient"))
										.toString());
				// do what ever you want here
			} while (c.moveToNext());
		}

		db.close();

		Chats_Adapter adapter = new Chats_Adapter(this.getBaseContext(), array);
		lv.setAdapter(adapter);

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
	
	public void deleteChat(String username){
		db.open();
		db.deleteChat(username);
		db.close();
	}
	public void formPopup(final String username) {

		popup = new Dialog(this, android.R.style.Theme_Translucent);
		popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popup.setCancelable(true);
		popup.setContentView(R.layout.popup_konfirmasi_hapus);

		ya = (Button) popup.findViewById(R.id.konfirmasi_ya);
		tidak = (Button) popup.findViewById(R.id.konfirmasi_tidak);
		pesanHapus = (TextView) popup.findViewById(R.id.pesan_hapus);
		
		pesanHapus.setText("Anda yakin akan menghapus chat :"+username+"?");

		ya.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteChat(username);
				popup.dismiss();
				array.clear();
				listChats();
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
	
	public String getChatakhir (String rec){
		String chat="";
		db.open();
		db.getChat(rec);
		
		
		Cursor c = db.getChat(rec);

		if (c.moveToFirst()) {
			do {
				if (c.getString(c.getColumnIndex("msg_recipient")).toString()
						.equals("0")) {
					
				} else {
					chat = c.getString(c.getColumnIndex("msg_recipient")).toString();
				}

				if (c.getString(c.getColumnIndex("msg_user")).toString()
						.equals("0")) {
					
				} else {
					chat = c.getString(c.getColumnIndex("msg_user")).toString();
				}

				// do what ever you want here
			} while (c.moveToNext());
		}
			
			db.close();
			
			return chat.substring(0,6);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		array.clear();
	    listChats();
	    MenuTabActivity myTabs = (MenuTabActivity) this.getParent();
		myTabs.setTitle("History Chats");
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
//			
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
}
