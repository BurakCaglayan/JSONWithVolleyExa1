package com.ctis487.jsonwithvolleyexa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity {

	EditText editTextKey;
	ListView listViewBooks;

	public static final String TAG_BOOKS = "books";
	public static final String TAG_NAME = "name";
	public static final String TAG_AUTHOR = "author";
	public static final String TAG_YEAR = "year";
	public static final String TAG_IMG = "img";

	private JSONArray kitaplar = null;
	private JSONObject kitaplarJSONAObject = null;
	// HashMap for ListView
	private ArrayList<HashMap<String, String>> kitaplarListesi;

	private ProgressDialog mProgressDialog;

	RequestQueue queue;

	String books_url = "http://www.ctis.bilkent.edu.tr/ctis487/jsonBook/books.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editTextKey = (EditText) findViewById(R.id.eitTextKey);
		listViewBooks = (ListView) findViewById(R.id.listViewBooks);
		
		queue = Volley.newRequestQueue(this);
		
	}

	public void onClick(View v) {

		kitaplarListesi = new ArrayList<HashMap<String, String>> ();
		
		mProgressDialog = new ProgressDialog(MainActivity.this);
		mProgressDialog.setMessage("Please Wait");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, books_url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						kitaplarJSONAObject = response;
						String key = editTextKey.getText().toString();
						new KitaplariAl().execute(key);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("Result",
								"ERROR JSONObject request" + error.toString());
						if (mProgressDialog.isShowing())
							mProgressDialog.dismiss();
					}
				});

		// add request to queue
		queue.add(jsonObjectRequest);
		// add request to queue

	}

	private class KitaplariAl extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			try {
			
				// Getting JSON Array node
				kitaplar = kitaplarJSONAObject.getJSONArray(TAG_BOOKS);

				// looping through all Contacts
				for (int i = 0; i < kitaplar.length(); i++) {
					JSONObject c = kitaplar.getJSONObject(i);

					String name = c.getString(TAG_NAME);
					String author = c.getString(TAG_AUTHOR);
					String year = c.getString(TAG_YEAR);
					String img = c.getString(TAG_IMG);

					HashMap<String, String> kitap = new HashMap<String, String>();

					kitap.put(TAG_NAME, name);
					kitap.put(TAG_AUTHOR, author);
					kitap.put(TAG_YEAR, year);
					kitap.put(TAG_IMG,
							"http://www.ctis.bilkent.edu.tr/ctis487/jsonBook/"
									+ img);

					kitaplarListesi.add(kitap);
				}
			} catch (JSONException ee) {
				ee.printStackTrace();
			}

			return kitaplar.length();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();		
		}

		@Override
		protected void onPostExecute(Integer kitapSayisi) {
			super.onPostExecute(kitapSayisi);
			// Dismiss the progress dialog
			if (mProgressDialog.isShowing())
				mProgressDialog.dismiss();
		
			if (kitaplarListesi != null) {
				ListViewAdapter listViewAdapter = new ListViewAdapter(
						MainActivity.this, kitaplarListesi);
				listViewBooks.setAdapter(listViewAdapter);
			} else
				Toast.makeText(MainActivity.this, "Not Found",
						Toast.LENGTH_LONG).show();
		}
	}
}
