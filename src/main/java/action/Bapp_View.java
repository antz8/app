package action;

import java.util.ArrayList;
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
import adapter.Bapp_Adapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class Bapp_View extends Activity {

	HashMap<String, String> hash;
	ListView lv;
	ArrayList<HashMap<String, String>> array;
	String id_p = "";
	String id = ""; // untuk delete

	String nama_mk, kelas;

	private AQuery aq;

	Dialog popup;

	Button ya, tidak;

	String txtJudul2, TxtTanggal2, txtJam_mulai2, txtJam_selesai2,
			txtkode_dosen2, txtasprak2, txtKet2;
	TextView txtJudul, txtTanggal, txtId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bapp_view);
		array = new ArrayList<HashMap<String, String>>();

		lv = (ListView) findViewById(R.id.list_bapp);
		getParameter();
		aq = new AQuery(this);

		asyncViewBapp();
		// listBapp();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long idl) {
				// TODO Auto-generated method stub
				txtJudul2 = array.get(position).get("judul").toString();
				TxtTanggal2 = array.get(position).get("tanggal").toString();
				txtJam_mulai2 = array.get(position).get("jam_mulai").toString();
				txtJam_selesai2 = array.get(position).get("jam_selesai")
						.toString();
				txtkode_dosen2 = array.get(position).get("kode_dosen")
						.toString();
				txtasprak2 = array.get(position).get("asprak").toString();
				txtKet2 = array.get(position).get("keterangan").toString();
				id = array.get(position).get("id").toString();

				Intent i = new Intent(Bapp_View.this, Bapp_Detail.class);

				i.putExtra("judul", txtJudul2.toString());
				i.putExtra("tanggal", TxtTanggal2.toString());
				i.putExtra("jam_mulai", txtJam_mulai2.toString());
				i.putExtra("jam_selesai", txtJam_selesai2.toString());
				i.putExtra("kode_dosen", txtkode_dosen2.toString());
				i.putExtra("asprak", txtasprak2.toString());
				i.putExtra("keterangan", txtKet2.toString());

				i.putExtra("hid", id);
				i.putExtra("id_p", array.get(position).get("id_p").toString());

				i.putExtra("nama_mk", nama_mk);
				i.putExtra("kelas", kelas);
				// i.putExtra("id", id);
				startActivity(i);

			}

		});

//		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					int position, long arg3) {
//				// TODO Auto-generated method stub
//				id = array.get(position).get("id").toString();
//				formPopup();
//				return true;
//			}
//		});

	}

	public void asyncViewBapp() {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/absenh/" + id_p;
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

					if (childitem.nodeName().equals("modul")) {
						hash.put("judul", childitem.text());
						Log.i("Modul", childitem.text());
					}
					if (childitem.nodeName().equals("tanggal")) {
						hash.put("tanggal", childitem.text());
						Log.i("Tanggal", childitem.text());
					}
					if (childitem.nodeName().equals("id")) {
						hash.put("id", childitem.text());
						Log.i("Id", childitem.text());
					}
					if (childitem.nodeName().equals("jam_mulai")) {
						hash.put("jam_mulai", childitem.text());

					}
					if (childitem.nodeName().equals("jam_selesai")) {
						hash.put("jam_selesai", childitem.text());

					}
					if (childitem.nodeName().equals("kode_dosen")) {
						hash.put("kode_dosen", childitem.text());

					}
					if (childitem.nodeName().equals("asprak")) {
						hash.put("asprak", childitem.text());

					}
					if (childitem.nodeName().equals("keterangan")) {
						hash.put("keterangan", childitem.text());

					}
					if (childitem.nodeName().equals("id_p")) {
						hash.put("id_p", childitem.text());

					}

				}
				i++;
				array.add(hash);

			}

			Bapp_Adapter adapter = new Bapp_Adapter(this.getBaseContext(),
					array);
			lv.setAdapter(adapter);

		} else {
			Log.d("tes", "null");
		}
	}

	private void getParameter() {
		Bundle extra = getIntent().getExtras();

		if (extra != null) {

			id_p = extra.getString("id_p"); // Id Praktikum
			nama_mk = extra.getString("nama_mk");
			kelas = extra.getString("kelas");
		}

	}

	public void formPopup() {

		popup = new Dialog(this, android.R.style.Theme_Translucent);
		popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		popup.setCancelable(true);
		popup.setContentView(R.layout.popup_konfirmasi_hapus);

		ya = (Button) popup.findViewById(R.id.konfirmasi_ya);
		tidak = (Button) popup.findViewById(R.id.konfirmasi_tidak);

		ya.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				async_postDeleteAbsen(id);
				popup.dismiss();
				array.clear();
				asyncViewBapp();
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

	public void async_postDeleteAbsen(String id) {
		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/absenh/delete";

		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject html, AjaxStatus status) {
				System.out.println(html);
			}
		};
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", id);
		cb.params(params);

		aq.ajax(url, params, JSONObject.class, cb);

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
	

	public void onBackPressed() {

		// connection.disconnect();
		// onDestroy();
		// finishActivity(0);
		finish();
		// connection.disconnect();
		//connection.addPacketListener(packetListener, packetFilter)
		
	}
}
