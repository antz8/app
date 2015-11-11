package adapter;

import java.util.ArrayList;

import com.example.p_talk.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Absen_Adapter2 extends BaseAdapter {

	private Activity activity;
    private String[] no;
    private String[] nim;
    private String[] nama;
    private int [] selected;
    public ArrayList<String> status;
    public Spinner [] spinner;
    private static LayoutInflater inflater = null;
 
    public Activity getActivity() {
        return activity;
    }
 
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
 
   
 
    public Absen_Adapter2(Activity a, String[] no, String[] nim, String[] nama,ArrayList<String> status) {
        this.no = no;
        activity = a;
       // activity.setTheme(android.R.style.Theme_Light);
        this.nim = nim;
        this.nama = nama;
        this.status = status;
        //inisialisasi nilai awal dengan nilai 1
//        for(int i=0;i<status.length;i++){
//            status[i]="Alfa";
//        }
        this.selected=new int[no.length];
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    @Override
    public Object getItem(int position) {
        return position;
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public static class ViewHolder {
        public TextView bapp_nim;
        public TextView bapp_name;
        public TextView bapp_no;
        public Spinner editText;
        public int ref;
    }
 
    public void refreshView() {
        notifyDataSetChanged();
    }
 
    String[] items;
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        String [] nilai=activity.getResources().getStringArray(R.array.StatusAbsen);
        ArrayAdapter<String> array=new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item,nilai);
        array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ViewHolder holder;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.bapp_list_mahasiswa, null);
            holder = new ViewHolder();
            holder.bapp_nim = (TextView) vi.findViewById(R.id.bapp_listmahasiswa_nim);
            holder.bapp_name = (TextView) vi.findViewById(R.id.bapp_listmahasiswa_name);
            holder.bapp_no = (TextView) vi.findViewById(R.id.bapp_listmahasiswa_no);
            holder.editText = (Spinner) vi
                    .findViewById(R.id.spinner_absen);
            holder.editText.setAdapter(array);
             
            holder.editText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
 
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                        int arg2, long arg3) {
                	
                	status.set(holder.ref, holder.editText.getSelectedItem().toString());
                    selected[holder.ref]=holder.editText.getSelectedItemPosition();
                    
                    Log.i("Posisi",""+selected[holder.ref]);

                }
 
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                     
                }
            });
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.ref=position;
        holder.bapp_no.setText(no[position] );
        holder.bapp_nim.setText(nim[position]);
        holder.bapp_name.setText(nama[position]);
       
        if (status.get(position).toString().equals("Hadir")){
        selected[holder.ref] = 1;
        holder.editText.setSelection(selected[position]);
        
        }else  if (status.get(position).toString().equals("Alfa")){
            selected[holder.ref] = 0;
            holder.editText.setSelection(selected[position]);
            
            }else if (status.get(position).toString().equals("Izin")){
                selected[holder.ref] = 2;
                holder.editText.setSelection(selected[position]);
                } 
       Log.i("Posisi 2 :", ""+selected[position]);
        Log.i("Posisi 3 :", ""+position);
        return vi;
    }
 
    @Override
    public int getCount() {
        return no.length;
    }
}
