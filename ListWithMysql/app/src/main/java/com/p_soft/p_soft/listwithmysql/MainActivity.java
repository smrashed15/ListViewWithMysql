package com.p_soft.p_soft.listwithmysql;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MainActivity extends Activity {
 private String jsonResult;
 private String url = "http://10.0.3.2/geta/employee_details.php";
 private ListView listView;
 //

 List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();
 
 
 
 SimpleAdapter simpleAdapter;
 
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  listView = (ListView) findViewById(R.id.listView1);

  accessWebService();
  
 }
 

 
 // Async Task to access the web
 private class JsonReadTask extends AsyncTask<String, Void, String> {
  @Override
  protected String doInBackground(String... params) {
   HttpClient httpclient = new DefaultHttpClient();
   HttpPost httppost = new HttpPost(params[0]);
   try {
    HttpResponse response = httpclient.execute(httppost);
    jsonResult = inputStreamToString(
      response.getEntity().getContent()).toString();
   }
 
   catch (ClientProtocolException e) {
    e.printStackTrace();
   } catch (IOException e) {
    e.printStackTrace();
   }
   return null;
  }
 
  private StringBuilder inputStreamToString(InputStream is) {
   String rLine = "";
   StringBuilder answer = new StringBuilder();
   BufferedReader rd = new BufferedReader(new InputStreamReader(is));
 
   try {
    while ((rLine = rd.readLine()) != null) {
     answer.append(rLine);
    }
   }
 
   catch (IOException e) {
    Toast.makeText(getApplicationContext(),
      "Error..." + e.toString(), Toast.LENGTH_LONG).show();
   }
   return answer;
  }
 
  @Override
  protected void onPostExecute(String result) {
   ListDrwaer();
  }
 }
 
 public void accessWebService() {

  JsonReadTask task = new JsonReadTask();
  task.execute(new String[] { url });

 }
 

 public void ListDrwaer() {
 
  try {
   JSONObject jsonResponse = new JSONObject(jsonResult);
   JSONArray jsonMainNode = jsonResponse.optJSONArray("emp_info");
 
   for (int i = 0; i < jsonMainNode.length(); i++) {
    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
    String name = jsonChildNode.optString("name");
    employeeList.add(createEmployee("", name));
   }
  } catch (JSONException e) {
   Toast.makeText(getApplicationContext(), "Error" + e.toString(),
     Toast.LENGTH_SHORT).show();
  }
 
  simpleAdapter = new SimpleAdapter(this, employeeList,
    android.R.layout.simple_list_item_1,
    new String[] { "" }, new int[] { android.R.id.text1 });
  listView.setAdapter(simpleAdapter);


  
  listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
  {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
      {

          Toast.makeText(MainActivity.this,listView.getItemAtPosition(position).toString()+" "+ position, Toast.LENGTH_SHORT).show();

         String st=listView.getItemAtPosition(position).toString();

          StringTokenizer str = new StringTokenizer(st, "{}=");
         String st1=str.nextToken();
          //String st2=str.nextToken();
          Toast.makeText(MainActivity.this,st1+" ", Toast.LENGTH_SHORT).show();

      }
  });

  
 }
 
 private HashMap<String, String> createEmployee(String name, String number) {
  HashMap<String, String> employeeNameNo = new HashMap<String, String>();
  employeeNameNo.put(name, number);
  return employeeNameNo;
 }
 
 
}
