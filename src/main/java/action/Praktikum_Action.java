package action;

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
import activity.PraktikumActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Praktikum_Action extends Activity {

	ImageButton btnReminder, btnBapp, btnPercentage;
	String id_p = ""; // Id Praktikum
	int row;
	String kelas, nama_mk, st;

	Spinner spinMataKuliah, spinKelas;

	private AQuery aq;
	String arrMk[];
	String arrKelas[] = {"Pilih Kelas"};

	String kode_asprak;

	ArrayAdapter Adkelas;
	ArrayAdapter AdMk;

	Button prak_submit;
	
	
	TextView txtBapp,txtReminder,txtPersentase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.praktikum);

		getParameter();

		
		btnReminder = (ImageButton) findViewById(R.id.praktikum_BtnReminder);
		btnBapp = (ImageButton) findViewById(R.id.praktikum_BtnBap);
		btnPercentage = (ImageButton) findViewById(R.id.praktikum_BtnPersentase);
		txtBapp = (TextView)findViewById(R.id.txtBap);
		txtReminder = (TextView)findViewById(R.id.txtReminder);
		txtPersentase = (TextView)findViewById(R.id.txtPercentage);
		
		invisibleMenu();
		
		
		prak_submit = (Button)findViewById(R.id.praktikum_Submit);

		
		spinMataKuliah = (Spinner) findViewById(R.id.spinner_MataKuliah);
		spinKelas = (Spinner) findViewById(R.id.spinner_Kelas);
		// id_p = (TextView)findViewById(R.id.prak_Id_p);
		prak_submit.setEnabled(false);
		
		Adkelas = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
				arrKelas);

		Adkelas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinKelas.setAdapter(Adkelas);

		spinKelas.setEnabled(false);

		aq = new AQuery(this);

		asyncMataKuliah(kode_asprak);

		spinMataKuliah.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				String nama_mk = spinMataKuliah.getSelectedItem().toString();
				if (nama_mk != "Pilih MK") {
					asyncKelas(nama_mk);
					spinKelas.setEnabled(true);
				} else {
					spinKelas.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				spinKelas.setEnabled(false);
			}

		});
		
		spinKelas.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String kelas = spinKelas.getSelectedItem().toString();
				if (kelas != "Pilih Kelas") {
					
					prak_submit.setEnabled(true);
				} else {
					prak_submit.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		// Aksi Btn Reminder
		btnReminder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Praktikum_Action.this,
						Reminder_Action.class);
				i.putExtra("id_p", id_p);
				i.putExtra("st", "baru");
				startActivity(i);

			}
		});

		btnBapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Praktikum_Action.this, Bapp_Action.class);
				i.putExtra("id_p", id_p);
				i.putExtra("st", "baru");
				i.putExtra("nama_mk", nama_mk);
				i.putExtra("kelas", kelas);
				Log.i("Ini Id", "" + i);
				startActivity(i);
			}
		});

		btnPercentage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Praktikum_Action.this,
						Presentase_Action.class);
				i.putExtra("id_p", id_p);
				startActivity(i);
			}
		});
		
		prak_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nama_mk = spinMataKuliah.getSelectedItem().toString();
				kelas = spinKelas.getSelectedItem().toString();
				asyncIdPraktikum(kode_asprak, nama_mk, kelas, v);
				
				
				visibleMenu();
				
			}
		});
	}


	private void getParameter() {
		Bundle extra = getIntent().getExtras();

		if (extra != null) {

			kode_asprak = extra.getString("kode_asprak"); // Kode_Asprak

		}

	}

	public void asyncMataKuliah(String kode_asprak) {
		// String url = "http://10.0.3.2:8112/ptalk/praktikum/kode_mk&a&a";
		String url = "http://" + XMPPLogic.getInstance().getHost()
				+ ":8112/ptalk/praktikum/kode_mk&" + kode_asprak + "&a";

		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		aq.ajax(url, String.class, this, "OutputAsal");
	}

	public void OutputAsal(String url, String txt, AjaxStatus status)
			throws JSONException {
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");
			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");
			arrMk = new String[itm.size() + 1];
			int i = 0;
			arrMk[0] = "Pilih MK";
			for (Element e : itm) {

				String nama = e.getElementsByTag("nama_mk").html();

				arrMk[i + 1] = nama;

				i++;

			}

			AdMk = new ArrayAdapter(this,
					android.R.layout.simple_spinner_item, arrMk);

			AdMk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			spinMataKuliah.setAdapter(AdMk);

		} else {
			Log.d("tes", "null");
		}
	}

	public void asyncKelas(String nama_mk) {
		// String url =
		// "http://10.0.3.2:8112/ptalk/praktikum/kelas&"+nama_mk+"&a";
		String url = "http://" + XMPPLogic.getInstance().getHost()
				+ ":8112/ptalk/praktikum/kelas&" + nama_mk + "&" + kode_asprak;

		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		aq.ajax(url, String.class, this, "OutputKelas");
	}

	public void OutputKelas(String url, String txt, AjaxStatus status)
			throws JSONException {
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");
			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");
			arrKelas = new String[itm.size() + 1];
			int i = 0;
			arrKelas[0] = "Pilih Kelas";
			for (Element e : itm) {

				String nama = e.getElementsByTag("nama_kelas").html();

				arrKelas[i + 1] = nama;

				i++;

			}

			ArrayAdapter aa = new ArrayAdapter(this,
					android.R.layout.simple_spinner_item, arrKelas);

			aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			spinKelas.setAdapter(aa);

		} else {
			Log.d("tes", "null");
		}
	}

	public void asyncIdPraktikum(String kode_asprak, String nama_mk,
			String kelas, View v) {
		String url = "http://" + XMPPLogic.getInstance().getHost()
				+ ":8112/ptalk/praktikum/" + kode_asprak + "&" + nama_mk + "&"
				+ kelas;
		// String url =
		// "http://10.0.3.2:8112/ptalk/praktikum/"+password+"&"+nama_mk+"&"+kelas;
		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		// aq.progress(makeProgressDialog("Loading..")).ajax(url,String.class,this,"OutputPemesanan");
		aq.ajax(url, String.class, this, "OutputId");
	}

	public void OutputId(String url, String txt, AjaxStatus status)
			throws JSONException {
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");
			row = 0;
			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");

			id_p = null;

			for (Element e : itm) {

				id_p = e.getElementsByTag("id").html();

			}

		} else {
			Log.d("tes", "null");
		}
	}
	
	public void invisibleMenu(){
		btnBapp.setVisibility(View.INVISIBLE);
		btnPercentage.setVisibility(View.INVISIBLE);
		btnReminder.setVisibility(View.INVISIBLE);
		txtBapp.setVisibility(View.INVISIBLE);
		txtPersentase.setVisibility(View.INVISIBLE);
		txtReminder.setVisibility(View.INVISIBLE);
	}

	public void visibleMenu(){
		btnBapp.setVisibility(View.VISIBLE);
		btnPercentage.setVisibility(View.VISIBLE);
		btnReminder.setVisibility(View.VISIBLE);
		txtBapp.setVisibility(View.VISIBLE);
		txtPersentase.setVisibility(View.VISIBLE);
		txtReminder.setVisibility(View.VISIBLE);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MenuTabActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	
	}
	
	

}
