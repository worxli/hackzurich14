package com.example.android_client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class ContactDetailViewActivity extends Activity {
	
	JSONArray finalResult;
	ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_detail_view);
		Intent myIntent = getIntent();
		String uuid = myIntent.getStringExtra("uuid");
		listview = (ListView) findViewById(R.id.listView1);
		
		new RequestTask().execute("http://hackzurich14.worx.li/getByUUID.php",uuid);
	}
	
	class RequestTask extends AsyncTask<String, String, String>{

	    @Override
	    protected String doInBackground(String... uri) {
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpPost post = new HttpPost(uri[0]);
	        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	        pairs.add(new BasicNameValuePair("UUID", uri[1]));
	        
	        HttpResponse response;
	        String responseString = null;
	        try {
	        	post.setEntity(new UrlEncodedFormEntity(pairs));
	        	
	            response = httpclient.execute(post);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
	            String json = reader.readLine();
	            JSONTokener tokener = new JSONTokener(json);
	            finalResult = new JSONArray(tokener);
	            
	        } catch (ClientProtocolException e) {
	        } catch (IOException e) {
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return responseString;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        Log.d("Result", finalResult.toString());
	        setData();
	    }
	}
	
	public void setData() {
		
		
		//....
	}

}
