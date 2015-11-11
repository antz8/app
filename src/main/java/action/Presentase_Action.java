package action;

import java.util.ArrayList;
import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
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
import adapter.Absen_Adapter2;
import adapter.Presentase_Adapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Presentase_Action extends Activity {
	HashMap<String, String> hash;
	ListView lv;
	ArrayList<HashMap<String, String>> array;

//	String nim[] = { "6301122194", "6301122195", "6301122196", "6301122197",
//			"6301122198", "6301122199", "6301122191", "6301122192",
//			"6301122193", "6301122190" };
//
//	String nama[] = { "Mike Lewis", "L Messi", "Chelsea Islan", "Maudi Ayunda",
//			"Sule", "Andre", "Zaskia Sungkar", "Tias Mirasih", "Mario T",
//			"Bebi Romeo" };

	//int persen[] = { 90, 100, 80, 85, 100, 100, 100, 60, 100, 100 };

	String tnim, tnama;
	
	AQuery aq;
	
	String id_p;
	
	String[] status;
	double [] jumlah;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presentase);

		getParameter();
		array = new ArrayList<HashMap<String, String>>();
		aq = new AQuery(this);

		lv = (ListView) findViewById(R.id.list_presentase);

		//listPresentase();
		asyncMahasiswaPraktikum();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				tnim = array.get(arg2).get("nim");
				tnama = array.get(arg2).get("nama");
				//openChart(tnim, tnama);
				asyncPersentase(tnim, tnama);
			}

		});

	}

//	public void listPresentase() {
//
//		for (int i = 0; i < nama.length; i++) {
//			hash = new HashMap<String, String>();
//			hash.put("nama", nama[i]);
//			hash.put("nim", nim[i]);
//			hash.put("persen", "" + persen[i] + "%");
//			hash.put("no", String.valueOf((i + 1) + "."));
//
//			array.add(hash);
//
//		}
//
//		Presentase_Adapter adapter = new Presentase_Adapter(
//				this.getBaseContext(), array);
//		lv.setAdapter(adapter);
//
//	}

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
			for (Element e : itm) {

				Elements child = e.getAllElements();
				 hash = new HashMap<String, String>();

				for (Element childitem : child) {

					if (childitem.nodeName().equals("nim")) {
						// hash.put("nim", childitem.text());
						hash.put("nim", childitem.text());
						
					}
					if (childitem.nodeName().equals("nama")) {
						// hash.put("name", childitem.text());
						hash.put("nama", childitem.text());
					}
					
					hash.put("persen", " ");
					

					hash.put("no", String.valueOf((i) + "."));
				}
				i++;
				 array.add(hash);

			}

			Presentase_Adapter adapter = new Presentase_Adapter(
					this.getBaseContext(), array);
			lv.setAdapter(adapter);

		} else {
			Log.d("tes", "null");
		}
	}
	
	public void asyncPersentase(String nim,String nama) {

		String url = "http://"+XMPPLogic.getInstance().getHost()+":8112/ptalk/praktikum/persentase&"
				+nim+"&a";
		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		aq.ajax(url, String.class, this, "OutputPersentase");
	}

	public void OutputPersentase(String url, String txt, AjaxStatus status)
			throws JSONException {
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");
			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");

			int i = 0;
			this.status = new String[itm.size()];
			jumlah = new double [itm.size()];
			
			for (Element e : itm) {

					String stat = e.getElementsByTag("status").html();
					this.status[i] = stat;
					String jumlah = e.getElementsByTag("jumlah").html();
					this.jumlah[i] = Double.parseDouble(jumlah);
					
				
				i++;
			//	 array.add(hash);

			}
			
			if (this.status.length>0){
				int[] colors = { Color.BLUE, Color.RED, Color.GREEN };

				// Instantiating CategorySeries to plot Pie Chart
				CategorySeries distributionSeries = new CategorySeries("");

				Log.i("jumlah :", "Jumlah :"+jumlah.length);
				for (int o = 0; o < jumlah.length; o++) {
					// Adding a slice with its values and name to the Pie Chart
					Log.i("jumlah :", "Jumlah :"+jumlah[o]);
					distributionSeries.add((this.status[o]+"-"+this.jumlah[o]).replace(".0", ""), this.jumlah[o]);
				}

				// Instantiating a renderer for the Pie Chart
				DefaultRenderer defaultRenderer = new DefaultRenderer();

				for (int o = 0; o < jumlah.length; o++) {
					SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
					seriesRenderer.setColor(colors[o]);
					seriesRenderer.setDisplayChartValues(true);
					// Adding a renderer for a slice
					defaultRenderer.addSeriesRenderer(seriesRenderer);
				}

				defaultRenderer.setChartTitle("Presentase Praktikan \n Nim :" + tnim
						+ "\n Nama :" + tnama);
				defaultRenderer.setChartTitleTextSize(60);
				defaultRenderer.setBackgroundColor(Color.BLACK);
				defaultRenderer.setLabelsColor(Color.BLACK);
				defaultRenderer.setLabelsTextSize(60);
				defaultRenderer.setAxesColor(Color.BLACK);
				defaultRenderer.setLegendTextSize(60);
				defaultRenderer.setZoomButtonsVisible(true);

				// Creating an intent to plot bar chart using dataset and
				// multipleRenderer
				Intent intent = ChartFactory.getPieChartIntent(getBaseContext(),
						distributionSeries, defaultRenderer, "Presentase Kehadiran");

				
				startActivity(intent);
			}else{
				Log.i("Kosong", "Kosong");
			}

//			Presentase_Adapter adapter = new Presentase_Adapter(
//					this.getBaseContext(), array);
//			lv.setAdapter(adapter);

		} else {
			Log.d("tes", "null");
		}
	}
	private void openChart(String nim, String nama) {

		// Pie Chart Section Names
		

		// Pie Chart Section Value
		
//		asyncPersentase(nim);

		// Color of each Pie Chart Sections
		int[] colors = { Color.BLUE, Color.RED, Color.GREEN };

		// Instantiating CategorySeries to plot Pie Chart
		CategorySeries distributionSeries = new CategorySeries("");

		for (int i = 0; i < jumlah.length; i++) {
			// Adding a slice with its values and name to the Pie Chart
			distributionSeries.add(status[i], jumlah[i]);
		}

		// Instantiating a renderer for the Pie Chart
		DefaultRenderer defaultRenderer = new DefaultRenderer();

		for (int i = 0; i < jumlah.length; i++) {
			SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
			seriesRenderer.setColor(colors[i]);
			seriesRenderer.setDisplayChartValues(true);
			// Adding a renderer for a slice
			defaultRenderer.addSeriesRenderer(seriesRenderer);
		}

		defaultRenderer.setChartTitle("Presentase Praktikan \n Nim :" + nim
				+ "\n Nama :" + nama);
		defaultRenderer.setChartTitleTextSize(30);
		defaultRenderer.setBackgroundColor(Color.BLACK);
		defaultRenderer.setLabelsColor(Color.BLACK);
		defaultRenderer.setLabelsTextSize(30);
		defaultRenderer.setAxesColor(Color.BLACK);
		defaultRenderer.setLegendTextSize(30);
		defaultRenderer.setZoomButtonsVisible(true);

		// Creating an intent to plot bar chart using dataset and
		// multipleRenderer
		Intent intent = ChartFactory.getPieChartIntent(getBaseContext(),
				distributionSeries, defaultRenderer, "Presentase Kehadiran");

		
		startActivity(intent);

	}
	
	private void getParameter() {
		Bundle extra = getIntent().getExtras();

		if (extra != null) {
			if (extra.getString("id_p") != null) {
				id_p = extra.getString("id_p"); // Id Praktikum
			}
		}
	}

	//
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.activity_main, menu);
	// return true;
	// }
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
