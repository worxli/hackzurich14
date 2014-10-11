package com.example.android_client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity implements OnRefreshListener {
	
	SwipeRefreshLayout swipeLayout; 
	DeviceScanActivity dsa;
	BluetoothManager bluetoothManager;
	BluetoothAdapter mBluetoothAdapter;
	HashSet<String> uuids = new HashSet<String>();
	ListView listview;
	CardListAdapter mCardListAdapter;
	BCard card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		
		mCardListAdapter = new CardListAdapter(getApplicationContext());
		
		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mCardListAdapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					card = (BCard) arg0.getItemAtPosition(arg2);
			}
		});
		    
		// Initializes Bluetooth adapter.
		bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		dsa = new DeviceScanActivity();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);
                findDevices();
            }
        }, 5000);
		
	}
	
	
	private void findDevices() {
		
		// Ensures Bluetooth is available on the device and it is enabled. If not,
		// displays a dialog requesting user permission to enable Bluetooth.
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, 1);
		}
		
		dsa.scanLeDevice(true, mBluetoothAdapter);
		
	}
	
	public class DeviceScanActivity {

	    private boolean mScanning;
	    private Handler mHandler = new Handler();

	    // Stops scanning after 5 seconds.
	    private static final long SCAN_PERIOD = 5000;

		private void scanLeDevice(final boolean enable, final BluetoothAdapter mBluetoothAdapter) {
			
	        if (enable) {
	            // Stops scanning after a pre-defined scan period.
	            mHandler.postDelayed(new Runnable() {
	                @Override
	                public void run() {
	                    mScanning = false;
	                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
	                }
	            }, SCAN_PERIOD);

	            mScanning = true;
	            mBluetoothAdapter.startLeScan(mLeScanCallback);
	        } else {
	            mScanning = false;
	            mBluetoothAdapter.stopLeScan(mLeScanCallback);
	        }
	    }
	}
	
	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback =
	        new BluetoothAdapter.LeScanCallback() {
	    @Override
	    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
	        Log.d("scan", scanRecord.toString());
	        	        
	        
			if(device.getType()==device.DEVICE_TYPE_LE) {
				checkUUIDonServer(device.getAddress());
			}        
	   }

		private void checkUUIDonServer(String uuid) {
			
			Log.d("CHECK UUID", uuid);
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://hackzurich14.worx.li/getNameByUUID.php");
			HttpResponse response = null;

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("UUID", uuid));
			try {
				post.setEntity(new UrlEncodedFormEntity(pairs));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String body = null;

			try {
				response = client.execute(post);
				HttpEntity entity = response.getEntity();
				body = EntityUtils.toString(entity);
				Log.d("Response", body);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(response != null)
			{
				LCard lcard = new LCard(body.toString(), uuid);
				mCardListAdapter.addLCard(lcard);
				mCardListAdapter.notifyDataSetChanged();
				
			}
			// TODO Auto-generated method stub
			
		}
	};
}
