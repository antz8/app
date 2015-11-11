package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.p_talk.R;

import action.Bapp_Action;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

@SuppressLint("NewApi") public class Absen_Adapter extends BaseAdapter {
	ArrayList<HashMap<String, String>> arrList;
   // Context context;
    LayoutInflater inflater;
    Activity activity;
    
    TextView bapp_no,bapp_name,bapp_nim;
    RadioGroup radGroup;
    int posisi=0;
    
   // HashMap<String, String> hash;
    
    Bapp_Action b ;
    
    public Absen_Adapter(Activity a ,  ArrayList<HashMap<String, String>> arr) {
		// TODO Auto-generated constructor stub
    	arrList = arr;
    	//context = ctx;
    	activity = a;
    	inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	b =  new Bapp_Action();
    	
    	//b.array = new  ArrayList<HashMap<String,String>>();
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 
		 View v = convertView;
		
		 
		 	final ViewHolder holder;
	        if (v == null) {
//	            v = inflater.inflate(R.layout.bapp_list_mahasiswa, parent, false);
//	            
//	            holder = new ViewHolder();
//	            
//	            holder.radGr = (RadioGroup)v.findViewById(R.id.bapp_radio);
//	            holder.rad1 = (RadioButton)v.findViewById(R.id.rad_Hadir);
//	            holder.rad2 = (RadioButton)v.findViewById(R.id.rad_Alfa);
//	            holder.rad3 = (RadioButton)v.findViewById(R.id.rad_Izin);
//	            
//	            holder.bapp_name = (TextView)v.findViewById(R.id.bapp_listmahasiswa_name);
//		        holder.bapp_nim = (TextView)v.findViewById(R.id.bapp_listmahasiswa_nim);
//		        holder.bapp_no = (TextView)v.findViewById(R.id.bapp_listmahasiswa_no);
//	    
//		        holder.radGr.set
//	            holder.radGr.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//		
//					@Override
//					public void onCheckedChanged(RadioGroup group, int id) {
//						// TODO Auto-generated method stub
//						Log.i("id group", ""+id);	
//						switch (id) {
//							
//							case R.id.rad_Hadir:
//								b.hash = new HashMap<String, String>();
//								b.hash.put("name", arrList.get(position).get("name"));
//						        b.hash.put("nim",arrList.get(position).get("nim"));
//						        b.hash.put("no",arrList.get(position).get("no"));
//						        b.hash.put("status", "Hadir");
//						        Log.i("Posisi", ""+position);
//						        arrList.set(position, b.hash);
//						        Log.i("Hadir", "Hadir");
//								break;
//							case R.id.rad_Alfa:
//								b.hash = new HashMap<String, String>();
//								String name = arrList.get(position).get("name").toString();
//								String nim =arrList.get(position).get("nim").toString();
//								String no = arrList.get(position).get("no").toString();
//								b.hash.put("name", name);
//						        b.hash.put("nim", nim);
//						        b.hash.put("no",no);
//						        b.hash.put("status", "Alfa");
//						        Log.i("Posisi", ""+position);
////						        Log.i("Alfa2", b.hash.get("status"));
////						        Log.i("Alfa2", b.hash.get("name"));
////						        Log.i("Alfa2", b.hash.get("nim"));
//						       // b.array.set(position, b.hash);
//						        arrList.set(position, b.hash);
//						        Log.i("Alfa", "Alfa");
//								break;
//								
//							case R.id.rad_Izin:
//								b.hash = new HashMap<String, String>();
//								b.hash.put("name", arrList.get(position).get("name"));
//						        b.hash.put("nim",arrList.get(position).get("nim"));
//						        b.hash.put("no",arrList.get(position).get("no"));
//						        b.hash.put("status", "Izin");
//						      //  b.array.set(posisi, b.hash);
//						        arrList.set(position, b.hash);
//						        Log.i("Izin", "Izin");
//								break;
//							
//							}
//						//}
////						Log.i("posisi", ""+posisi);
////						Log.i("posisi", hash.get("status"));
////						
//						//return v;	
//						//Absen_Adapter(context,b.array);
//					}
//				
//					}
////		        
//		        		);
//	            
//	            v.setTag(holder);
	        
	        }else{
	        	holder = (ViewHolder) v.getTag();
	        }
	        
//	        bapp_name = (TextView)v.findViewById(R.id.bapp_listmahasiswa_name);
//	        bapp_nim = (TextView)v.findViewById(R.id.bapp_listmahasiswa_nim);
//	        bapp_no = (TextView)v.findViewById(R.id.bapp_listmahasiswa_no);
	      //  radGroup = (RadioGroup)v.findViewById(R.id.bapp_radio);
	      
//	        holder.ref= position;
//	        holder.bapp_name.setText(arrList.get(position).get("name"));
//	        holder.bapp_nim.setText(arrList.get(position).get("nim"));
//	        holder.bapp_no.setText(arrList.get(position).get("no"));
	        
	      //  radGroup.setId(position);
	        
//
//	        radGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//				
//				@Override
//				public void onCheckedChanged(RadioGroup group, int id) {
//					// TODO Auto-generated method stub
//					Log.i("id group", ""+id);	
//					switch (id) {
//						
//						case R.id.rad_Hadir:
//							b.hash = new HashMap<String, String>();
//							b.hash.put("name", arrList.get(position).get("name"));
//					        b.hash.put("nim",arrList.get(position).get("nim"));
//					        b.hash.put("no",arrList.get(position).get("no"));
//					        b.hash.put("status", "Hadir");
//					        Log.i("Posisi", ""+position);
//					        arrList.set(position, b.hash);
//					       
//					        Log.i("Hadir", "Hadir");
//							break;
//						case R.id.rad_Alfa:
//							b.hash = new HashMap<String, String>();
//							String name = arrList.get(position).get("name").toString();
//							String nim =arrList.get(position).get("nim").toString();
//							String no = arrList.get(position).get("no").toString();
//							b.hash.put("name", name);
//					        b.hash.put("nim", nim);
//					        b.hash.put("no",no);
//					        b.hash.put("status", "Alfa");
//					        Log.i("Posisi", ""+position);
////					        Log.i("Alfa2", b.hash.get("status"));
////					        Log.i("Alfa2", b.hash.get("name"));
////					        Log.i("Alfa2", b.hash.get("nim"));
//					       // b.array.set(position, b.hash);
//					        arrList.set(position, b.hash);
//					        Log.i("Alfa", "Alfa");
//							break;
//							
//						case R.id.rad_Izin:
//							b.hash = new HashMap<String, String>();
//							b.hash.put("name", arrList.get(position).get("name"));
//					        b.hash.put("nim",arrList.get(position).get("nim"));
//					        b.hash.put("no",arrList.get(position).get("no"));
//					        b.hash.put("status", "Izin");
//					      //  b.array.set(posisi, b.hash);
//					        arrList.set(position, b.hash);
//					        Log.i("Izin", "Izin");
//							break;
//						
//						}
//					//}
////					Log.i("posisi", ""+posisi);
////					Log.i("posisi", hash.get("status"));
////					
//					//return v;	
//					//Absen_Adapter(context,b.array);
//				}
//			
//				}
////	        
//	        		);
	
	        return v;	
	        }
	
	public static class ViewHolder {
		//public TextView textDesc;
		//public TextView textValue;
		public RadioGroup radGr;
		public RadioButton rad1;
		public RadioButton rad2;
		public RadioButton rad3;
		public int ref;
		TextView bapp_nim,bapp_name,bapp_no;
	}
	
}

