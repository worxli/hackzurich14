package com.example.android_client;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
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
	                    updateCardList();
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
				try {
					for (ParcelUuid i : device.getUuids()) {
						if(checkUUIDonServer(i.getUuid())){
							
						}
					}
				} catch (Exception e) {
					Log.e("device UUID list", e.toString());
				}
			}
	        
	        uuids.add(device.getName());
	        
	   }

		private boolean checkUUIDonServer(UUID uuid) {
			
			
			return true;
			// TODO Auto-generated method stub
			
		}
	};
	
	public void updateCardList(){
		
		Log.d("device list length", uuids.size()+"");
		
		
		
	}
}
