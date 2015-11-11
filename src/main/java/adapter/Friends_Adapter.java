package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.p_talk.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Friends_Adapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> arrList;
    Context context;
    LayoutInflater inflater;
    
    TextView friends_name,friends_nim,friends_status;
    ImageView online,offline;
    
    public Friends_Adapter(Context ctx ,  ArrayList<HashMap<String, String>> arr) {
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
	            v = inflater.inflate(R.layout.friends_list, parent, false);
	        }
	        
	        friends_name = (TextView)v.findViewById(R.id.friends_name);
	        friends_nim = (TextView)v.findViewById(R.id.friends_nim);
	       
	        friends_name.setText(arrList.get(position).get("name"));
	        friends_nim.setText(arrList.get(position).get("nim"));

	        return v;
	}
	
}
