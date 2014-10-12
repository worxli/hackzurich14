package com.example.android_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ContactDetailViewActivity extends Activity {
	
	JSONObject finalResult;
	ListView listview;
	BCard card = null;
	BCardListAdapter listAdapter;
	Button button;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent myIntent = getIntent();
		String uuid = myIntent.getStringExtra("uuid");
		
		setContentView(com.example.android_client.R.layout.activity_contact_detail_view);
		listview = (ListView) findViewById(com.example.android_client.R.id.listView1);
		button = (Button) findViewById(com.example.android_client.R.id.button1);
		
		listAdapter = new BCardListAdapter(getApplicationContext());
		listview.setAdapter(listAdapter);
		
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
	            finalResult = new JSONObject(tokener);
	            
	        } catch (ClientProtocolException e) {e.printStackTrace();
	        } catch (IOException e) {e.printStackTrace();
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
	        try {
				setData();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	public void setData() throws JSONException {
		String 	name, first_name, dob, address, postcode, city, land, email_address, 
				phone_number, facebook, twitter, linkedin, xing;
		
			card = new BCard();
			
	
			if((name = finalResult.getString("name")) != "") {
				card.setName(name);
			}

			if((first_name = finalResult.getString("first_name")) != "") {
				card.setFirst_name(first_name);
			}

			if((dob = finalResult.getString("dob")) != "") {
				card.setDob(dob);
			}
		
			if((address = finalResult.getString("address")) != "") {
				card.setAddress(address);
			}
		
			if((postcode = finalResult.getString("postcode")) != "") {
				card.setPostcode(postcode);
			}
		
			if((city = finalResult.getString("city")) != "") {
				card.setCity(city);
			}
			
			if((land = finalResult.getString("land")) != "") {
				card.setLand(land);
			}
			
			if((email_address = finalResult.getString("email_address")) != "") {
				card.setEmail_address(email_address);
			}
			
			if((phone_number = finalResult.getString("phone_number")) != "") {
				card.setPhone_number(phone_number);
			}
			
			if((facebook = finalResult.getString("facebook")) != "") {
				card.setFacebook(facebook);
			}
			
			if((twitter = finalResult.getString("twitter")) != "") {
				card.setTwitter(twitter);
			}
			
			if((linkedin = finalResult.getString("linkedin")) != "") {
				card.setLinkedin(linkedin);
			}
			
			if((xing = finalResult.getString("xing")) != "") {
				card.setXing(xing);
			}
			
			//listAdapter.empty();
			
			for (Entry<String, String> entry : card.getAll().entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();
			    if(!value.equals("null")&&value!=null&&!value.equals("")){
			    	listAdapter.addString(key, value);
			    }
			}	
			
			listAdapter.notifyDataSetChanged();
	}
	
	public void addToContacts(View v) {
		 String DisplayName = card.getFirst_name().concat(" ".concat(card.getName()));
		 String MobileNumber = card.getPhone_number();
		 String emailID = card.getEmail_address();
		 String dob = card.getDob();
		 String address = (card.getAddress() == null) ? "" : card.getAddress();
		 String city = (card.getCity() == null) ? "" : card.getCity();
		 String postcode = (card.getPostcode() == null) ? "" : card.getPostcode();
		 String land = (card.getLand() == null) ? "" : card.getLand();

		 ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

		 ops.add(ContentProviderOperation.newInsert(
		 ContactsContract.RawContacts.CONTENT_URI)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
		     .build());

		 //------------------------------------------------------ Names
		 if (DisplayName != null) {
		     ops.add(ContentProviderOperation.newInsert(
		     ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
		         .withValue(
		     ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
		     DisplayName).build());
		 }

		 //------------------------------------------------------ Mobile Number                     
		 if (MobileNumber != null) {
		     ops.add(ContentProviderOperation.
		     newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
		         .build());
		 }
		 
		//------------------------------------------------------ Address                    
		 ops.add(ContentProviderOperation
			      .newInsert(ContactsContract.Data.CONTENT_URI)
			      .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
			        0)
			        
			      .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
			      .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, address) 

			      .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
			      .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, city)

			      .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
			      .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, postcode)

			      .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
			      .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, land)

			      .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
			      .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, "3").build());

		 //------------------------------------------------------ Home Numbers
		 /*if (HomeNumber != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
		         .build());
		 }*/

		 //------------------------------------------------------ Work Numbers
		 /*if (WorkNumber != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
		         .build());
		 }*/

		 //------------------------------------------------------ Email
		 if (emailID != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
		         .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
		         .build());
		 }

		//------------------------------------------------------ Date of Birth
		 if (!dob.equals("")) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
		     .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, dob).build());
		     
		 }

		 
		 //------------------------------------------------------ Organization
		 /*if (!company.equals("") && !jobTitle.equals("")) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
		         .build());
		 }*/

		 // Asking the Contact provider to create a new contact                 
		 try {
		     getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		 } catch (Exception e) {
		     e.printStackTrace();
		     Toast.makeText(getBaseContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		 } 
		 
		 button.setVisibility(View.GONE);
		 AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		 alertDialog.setMessage("Contact added");
		 alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
