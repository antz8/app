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

public class Chats_Adapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> arrList;
    Context context;
    LayoutInflater inflater;
    
    TextView chats_name, chats_idd,chats_message;
    
    public Chats_Adapter(Context ctx ,  ArrayList<HashMap<String, String>> arr) {
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
	            v = inflater.inflate(R.layout.chats_list, parent, false);
	        }
	        
	        chats_name = (TextView)v.findViewById(R.id.chats_name);
	        chats_idd = (TextView)v.findViewById(R.id.chats_idd);
	        chats_message = (TextView)v.findViewById(R.id.chats_last_chat);
	      
	        chats_name.setText(arrList.get(position).get("name"));
	        chats_idd.setText(arrList.get(position).get("idd"));
	        chats_message.setText(arrList.get(position).get("message"));
	        return v;	
	        }
	

}
