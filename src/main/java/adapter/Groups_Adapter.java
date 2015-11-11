package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.p_talk.R;

import android.content.Context;
//import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Groups_Adapter extends BaseAdapter {
	
	ArrayList<HashMap<String, String>> arrList;
    Context context;
    LayoutInflater inflater;
    
    TextView groups_name1,name_room;
    
    public Groups_Adapter(Context ctx ,  ArrayList<HashMap<String, String>> arr) {
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
	            v = inflater.inflate(R.layout.groups_list, parent, false);
	        } 
	        groups_name1 = (TextView)v.findViewById(R.id.groups_name1);
	        name_room = (TextView)v.findViewById(R.id.groups_nameroom);
	        groups_name1.setText(arrList.get(position).get("name1"));
	        name_room.setText(arrList.get(position).get("nameroom"));
	       
	        
	        return v;
	}
}
