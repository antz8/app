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

public class Reminder_Adapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> arrList;
    Context context;
    LayoutInflater inflater;
    
    TextView judul,tanggal;
    
    public Reminder_Adapter(Context ctx ,  ArrayList<HashMap<String, String>> arr) {
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
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 View v = convertView;
	        if (v == null) {
	            v = inflater.inflate(R.layout.reminder_list, parent, false);
	        }
	        
	       judul = (TextView)v.findViewById(R.id.reminder_listJudul);
	       tanggal = (TextView)v.findViewById(R.id.reminder_listTanggal);
	        
//	        img.setImageResource(R.drawable.ic_launcher);
	        judul.setText(arrList.get(position).get("judul"));
	        tanggal.setText(arrList.get(position).get("tanggal_mulai"));
	        
	        return v;
	}

}
