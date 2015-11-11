package action;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import xmpp.XMPPLogic;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.p_talk.R;

import activity.MenuTabActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.view.View.OnClickListener;

public class Reminder_Action extends Activity {

	ImageButton viewReminder;
	Button create;
	int hour, minute, mYear, mMonth, mDay;

	static final int DATE_DIALOG_ID = 1;

	private String[] arrMonth = { "01", "02", "03", "04", "05", "06", "07",
			"08", "09", "10", "11", "12" };

	EditText tanggal_mulai,tanggal_selesai, jam_mulai,jam_selesai,judul,keterangan;

	TimePicker timeStart;

	Context ctx;
	
	String id_p;
	
	AQuery aq;
	
	String st;
	
	String id;// Untuk Update
	
	String err="";

	Dialog popup;
	
	TextView  pesanErorr;
	
	Button  btnErrClose;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder);

		ctx = this;
		
		viewReminder = (ImageButton) findViewById(R.id.reminder_BtnView);
		create = (Button) findViewById(R.id.reminder_BtnCreate);
		
		tanggal_mulai = (EditText) findViewById(R.id.reminder_tanggal_mulai);
		tanggal_selesai = (EditText) findViewById(R.id.reminder_tanggal_akhir);
		jam_mulai = (EditText) findViewById(R.id.reminder_jammulai);
		jam_selesai= (EditText) findViewById(R.id.reminder_jamselesai);
		judul= (EditText) findViewById(R.id.reminder_title);
		keterangan= (EditText) findViewById(R.id.reminder_caption);
		
		getParameter();
		
		CekEditorInsertorEdit();
		
		aq= new AQuery(this);
	
		// timeStart = (TimePicker)findViewById(R.id.timeStart);
		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);

		mMonth = c.get(Calendar.MONTH);

		mDay = c.get(Calendar.DAY_OF_MONTH);

		hour = c.get(Calendar.HOUR_OF_DAY);

		minute = c.get(Calendar.MINUTE);

		tanggal_mulai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(ctx, mDateSetListener, mYear, mMonth, mDay)
						.show();
			}
		});
		
		tanggal_selesai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(ctx, mDateSetListener2, mYear, mMonth, mDay)
						.show();
			}
		});

		jam_mulai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new TimePickerDialog(ctx, mTimesetListener,
						hour, minute, true).show();
			}
		});
		
		jam_selesai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new TimePickerDialog(ctx, mTimesetListener2,
						hour, minute, true).show();
			}
		});

		viewReminder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.i("Time Start", "Time Start" + timeStart.getCurrentHour());
				Intent i = new Intent(Reminder_Action.this, Reminder_View.class);
				i.putExtra("id_p", id_p);
				startActivity(i);
				finish();

			}
		});
		
		create.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (validasi() == true){
				if (st.equals("baru")){
					async_postReminder();	
				}else if (st.equals("edit")){
					async_EditReminder();
				}
				reset();
				}else{
					PopupErorrCreate();
				}
				
				
			
			}
		});

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

			String sdate = mYear + "-" + arrMonth[mMonth] + "-"
					+ LPad(mDay + "", "0", 2);

			tanggal_mulai.setText(sdate);

		}

	};

	private DatePickerDialog.OnDateSetListener mDateSetListener2 =

			new DatePickerDialog.OnDateSetListener()

			{

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {

					mYear = year;

					mMonth = monthOfYear;

					mDay = dayOfMonth;

					String sdate = mYear + "-" + arrMonth[mMonth] + "-"
							+ LPad(mDay + "", "0", 2);

					tanggal_selesai.setText(sdate);

				}

			};

	private static String LPad(String schar, String spad, int len) {

		String sret = schar;

		for (int i = sret.length(); i < len; i++) {

			sret = spad + sret;

		}

		return new String(sret);

	}

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
			
			public void async_postReminder() {
				String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/reminder/insert&a&a&a";

				AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject html, AjaxStatus status) {
						System.out.println(html);
					}
				};

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id_p", id_p);
				params.put("judul", judul.getText().toString());
				params.put("tanggal_mulai", tanggal_mulai.getText().toString());
				params.put("tanggal_selesai", tanggal_selesai.getText().toString());
				params.put("jam_mulai", jam_mulai.getText().toString());
				params.put("jam_selesai", jam_selesai.getText().toString());
				params.put("keterangan", keterangan.getText().toString());

				cb.params(params);

				aq.ajax(url, params, JSONObject.class, cb);

			}
			
			public void async_EditReminder() {
				String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/reminder/update&a&a&a";

				AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject html, AjaxStatus status) {
						System.out.println(html);
					}
				};

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", id);
				params.put("judul", judul.getText().toString());
				params.put("tanggal_mulai", tanggal_mulai.getText().toString());
				params.put("tanggal_selesai", tanggal_selesai.getText().toString());
				params.put("jam_mulai", jam_mulai.getText().toString());
				params.put("jam_selesai", jam_selesai.getText().toString());
				params.put("keterangan", keterangan.getText().toString());

				cb.params(params);

				aq.ajax(url, params, JSONObject.class, cb);

			}

			private void getParameter() {
				Bundle extra = getIntent().getExtras();

				if (extra != null) {
					if (extra.getString("id_p") != null) {
						id_p = extra.getString("id_p"); // Id Praktikum
					}
					
					if (extra.getString("st") != null) {
						st = extra.getString("st"); // Id Praktikum
					}
				}
			}
			
			public void CekEditorInsertorEdit(){
				if (st.equals("edit")){
					create.setText("Edit Reminder");
					Bundle extra = getIntent().getExtras();

					if (extra != null) {
						if (extra.getString("id") != null) {
							id = extra.getString("id"); // Id Praktikum
						}
						if (extra.getString("judul") != null) {
							judul.setText(extra.getString("judul")); // Id Praktikum
						}
						if (extra.getString("tanggal_mulai") != null) {
							tanggal_mulai.setText(extra.getString("tanggal_mulai")); // Id Praktikum
						}
						if (extra.getString("tanggal_selesai") != null) {
							tanggal_selesai.setText(extra.getString("tanggal_selesai")); // Id Praktikum
						}
						if (extra.getString("jam_mulai") != null) {
							jam_mulai.setText(extra.getString("jam_mulai")); // Id Praktikum
						}
						if (extra.getString("jam_selesai") != null) {
							jam_selesai.setText(extra.getString("jam_selesai")); // Id Praktikum
						}
						if (extra.getString("keterangan") != null) {
							keterangan.setText(extra.getString("keterangan")); // Id Praktikum
						}
					}
					
				}else{
					create.setText("Create Reminder");
				}
				
			}
			
			
			public void reset(){
				judul.setText("");
				tanggal_mulai.setText("");
				tanggal_selesai.setText("");
				jam_mulai.setText("");
				jam_selesai.setText("");
				keterangan.setText("");
			}
			
			public boolean validasi (){
				boolean stat = false;
				if (judul.getText().toString().equals("")){
					err = err + " - Judul Tidak Boleh Kosong";
				}if (tanggal_mulai.getText().toString().equals("")){
					err = err + "\n - Start Date Tidak Boleh Kosong";
				}if (tanggal_selesai.getText().toString().equals("")){
					err = err + "\n - End Date Tidak Boleh Kosong";
				}if (jam_mulai.getText().toString().equals("")){
					err = err + "\n - Start Time Tidak Boleh Kosong";
				}if (jam_selesai.getText().toString().equals("")){
					err = err + "\n - End Time Tidak Boleh Kosong";
				}if (keterangan.getText().toString().equals("")){
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
