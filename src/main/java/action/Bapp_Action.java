package action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xmpp.XMPPLogic;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.p_talk.R;

import activity.MenuTabActivity;
import adapter.Absen_Adapter;
import adapter.Absen_Adapter2;
import adapter.Bapp_Adapter;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Bapp_Action extends Activity {

	public HashMap<String, String> hash;
	ListView lv;
	public ArrayList<HashMap<String, String>> array;
	Dialog popup;
	Button btnView, btnSendLect, btnCreate, btnErrClose;

	EditText modul, tanggal, jam_mulai, jam_selesai, kode_dosen, asprak,
			keterangan;

	private AQuery aq;

	String err = "";

	RadioGroup rad;

	// Untuk Popup
	Button send,close, btnYa, btnTidak, att;

	EditText email, subject, message;
	
	TextView namaFile, pesanErorr;

	String txtjudul, txttanggal, txtjam_mulai, txtjam_selesai, txtkode_dosen,
			txtasprak, txtket;

	String id_p = null; // id_praktikum
	String hid = null; // Untuk Edit Absen
	String st = null;

	int hour, minute, mYear, mMonth, mDay;

	static final int DATE_DIALOG_ID = 1;

	private String[] arrMonth = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	String[] no, nama, nim, id;
	ArrayList<String> status;
	Context context;

	// Untuk Kirim Email
	String Semail, Ssubject, Smessage, attachmentFile;
	Uri URI = null;
	private static final int PICKFILE_RESULT_CODE = 1;
	int columnIndex;

	String nama_mk, kelas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// View rootView = inflater.inflate(R.layout.fragmentsearch, container,
		// false);
		setContentView(R.layout.bapp);

		getParameter();

		context = this;
		aq = new AQuery(this);

		array = new ArrayList<HashMap<String, String>>();

		this.status = new ArrayList<String>();

		lv = (ListView) findViewById(R.id.list_bapp_mahasiswa);
		setListViewHeightBasedOnChildren(lv);
		
		btnView = (Button)findViewById(R.id.bapp_BtnViewww);
		
		btnCreate = (Button) findViewById(R.id.bapp_btnCreate);
		modul = (EditText) findViewById(R.id.bapp_modul);
		tanggal = (EditText) findViewById(R.id.bapp_date);
		jam_mulai = (EditText) findViewById(R.id.bapp_jammulai);
		jam_selesai = (EditText) findViewById(R.id.bapp_jamselesai);
		kode_dosen = (EditText) findViewById(R.id.bapp_lectureCode);
		asprak = (EditText) findViewById(R.id.bapp_pj);
		keterangan = (EditText) findViewById(R.id.bapp_caption);

		ListMahasiswa();

		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);

		mMonth = c.get(Calendar.MONTH);

		mDay = c.get(Calendar.DAY_OF_MONTH);
		
		hour = c.get(Calendar.HOUR_OF_DAY);

		minute = c.get(Calendar.MINUTE);

		tanggal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(context, mDateSetListener, mYear, mMonth,
						mDay).show();
			}
		});
		
		jam_mulai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new TimePickerDialog(context, mTimesetListener,
						hour, minute, true).show();
			}
		});
		
		jam_selesai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new TimePickerDialog(context, mTimesetListener2,
						hour, minute, true).show();
			}
		});

		btnView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Bapp_Action.this, Bapp_View.class);
				i.putExtra("id_p", id_p);
				i.putExtra("nama_mk", nama_mk);
				i.putExtra("kelas", kelas);
				startActivity(i);
			}
		});

		

		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				asyncCekBapp();
			}
		});

	}

	public void formPopup2() {

		popup = new Dialog(this, android.R.style.Theme_Translucent);
		popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popup.setCancelable(true);
		popup.setContentView(R.layout.bapp_popup_input_absen);

		btnYa = (Button) popup.findViewById(R.id.bapp_btnYa);
		btnTidak = (Button) popup.findViewById(R.id.bapp_btnTidak);

		btnYa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				async_postAbsen_D();
				popup.dismiss();
				resetForm();
			}
		});

		btnTidak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				async_postAbsen_Cancel();
				popup.dismiss();
			}
		});
		popup.show();

	}

	private void getParameter() {
		Bundle extra = getIntent().getExtras();

		if (extra != null) {
			if (extra.getString("id_p") != null) {
				id_p = extra.getString("id_p"); // Id Praktikum
			}
			if (extra.getString("hid") != null) {
				hid = extra.getString("hid"); // HId AbsenD
				id_p = extra.getString("id_p");
			}

			if (extra.getString("modul") != null) {
				txtjudul = extra.getString("modul");
			}

			if (extra.getString("tanggal") != null) {
				txttanggal = extra.getString("tanggal");
			}
			if (extra.getString("jam_mulai") != null) {
				txtjam_mulai = extra.getString("jam_mulai");
			}
			if (extra.getString("jam_selesai") != null) {
				txtjam_selesai = extra.getString("jam_selesai");
			}
			if (extra.getString("kode_dosen") != null) {
				txtkode_dosen = extra.getString("kode_dosen");
			}
			if (extra.getString("asprak") != null) {
				txtasprak = extra.getString("asprak");
			}
			if (extra.getString("keterangan") != null) {
				txtket = extra.getString("keterangan");
			}

			st = extra.getString("st"); // Id Praktikum
			nama_mk = extra.getString("nama_mk");
			kelas = extra.getString("kelas");

		}

	}

	public void asyncMahasiswaPraktikum() {

		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/mahasiswapraktikum/"
				+ id_p;
		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		aq.ajax(url, String.class, this, "Output");
	}

	public void Output(String url, String txt, AjaxStatus status)
			throws JSONException {
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");
			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");

			int i = 1;
			no = new String[itm.size()];
			nim = new String[itm.size()];
			nama = new String[itm.size()];

			for (Element e : itm) {

				Elements child = e.getAllElements();
				// hash = new HashMap<String, String>();

				for (Element childitem : child) {

					if (childitem.nodeName().equals("nim")) {
						// hash.put("nim", childitem.text());
						nim[i - 1] = childitem.text();
					}
					if (childitem.nodeName().equals("nama")) {
						// hash.put("name", childitem.text());
						nama[i - 1] = childitem.text();
					}

					no[i - 1] = "" + i + ".";
					this.status.add("Alfa");

					// hash.put("no", ""+i+".");

					// hash.put("status", "Hadir");

				}
				i++;
				// array.add(hash);

			}

			// Absen_Adapter adapter = new Absen_Adapter(this.getBaseContext(),
			// array);
			// lv.setAdapter(adapter);

			// Absen_Adapter adapter = new Absen_Adapter(Bapp_Action.this,
			// array);
			// lv.setAdapter(adapter);

			// ArrayAdapter<String> adapter2 = new
			// ArrayAdapter<String>(Bapp_Action.this,
			// android.R.layout.simple_spinner_item,
			// getApplicationContext().getResources().getStringArray(R.array.StatusAbsen));
			// ganti dropdown resource
			// adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			Absen_Adapter2 s = new Absen_Adapter2(Bapp_Action.this, no, nim,
					nama, this.status);
			lv.setAdapter(s);

		} else {
			Log.d("tes", "null");
		}
	}

	public void asyncEditAbsen() {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/absend/" + hid;
		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		aq.ajax(url, String.class, this, "OutputAbsen");
	}

	public void OutputAbsen(String url, String txt, AjaxStatus status)
			throws JSONException {
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");
			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");

			int i = 1;
			no = new String[itm.size()];
			nim = new String[itm.size()];
			nama = new String[itm.size()];
			id = new String[itm.size()];

			for (Element e : itm) {

				Elements child = e.getAllElements();
				// hash = new HashMap<String, String>();

				for (Element childitem : child) {

					if (childitem.nodeName().equals("nim")) {
						// hash.put("nim", childitem.text());
						nim[i - 1] = childitem.text();
					}
					if (childitem.nodeName().equals("nama")) {
						// hash.put("name", childitem.text());
						nama[i - 1] = childitem.text();
					}
					if (childitem.nodeName().equals("status")) {
						// hash.put("name", childitem.text());
						this.status.add(childitem.text());
					}
					if (childitem.nodeName().equals("id")) {
						// hash.put("name", childitem.text());
						id[i - 1] = childitem.text();
					}

					no[i - 1] = "" + i + ".";

				}
				i++;
				// array.add(hash);

			}


			Absen_Adapter2 s = new Absen_Adapter2(Bapp_Action.this, no, nim,
					nama, this.status);
			lv.setAdapter(s);

		} else {
			Log.d("tes", "null");
		}
	}

	// Create Bapp

	public void async_postAbsen_H() {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/absenh/insert";

		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject html, AjaxStatus status) {
				System.out.println(html);
			}
		};

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id_p", id_p);
		params.put("modul", modul.getText().toString());
		params.put("tanggal", tanggal.getText().toString());
		params.put("jam_mulai", jam_mulai.getText().toString());
		params.put("jam_selesai", jam_selesai.getText().toString());
		params.put("kode_dosen", kode_dosen.getText().toString());
		params.put("asprak", asprak.getText().toString());
		params.put("keterangan", keterangan.getText().toString());

		cb.params(params);

		aq.ajax(url, params, JSONObject.class, cb);

	}

	public void async_postAbsen_D() {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/absend/insert";

		for (int i = 0; i < nim.length; i++) {
			AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

				@Override
				public void callback(String url, JSONObject html,
						AjaxStatus status) {
					System.out.println(html);
				}
			};

			Map<String, Object> params = new HashMap<String, Object>();

			params.put("id_p", id_p);
			// params.put("nim", array.get(i).get("nim"));
			// params.put("status", array.get(i).get("status"));

			params.put("nim", nim[i]);
			params.put("status", this.status.get(i).toString());

			cb.params(params);

			aq.ajax(url, params, JSONObject.class, cb);

		}
		

	}

	public void async_postAbsen_Cancel() {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/absenh/deletemax";

		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject html, AjaxStatus status) {
				System.out.println(html);
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();
		// Log.i("Id",id);
		params.put("id_p", id_p);
		cb.params(params);

		aq.ajax(url, params, JSONObject.class, cb);

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener =

	new DatePickerDialog.OnDateSetListener()

	{

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mYear = year;

			mMonth = monthOfYear;

			mDay = dayOfMonth;

			String sdate = LPad(mDay + "", "0", 2) + " " + arrMonth[mMonth]
					+ ", " + mYear;

			tanggal.setText(sdate);

		}

	};

	private static String LPad(String schar, String spad, int len) {

		String sret = schar;

		for (int i = sret.length(); i < len; i++) {

			sret = spad + sret;

		}

		return new String(sret);

	}

	public void resetForm() {
		modul.setText("");
		tanggal.setText("");
		jam_mulai.setText("");
		jam_selesai.setText("");
		kode_dosen.setText("");
		asprak.setText("");
		keterangan.setText("");
		asyncMahasiswaPraktikum();
	}

	public void ListMahasiswa() {
		if (st.equals("baru")) {
			asyncMahasiswaPraktikum();
		} else if (st.equals("edit")) {
			asyncEditAbsen();
			modul.setText(txtjudul);
			tanggal.setText(txttanggal);
			jam_mulai.setText(txtjam_mulai);
			jam_selesai.setText(txtjam_selesai);
			kode_dosen.setText(txtkode_dosen);
			asprak.setText(txtasprak);
			keterangan.setText(txtket);
			btnCreate.setText("Edit");
		}
	}

	public void async_postEditAbsen_H() {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/absenh/update";

		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject html, AjaxStatus status) {
				System.out.println(html);
			}
		};

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", hid);
		params.put("modul", modul.getText().toString());
		params.put("tanggal", tanggal.getText().toString());
		params.put("jam_mulai", jam_mulai.getText().toString());
		params.put("jam_selesai", jam_selesai.getText().toString());
		params.put("kode_dosen", kode_dosen.getText().toString());
		params.put("asprak", asprak.getText().toString());
		params.put("keterangan", keterangan.getText().toString());

		cb.params(params);

		aq.ajax(url, params, JSONObject.class, cb);

	}

	public void async_postEditAbsen_D() {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/absend/update";

		for (int i = 0; i < nim.length; i++) {
			AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

				@Override
				public void callback(String url, JSONObject html,
						AjaxStatus status) {
					System.out.println(html);
				}
			};

			Map<String, Object> params = new HashMap<String, Object>();

			params.put("status", this.status.get(i).toString());

			params.put("id", id[i]);
			params.put("nim", nim[i]);

			Log.i("Status :", this.status.get(i).toString());
			Log.i("Hid :", "" + hid);
			Log.i("Nim :", nim[i]);

			cb.params(params);

			aq.ajax(url, params, JSONObject.class, cb);

		}

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

	public boolean validasi() {
		boolean stat = false;

		if (modul.getText().toString().equals("")) {
			err = err + " - Modul Tidak Boleh Kosong";
		}
		if (tanggal.getText().toString().equals("")) {
			err = err + "\n - Tanggal Tidak Boleh Kosong";
		}
		if (jam_mulai.getText().toString().equals("")) {
			err = err + "\n - Jam Mulai Tidak Boleh Kosong";
		}
		if (jam_selesai.getText().toString().equals("")) {
			err = err + "\n - Jam Selesai Tidak Boleh Kosong";
		}
		if (kode_dosen.getText().toString().equals("")) {
			err = err + "\n - Kode Dosen Tidak Boleh Kosong";
		}
		if (asprak.getText().toString().equals("")) {
			err = err + "\n - Asprak Tidak Boleh Kosong";
		}
		if (keterangan.getText().toString().equals("")) {
			err = err + "\n - Caption Tidak Boleh Kosong";
		}

		if (err != "") {
			stat = false;
		} else {
			stat = true;
		}
		return stat;
	}

	public void PopupErorrCreate() {

		popup = new Dialog(this, android.R.style.Theme_Translucent);
		popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popup.setCancelable(true);
		popup.setContentView(R.layout.bapp_popup_inputan);

		btnErrClose = (Button) popup.findViewById(R.id.close_erorr);

		pesanErorr = (TextView) popup.findViewById(R.id.bapp_erorr);

		pesanErorr.setText(err);
		err = "";

		btnErrClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popup.dismiss();
			}
		});

		popup.show();

	}
	
	private TimePickerDialog.OnTimeSetListener mTimesetListener2 =

			new TimePickerDialog.OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					int jam = hourOfDay;
					int min = minute;

					String sTime = jam + ":" + min + ":00";
					jam_selesai.setText(sTime);

				}
			};
			
			private TimePickerDialog.OnTimeSetListener mTimesetListener =

					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							// TODO Auto-generated method stub
							int jam = hourOfDay;
							int min = minute;

							String sTime = jam + ":" + min + ":00";
							jam_mulai.setText(sTime);

						}
					};
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
		return;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
		view = listAdapter.getView(i, view, listView);
		if (i == 0)
		view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
		view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
		totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
		}
	
	public void onBackPressed() {

		// connection.disconnect();
		// onDestroy();
		// finishActivity(0);
		finish();
		// connection.disconnect();
		//connection.addPacketListener(packetListener, packetFilter)
		
	}
	
	// Tambahan Revisi
	public void asyncCekBapp() {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/absenh/" + id_p;
		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		aq.ajax(url, String.class, this, "OutputCek");
	}

	public void OutputCek(String url, String txt, AjaxStatus status)
			throws JSONException {
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");
			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");

			int i = 0;

			String mod="";
			String tang="";
		    String id_pr="";
			for (Element e : itm) {

				Elements child = e.getAllElements();
				hash = new HashMap<String, String>();
				
				

				for (Element childitem : child) {
					
					if (childitem.nodeName().equals("modul")) {
						mod = childitem.text();
					}
					if (childitem.nodeName().equals("tanggal")) {
						tang = childitem.text();
					}
					if (childitem.nodeName().equals("id_p")) {
						id_pr = childitem.text();

					}

					if (modul.getText().toString().equals(mod) && 
							tanggal.getText().toString().equals(tang) &&
							id_p.equals(id_pr)){
						i = i + 1;
					}
				}
			
				array.add(hash);

			}
			
			if (i > 0){
				Toast.makeText(getApplicationContext(), "Data Sudah Ada", Toast.LENGTH_SHORT).show();	
			}else {
				
				if (validasi() == true) {
					if (btnCreate.getText().equals("Create Bapp")) {
						async_postAbsen_H();
						formPopup2();
					} else if (btnCreate.getText().equals("Edit")) {
						async_postEditAbsen_H();
						async_postEditAbsen_D();
						resetForm();
					}

				} else {
					PopupErorrCreate();
					// Toast.makeText(getApplicationContext(), err,
					// Toast.LENGTH_SHORT).show();
					// err= "";
				}
			}

			

		} else {
			Log.d("tes", "null");
		}
	}

}
