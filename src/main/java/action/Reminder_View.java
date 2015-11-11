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
import adapter.Bapp_Adapter;
import adapter.Reminder_Adapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class Reminder_View extends Activity {

	HashMap<String, String> hash;
    ListView lv;
    ArrayList<HashMap<String, String>> array;
    
    String judul [] = {"TP Praktikum Alpro 1","TA Praktikum Alpro 2","TP Praktikum Alpro 3","TA Praktikum Alpro 4"
    		,"TP Praktikum Alpro 5","TA Praktikum Alpro 6","TP Praktikum Alpro 7","TA Praktikum Alpro 8","TP Praktikum Alpro 9"
    		,"TA Praktikum Alpro 10"};
    
    String tanggal [] = {"1 April 2015","8 April 2015","15 April 2015","22 April 2015","29 April 2015","1 April 2015"
    		,"6 Mei 2015","13 Mei 2015","20 Mei 2015","27 Mei 2015"};
    
    Dialog popup;
    
    String txtJudul = "";
    String txtTanggal_mulai = "";
    String txtTanggal_selesai = "";
    String txtJamMulai = "";
    String txtJamSelesai = "";
    String txtCaption = "";
    String txtId = "";
    
    TextView d_judul,d_tanggal_mulai,d_tanggal_selesai,mulai,selesai,caption;
    Button edit,delete;
    
    private AQuery aq;
    
    String st;
    
    String id_p;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_view);
		
		getParameter();
		
		array = new ArrayList<HashMap<String,String>>();
		
		aq = new AQuery(this);
		
		lv = (ListView)findViewById(R.id.reminder_list);
		
		//listReminder();
		asyncViewReminder();
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				txtJudul = array.get(position).get("judul");
				txtTanggal_mulai = array.get(position).get("tanggal_mulai");
				txtTanggal_selesai = array.get(position).get("tanggal_selesai");
				txtJamMulai = array.get(position).get("jam_mulai");
				txtJamSelesai = array.get(position).get("jam_selesai");
				txtCaption =array.get(position).get("keterangan");
				txtId =array.get(position).get("id");
				
				formPopup();
				
				
				
				
			}
		});
        
		
	}
	

		
		 public void formPopup(){
				
				popup = new Dialog(this, android.R.style.Theme_Translucent);
				popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
				popup.setCancelable(true);
				popup.setContentView(R.layout.reminder_detail);
				
				d_judul = (TextView)popup.findViewById(R.id.reminder_detailJudul);
				d_tanggal_mulai = (TextView)popup.findViewById(R.id.reminder_DetailTanggal);
				mulai = (TextView)popup.findViewById(R.id.reminder_detailJamMulai);
				selesai = (TextView)popup.findViewById(R.id.reminder_detailJamSelesai);
				caption = (TextView)popup.findViewById(R.id.reminder_detailCaption);
				
				d_judul.setText(txtJudul);
				d_tanggal_mulai.setText(txtTanggal_mulai);
				mulai.setText(txtJamMulai);
				selesai.setText(txtJamSelesai);
				caption.setText(txtCaption);
				
				edit = (Button)popup.findViewById(R.id.reminder_ButEdit);
				delete = (Button)popup.findViewById(R.id.reminder_ButClose);
				
				delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//popup.dismiss();
						async_DeleteReminder(txtId);
						popup.dismiss();
						array.clear();
						asyncViewReminder();
						
					}
				});
				
				edit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					Intent i = new Intent(Reminder_View.this,Reminder_Action.class);
					i.putExtra("id", txtId);
					i.putExtra("judul", txtJudul);
					i.putExtra("jam_mulai", txtJamMulai);
					i.putExtra("jam_selesai", txtJamSelesai);
					i.putExtra("tanggal_mulai", txtTanggal_mulai);
					i.putExtra("tanggal_selesai", txtTanggal_selesai);
					i.putExtra("keterangan", txtCaption);
					i.putExtra("st", "edit");
					i.putExtra("id_p", id_p);
					startActivity(i);
					popup.dismiss();
					finish();
					}
				});
				
				popup.show();
				
		 }

		 public void asyncViewReminder() {
				String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/reminder/reminderbypraktikum&"+id_p+"&a&a";
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

							if (childitem.nodeName().equals("id")) {
								hash.put("id", childitem.text());
								Log.i("Modul", childitem.text());
							}
							if (childitem.nodeName().equals("judul")) {
								hash.put("judul", childitem.text());
								Log.i("Modul", childitem.text());
							}
							
							if (childitem.nodeName().equals("tanggal_mulai")) {
								hash.put("tanggal_mulai", childitem.text());
								Log.i("Tanggal", childitem.text());
							}
							if (childitem.nodeName().equals("tanggal_selesai")) {
								hash.put("tanggal_selesai", childitem.text());
								Log.i("Tanggal", childitem.text());
							}
							if (childitem.nodeName().equals("jam_mulai")) {
								hash.put("jam_mulai", childitem.text());
								Log.i("Tanggal", childitem.text());
							}
							if (childitem.nodeName().equals("jam_selesai")) {
								hash.put("jam_selesai", childitem.text());
								Log.i("Tanggal", childitem.text());
							}
							if (childitem.nodeName().equals("keterangan")) {
								hash.put("keterangan", childitem.text());
								Log.i("Tanggal", childitem.text());
							}
						}
						i++;
						array.add(hash);

					}

					Reminder_Adapter adapter = new Reminder_Adapter(this.getBaseContext(),
							array);
					lv.setAdapter(adapter);

				} else {
					Log.d("tes", "null");
				}
			}
	
			
			private void getParameter() {
				Bundle extra = getIntent().getExtras();

				if (extra != null) {
					if (extra.getString("id_p") != null) {
						id_p = extra.getString("id_p"); // Id Praktikum
					}
					
				}
			}
			
			
		 @Override
		    public boolean onOptionsItemSelected(MenuItem item) 
		    {    
		       switch (item.getItemId()) 
		       {        
		          case android.R.id.home:            
		             Intent intent = new Intent(this, MenuTabActivity.class);            
		             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		             startActivity(intent);            
		             return true;        
		          default:            
		             return super.onOptionsItemSelected(item);    
		       }
		    }
		 
		 public void async_DeleteReminder(String id) {
				String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/reminder/delete&a&a&a";

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
			public void onBackPressed() {
				finish();
			
			}

}
