package com.toaker.layeredcover.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {


    public static final boolean DEBUG = true;

    private static int VERSION = 1;

    private static final String LOG_TAG = "LayeredCover-Sample" + ++VERSION;

    private static final int[] VIEW_ICONS = new int[]{R.mipmap.icn_1,R.mipmap.icn_2,
            R.mipmap.icn_3,R.mipmap.icn_4,
            R.mipmap.icn_5,R.mipmap.icn_6,
            R.mipmap.icn_7,};

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new SampleAdapter());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    class SampleAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return VIEW_ICONS.length;
        }

        @Override
        public Object getItem(int position) {
            return VIEW_ICONS[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.listview_item,null);
                holder.mIconView = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mIconView.setBackgroundResource(VIEW_ICONS[position]);
            return convertView;
        }
    }

    private class ViewHolder{
        public ImageView mIconView;
    }
}
