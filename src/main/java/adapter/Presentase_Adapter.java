package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.p_talk.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Presentase_Adapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> arrList;
    Context context;
    LayoutInflater inflater;
    
    TextView presentase_nama,presentase_nim,presentase_persen,no;
    
    public Presentase_Adapter(Context ctx ,  ArrayList<HashMap<String, String>> arr) {
		// TODO Auto-generated constructor stub
    	arrList = arr;
    	context = ctx;
    	inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 View v = convertView;
	        if (v == null) {
	            v = inflater.inflate(R.layout.presentase_list, parent, false);
	        }
	        
	       presentase_nama = (TextView)v.findViewById(R.id.presentase_nama);
	      presentase_nim = (TextView)v.findViewById(R.id.presentase_nim);
	      presentase_persen = (TextView)v.findViewById(R.id.presentase_persen);
	      no = (TextView)v.findViewById(R.id.presentase_no);
	      
	        
//	        img.setImageResource(R.drawable.ic_launcher);
	       presentase_nama.setText(arrList.get(position).get("nama"));
	       presentase_nim.setText(arrList.get(position).get("nim"));
	       presentase_persen.setText(arrList.get(position).get("persen"));
	       no.setText(arrList.get(position).get("no"));
	        
	        return v;
	}
}
