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

public class Chats_Action_Adapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> arrList;
    Context context;
    LayoutInflater inflater;
    
    TextView terima,kirim,userr,roster,time_user,time_rec;
    
    
    public Chats_Action_Adapter(Context ctx ,  ArrayList<HashMap<String, String>> arr) {
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
	            v = inflater.inflate(R.layout.chats_action_list, parent, false);
	        }
	        
	        kirim = (TextView)v.findViewById(R.id.chats_action_kirim);
	        terima = (TextView)v.findViewById(R.id.chats_action_terima);
	        
	        userr = (TextView)v.findViewById(R.id.chats_user);
	        roster = (TextView)v.findViewById(R.id.chats_roster);
	        
	        time_user = (TextView)v.findViewById(R.id.chats_time_user);
	        time_rec = (TextView)v.findViewById(R.id.chats_time_rec);
	        
	        String user = arrList.get(position).get("user");
	        String recipient = arrList.get(position).get("recipient");
	        String text = arrList.get(position).get("message");
	        String time = arrList.get(position).get("time");
	        
	        if (user.equals("0")){
	        	kirim.setText("");
	        	terima.setText(text);
	        	roster.setText(recipient);
	        	time_user.setText("");
	        	time_rec.setText(time);
	        	kirim.setVisibility(View.INVISIBLE);
	        	terima.setVisibility(View.VISIBLE);
	        	userr.setVisibility(View.INVISIBLE);
	        	roster.setVisibility(View.VISIBLE);
	        	time_user.setVisibility(View.INVISIBLE);
	        	time_rec.setVisibility(View.VISIBLE);
	        }
	        
	        if (recipient.equals("0")){
	        	kirim.setText(text);
	        	terima.setText("");
	        	userr.setText(user);
	        	time_user.setText(time);
	        	time_rec.setText("");
	        	kirim.setVisibility(View.VISIBLE);
	        	terima.setVisibility(View.INVISIBLE);
	        	userr.setVisibility(View.VISIBLE);
	        	roster.setVisibility(View.INVISIBLE);
	        	time_user.setVisibility(View.VISIBLE);
	        	time_rec.setVisibility(View.INVISIBLE);
	        }
	        return v;	
	        }
}
