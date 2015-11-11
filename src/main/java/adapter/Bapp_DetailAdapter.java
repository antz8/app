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

public class Bapp_DetailAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> arrList;
    Context context;
    LayoutInflater inflater;
    
    TextView bapp_no,bapp_name,bapp_nim,bapp_status;
    
    
    public Bapp_DetailAdapter(Context ctx ,  ArrayList<HashMap<String, String>> arr) {
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
	            v = inflater.inflate(R.layout.bapp_list_detail, parent, false);
	        }
	        
	        bapp_name = (TextView)v.findViewById(R.id.bapp_listDetailNama);
	        bapp_nim = (TextView)v.findViewById(R.id.bapp_listDetailNim);
	        bapp_no = (TextView)v.findViewById(R.id.bapp_listDetailNo);
	        bapp_status = (TextView)v.findViewById(R.id.bapp_detailStatusHadir);
	      
	        bapp_name.setText(arrList.get(position).get("name"));
	        bapp_nim.setText(arrList.get(position).get("nim"));
	        bapp_no.setText(arrList.get(position).get("no"));
	        bapp_status.setText(arrList.get(position).get("status"));
	        return v;	
	        }
	

}
