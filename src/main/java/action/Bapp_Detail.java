package action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import adapter.Absen_Adapter;
import adapter.Bapp_Adapter;
import adapter.Bapp_DetailAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class Bapp_Detail extends Activity {

	HashMap<String, String> hash;
	ListView lv;
	ArrayList<HashMap<String, String>> array;

	private AQuery aq;
	String hid;

	Button edit, convert;

	Button btnSendLect;
	String judul, tanggal, jam_mulai, jam_selesai, kode_dosen, asprak, ket;

	String id = null;
	String id_p = null;

	TextView txtjudul, txttanggal;

	String nama_mk, kelas;
	//Send Email
	Dialog popup;
	Button send,close,att;
	EditText email, subject, message;
	TextView namaFile;
	
	String Semail, Ssubject, Smessage, attachmentFile;
	Uri URI = null;
	private static final int PICKFILE_RESULT_CODE = 1;
	int columnIndex;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bapp_detail);

		aq = new AQuery(this);

		array = new ArrayList<HashMap<String, String>>();

		lv = (ListView) findViewById(R.id.list_detailBapp);
		txtjudul = (TextView) findViewById(R.id.bapp_detailJudul);
		txttanggal = (TextView) findViewById(R.id.bapp_detailTanggal);
		btnSendLect = (Button) findViewById(R.id.bapp_btnSendtoLect);

		edit = (Button) findViewById(R.id.bapp_BtnEdit);
		convert = (Button) findViewById(R.id.bapp_ConvertExcel);

		getParameter();
		// listDetailBapp();
		asyncViewDetail();

		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Bapp_Detail.this, Bapp_Action.class);
				i.putExtra("hid", hid);
				i.putExtra("id_p", id_p);
				i.putExtra("st", "edit");

				i.putExtra("modul", judul);
				i.putExtra("tanggal", tanggal);
				i.putExtra("jam_mulai", jam_mulai);
				i.putExtra("jam_selesai", jam_selesai);
				i.putExtra("kode_dosen", kode_dosen);
				i.putExtra("asprak", asprak);
				i.putExtra("keterangan", ket);

				// Log.i("Ini Id", ""+i);
				startActivity(i);
			}
		});

		convert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConvertToExcel2();
				Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_SHORT).show();
			}
		});

		btnSendLect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//formPopupSendEmail();
				final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.setType("vnd.android.cursor.dir/email");
				startActivity(
				Intent.createChooser(emailIntent,
						"Sending email..."));
			}
		});
	}

	public void asyncViewDetail() {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/absend/" + hid;
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

			for (Element e : itm) {

				Elements child = e.getAllElements();
				hash = new HashMap<String, String>();

				for (Element childitem : child) {

					if (childitem.nodeName().equals("nim")) {
						hash.put("nim", childitem.text());

					}

					if (childitem.nodeName().equals("nama")) {
						hash.put("name", childitem.text());

					}

					if (childitem.nodeName().equals("status")) {
						hash.put("status", childitem.text());

					}

					if (childitem.nodeName().equals("hid")) {
						hash.put("hid", childitem.text());

					}

					hash.put("no", "" + i + ".");

				}
				i++;
				array.add(hash);

			}

			Bapp_DetailAdapter adapter = new Bapp_DetailAdapter(
					this.getBaseContext(), array);
			lv.setAdapter(adapter);

		} else {
			Log.d("tes", "null");
		}
	}

	private void getParameter() {
		Bundle extra = getIntent().getExtras();

		if (extra != null) {
			hid = extra.getString("hid");
			judul = extra.getString("judul");
			tanggal = extra.getString("tanggal");
			jam_mulai = extra.getString("jam_mulai");
			jam_selesai = extra.getString("jam_selesai");
			kode_dosen = extra.getString("kode_dosen");
			asprak = extra.getString("asprak");
			ket = extra.getString("keterangan");
			id_p = extra.getString("id_p");
		}

		txtjudul.setText(judul);
		txttanggal.setText(tanggal);
		nama_mk = extra.getString("nama_mk");
		kelas = extra.getString("kelas");

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

	public void ConvertToExcel2() {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/absend/" + hid;
		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		aq.ajax(url, String.class, this, "Output3");
	}

	public void Output3(String url, String txt, AjaxStatus status)
			throws JSONException, IOException {
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");
			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");
			int i = 1;
			final String fileName = txtjudul.getText() + ".xls";
			// Saving file in external storage
			
			File sdCard = Environment.getExternalStorageDirectory();
//			File directory = new File(sdCard.getAbsolutePath() + "/Android/data/com.ptalk.data/" + nama_mk
//					+ "-" + kelas);
//			
			//"/mnt/extSdCard/"
			
			File directory = new File(sdCard.getAbsolutePath() + "/Praktikum/" + nama_mk
					+ "-" + kelas);
			Log.i("Path ", "Path :"+sdCard.getAbsolutePath());

			if (!directory.isDirectory()) {
				directory.mkdirs();
			}

			// file path
			File file = new File(directory, fileName);

			WorkbookSettings wbSettings = new WorkbookSettings();
			wbSettings.setLocale(new Locale("en", "EN"));
			WritableWorkbook workbook;

			try {
				workbook = Workbook.createWorkbook(file, wbSettings);
				// Excel sheet name. 0 represents first sheet
				WritableSheet sheet = workbook.createSheet("Sheet1", 0);

				try {
					// Header
					sheet.addCell(new Label(0, 0, "Modul :")); // column and row
					sheet.addCell(new Label(1, 0, judul));
					sheet.addCell(new Label(2, 0, "Tanggal :"));
					sheet.addCell(new Label(3, 0, tanggal));
					sheet.addCell(new Label(0, 1, "Jam :")); // column and row
					sheet.addCell(new Label(1, 1, jam_mulai + "-" + jam_selesai));
					sheet.addCell(new Label(0, 2, "Kode Dosen :")); // column
																	// and row
					sheet.addCell(new Label(1, 2, kode_dosen));
					sheet.addCell(new Label(0, 3, "Asprak :")); // column and
																// row
					sheet.addCell(new Label(1, 3, asprak));
					sheet.addCell(new Label(0, 4, "Ket :")); // column and row
					sheet.addCell(new Label(1, 4, ket));

					sheet.addCell(new Label(0, 6, "Nim")); // column and row
					sheet.addCell(new Label(1, 6, "Nama"));
					sheet.addCell(new Label(2, 6, "Status"));
					String nim = "";
					String nama = "";
					String stat = "";
					for (Element e : itm) {

						Elements child = e.getAllElements();

						for (Element childitem : child) {
							if (childitem.nodeName().equals("nim")) {
								nim = childitem.text();
							}

							if (childitem.nodeName().equals("nama")) {
								nama = childitem.text();
							}

							if (childitem.nodeName().equals("status")) {
								stat = childitem.text();

							}

						}

						sheet.addCell(new Label(0, i + 6, nim));
						sheet.addCell(new Label(1, i + 6, nama));
						sheet.addCell(new Label(2, i + 6, stat));
						i++;
					}

					// closing cursor

				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}
				workbook.write();
				try {
					workbook.close();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			Log.d("tes", "null");
		}
	}
	
	public void formPopupSendEmail() {

		popup = new Dialog(this, android.R.style.Theme_Translucent);
		popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popup.setCancelable(true);
		popup.setContentView(R.layout.bapp_popup);

		send = (Button) popup.findViewById(R.id.bapp_BtnSend);
		close = (Button) popup.findViewById(R.id.bapp_BtnClose);
		att = (Button) popup.findViewById(R.id.bapp_BtnFile);

		email = (EditText) popup.findViewById(R.id.bapp_emailDosen);
		subject = (EditText) popup.findViewById(R.id.bapp_Subject);
		message = (EditText) popup.findViewById(R.id.bapp_Message);

		namaFile = (TextView) popup.findViewById(R.id.bapp_TxtNamafile);

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popup.dismiss();
			}
		});

		att.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openGallery();
			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Semail = email.getText().toString();
					Ssubject = subject.getText().toString();
					Smessage = message.getText().toString();

					final Intent emailIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					//emailIntent.setType("vnd.android.cursor.dir/email");
					emailIntent.setType("application/excel");
					// emailIntent.setType("file/*");
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
							new String[] { Semail });
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							Ssubject);
					if (URI != null) {
						emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
					}
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							Smessage);
					
					popup.getContext().startActivity(
							Intent.createChooser(emailIntent,
									"Sending email..."));

				} catch (Throwable t) {
					Toast.makeText(popup.getContext(),
							"Request failed try again: " + t.toString(),
							Toast.LENGTH_LONG).show();
				}
			}
		});
		popup.show();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK) {
			/**
			 * Get Path
			 */
//			 Uri selectedData = data.getData();
//			// String[] filePathColumn = { MediaStore.Images.Media.DATA };
//			 String[] filePathColumn = { MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath() };
//			 Cursor cursor =
//			 getContentResolver().query(selectedData,filePathColumn, null,
//			 null, null);
//			 cursor.moveToFirst();
//			 columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//			 attachmentFile = cursor.getString(columnIndex);
			// Log.e("Attachment Path:", attachmentFile);
			URI = Uri.parse("file:/" + data.getData().getPath());
			//URI = Uri.parse(data.getData().getPath());
			String a = data.getData().getLastPathSegment();
			namaFile.setText(String.valueOf(URI));
			namaFile.setVisibility(View.VISIBLE);
			// Log.i("path ",""+URI);
			// Log.i("path ",""+a);
			// cursor.close();
		}
	}
	public void openGallery() {
		Intent intent = new Intent();
		// intent.setType("image/*");
		intent.setType("file/*");
		//intent.setType("application/excel");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra("return-data", true);
		startActivityForResult(intent,PICKFILE_RESULT_CODE);
//		startActivityForResult(
//				Intent.createChooser(intent, "Complete action using"),
//				PICKFILE_RESULT_CODE);

	}
	

	public void onBackPressed() {

		// connection.disconnect();
		// onDestroy();
		// finishActivity(0);
		finish();
		// connection.disconnect();
		//connection.addPacketListener(packetListener, packetFilter)
		
	}
	
}
