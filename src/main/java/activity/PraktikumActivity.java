package activity;

import org.jivesoftware.smack.XMPPConnection;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import service.BackgroundService;

import xmpp.XMPPLogic;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.example.p_talk.R;

import action.Bapp_Action;
import action.Chats_Action;
import action.Praktikum_Action;
import action.Presentase_Action;
import action.Reminder_Action;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.System;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PraktikumActivity extends Activity {

	ImageButton btnReminder, btnBapp, btnPercentage;
	Button login, cancel;
	Dialog popup;
	
	private AQuery aq;
	String arrMk[];
	String arrKelas[];
	// String arrId[];
	//String id_p = "";
	String kd_asprak;
	int row;

	
	// TextView id_p;

	EditText pass,kode_asprak;
	

	String kelas, nama_mk;
	
	private XMPPConnection connection;
	
	 BackgroundService b = new BackgroundService();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.praktikum_login);
		
		MenuTabActivity myTabs = (MenuTabActivity) this.getParent();
		myTabs.setTitle("Praktikum");
		aq = new AQuery(this);
		
		login = (Button) findViewById(R.id.praktikum_btnLogin);
		cancel = (Button)findViewById(R.id.praktikum_BtnBatal);

		connection=XMPPLogic.getInstance().getConnection();

		pass = (EditText) findViewById(R.id.praktikum_password);
		kode_asprak = (EditText) findViewById(R.id.praktikum_Login_kode_mk);

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popup.dismiss();
				Intent i = new Intent(PraktikumActivity.this,
						MenuTabActivity.class);
				startActivity(i);
			}
		});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//nama_mk = spinMataKuliah.getSelectedItem().toString();
				//kelas = spinKelas.getSelectedItem().toString();
				String pas = pass.getText().toString();
				String kode =  kode_asprak.getText().toString();  

				// asyncId(nama_mk, kelas);
				asyncUser(pas, kode,v);
				
				

			}
		});
		

	}


	public void asyncMataKuliah() {
		// String url = "http://10.0.3.2:8112/ptalk/praktikum/kode_mk&a&a";
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/praktikum/kode_mk&a&a";

		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		aq.ajax(url, String.class, this, "OutputAsal");
	}






	public void asyncUser(String password, String kode_asprak,View v) {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/praktikum/login&"+password+"&"+kode_asprak;
		// String url =
		// "http://10.0.3.2:8112/ptalk/praktikum/"+password+"&"+nama_mk+"&"+kelas;
		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		// aq.progress(makeProgressDialog("Loading..")).ajax(url,String.class,this,"OutputPemesanan");
		aq.ajax(url, String.class, this, "OutputUser");
	}

	public void OutputUser(String url, String txt, AjaxStatus status)
			throws JSONException {
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");
			row = 0;
			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");

			kd_asprak = null;

			for (Element e : itm) {

				kd_asprak = e.getElementsByTag("kode_asprak").html();

			}
			
			if (kd_asprak != null) {
				// Log.i("id", "id :"+id);
				
				pass.setText("");
				kode_asprak.setText("");
				Intent i = new Intent(PraktikumActivity.this,Praktikum_Action.class);
				i.putExtra("kode_asprak", kd_asprak);
				startActivity(i);
				
				Toast.makeText(getBaseContext(), "Sukses", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getBaseContext(), "Gagal", Toast.LENGTH_SHORT)
						.show();

			}


		} else {
			Log.d("tes", "null");
		}
	}

	private Boolean exit = false;
	@Override
	    public void onBackPressed() {
	        if (exit) {
	            finish(); // finish activity
	            connection.disconnect();
	            XMPPLogic.getInstance().setConnection(null);
//	        	android.os.Process.killProcess(android.os.Process.myPid());
//	            java.lang.System.exit(1);
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
//	public ProgressDialog makeProgressDialog(String message) {
//		ProgressDialog dialog = new ProgressDialog(this);
//		dialog.setIndeterminate(true);
//		dialog.setCancelable(true);
//		dialog.setInverseBackgroundForced(false);
//		dialog.setCanceledOnTouchOutside(true);
//		dialog.setMessage(message);
//		return dialog;
//	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		connection = XMPPLogic.getInstance().getConnection();
		MenuTabActivity myTabs = (MenuTabActivity) this.getParent();
		myTabs.setTitle("Praktikum");
	}

	

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//			// Log.d(this.getClass().getName(), "back button pressed");
//			// Toast.makeText(getApplicationContext(), "Tidak Bisa Back",
//			// Toast.LENGTH_LONG).show();
//			Intent i = new Intent(PraktikumActivity.this, MenuTabActivity.class);
//			startActivity(i);
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
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
