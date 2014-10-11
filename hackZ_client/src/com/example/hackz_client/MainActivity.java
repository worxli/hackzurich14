package com.example.hackz_client;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	DeviceScanActivity dsa;
	BluetoothManager bluetoothManager;
	BluetoothAdapter mBluetoothAdapter;
	Context context;
	CardListAdapter mCardListAdapter;
	BluetoothGatt mBluetoothGatt;
	ListView listview;
	BCard card;
	ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.context = (Context)this.getApplicationContext();
		listview = (ListView) findViewById(R.id.listView1);
		listview.setAdapter(mCardListAdapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					card = (BCard) arg0.getItemAtPosition(arg2);
			}
		});
		
	}
	
	public void scanDevices(View v){
		
		// Initializes Bluetooth adapter.
		bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		
		// Ensures Bluetooth is available on the device and it is enabled. If not,
		// displays a dialog requesting user permission to enable Bluetooth.
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, 1);
		}
		
		dsa.scanLeDevice(true, mBluetoothAdapter);
		
	}
	
	public void updateCardList(){
		
		Log.d("device list length", mDeviceList.size()+"");
		
		for (BluetoothDevice device : mDeviceList) {
			Log.d("asdf","asdf");
			if(device.getType()==device.DEVICE_TYPE_LE) {
				try {
					for (ParcelUuid i : device.getUuids()) {
						checkUUIDonServer(i.getUuid());
					}
				} catch (Exception e) {
					Log.e("device UUID list", e.toString());
				}
				
			}
		}
	}
	
	private void checkUUIDonServer(UUID uuid) {
		
		//send uuid to server and check if it exists -> then return card 
		Log.d("UUID", uuid.toString());
		
	}

	public class DeviceScanActivity extends ListActivity {

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
	    public void onLeScan(final BluetoothDevice device, int rssi,
	            byte[] scanRecord) {
	        runOnUiThread(new Runnable() {
	           @Override
	           public void run() {
	        	   Log.d("Device add", device.getName()+"");
	               mDeviceList.add(device);
	               updateCardList();
	           }
	       });
	   }
	};

	
}
