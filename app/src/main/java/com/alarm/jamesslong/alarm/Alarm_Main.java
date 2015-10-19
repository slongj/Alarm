package com.alarm.jamesslong.alarm;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


public class Alarm_Main extends Activity {
    ListView listView;
//    AlarmAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
        listView = (ListView) findViewById(R.id.alarmList);

        FloatingActionButton addAlarmButton = (FloatingActionButton) findViewById(R.id.addAlarm);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Toast.makeText(getApplicationContext(), "Clicked Button", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    public class AlarmAdapter extends ArrayAdapter<AlarmObject>{
//        public MedicationsAdapter(Context context, ArrayList<AlarmObject> alarmList){
//            super(context, 0, alarmList);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent){
//            AlarmObject alarmObject = getItem(position);
//            if(convertView == null){
//                convertView = LayoutInflater.from(getContext()).inflate()
//            }
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_alarm_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
