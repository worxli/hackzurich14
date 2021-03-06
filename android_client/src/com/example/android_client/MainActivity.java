package com.example.android_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.android_client.ContactDetailViewActivity.RequestTask;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends Activity implements OnRefreshListener {
	
	protected static final String TAG = null;
	SwipeRefreshLayout swipeLayout; 
	DeviceScanActivity dsa;
	BluetoothManager bluetoothManager;
	BluetoothAdapter mBluetoothAdapter;
	HashSet<String> uuids = new HashSet<String>();
	ListView listview;
	CardListAdapter mCardListAdapter;
	LCard card;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		context = getApplicationContext();
		
		mCardListAdapter = new CardListAdapter(getApplicationContext());
		
		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mCardListAdapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				card = (LCard) arg0.getItemAtPosition(arg2);
				String uuid = card.getUuid();
				Intent intent = new Intent(context, ContactDetailViewActivity.class);
				intent.putExtra("uuid", uuid);
				startActivity(intent);
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
		
		mCardListAdapter.empty();
		mCardListAdapter.notifyDataSetChanged();
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
				new RequestTask().execute(device.getAddress());
			}
			
			device.connectGatt(getBaseContext(), true, btGattCB);
	   }
	    
	    class RequestTask extends AsyncTask<String, String, Pair<String,String>>{

		    @Override
		    protected Pair<String, String> doInBackground(String... uri) {
		    	
		    	Log.d("Address", uri[0]);
		        
		    	String uuid = uri[0];
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
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	
		        return new Pair<String,String>(body,uuid);
		    }

		    @Override
		    protected void onPostExecute(Pair<String, String> pair) {
		    	super.onPostExecute(pair);
		    	
		    	String body = pair.first;
		    	String uuid = pair.second;
		        
		        if(body != null && !body.equals("") && body.length()<150)
				{
					LCard lcard = new LCard(body.toString(), uuid);
					mCardListAdapter.addLCard(lcard);
					mCardListAdapter.notifyDataSetChanged();
					
				}
		    }
		}
	};
	
	
	
	///GATT stuff
	
	
	private BluetoothGattCallback btGattCB = new BluetoothGattCallback() {
	    @Override
	    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
	        super.onConnectionStateChange(gatt, status, newState);
	        if(newState == BluetoothProfile.STATE_CONNECTED){
	            //Log.i(TAG, "Gatt Connected");
	            gatt.discoverServices();
	        }
	        else if(newState == BluetoothProfile.STATE_DISCONNECTED){
	            //Log.i(TAG, "Gatt Disconnected");
	        }
	    }

	    @Override
	    public void onServicesDiscovered(BluetoothGatt gatt, int status){
	        Log.i(TAG,"Status onServiceDiscovered: "+status);   //status code i'm getting here is 129
	        List<BluetoothGattService> btServices = gatt.getServices();//try anyway
	        for (BluetoothGattService bluetoothGattService : btServices) {
				//Log.d("service",bluetoothGattService.getUuid().toString());
				new RequestTask().execute(bluetoothGattService.getUuid().toString());
	//			bluetoothGattService.g
			}
	    }
	    
	    
	    class RequestTask extends AsyncTask<String, String, Pair<String,String>>{

		    @Override
		    protected Pair<String, String> doInBackground(String... uri) {
		    	
		    	Log.d("Address", uri[0]);
		        
		    	String uuid = uri[0];
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
					if(body.length()<150){
						Log.d("Response", body);
					}
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	
		        return new Pair<String,String>(body,uuid);
		    }

		    @Override
		    protected void onPostExecute(Pair<String, String> pair) {
		    	super.onPostExecute(pair);
		    	
		    	String body = pair.first;
		    	String uuid = pair.second;
		        
		        if(body != null && !body.equals("") && body.length()<150)
				{
					LCard lcard = new LCard(body.toString(), uuid);
					mCardListAdapter.addLCard(lcard);
					mCardListAdapter.notifyDataSetChanged();
					
				}
		    }
		}
	    
	};
}
