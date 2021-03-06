package webservices.jibin.com.webservicesstudy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import webservices.jibin.com.webservicesstudy.db.AndroidDatabaseExample;

/**
 * Created by jibin on 20/07/15.
 */
public class JSONStudy extends Activity {

    ListAdapter listAdapter;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_view);
        listView = (ListView) findViewById(R.id.listView);
       // new JSONCall().execute();

        Intent intent=new Intent(this, AndroidDatabaseExample.class);
        startActivity(intent);

        //VolleyStudy volleyStudy=new VolleyStudy(this);
        //volleyStudy.jsonRequest("http://jsonplaceholder.typicode.com/posts");
//       AqueryStudy study=new AqueryStudy(this);
//       study. downloadImage("http://www.online-image-editor.com//styles/2014/images/example_image.png");
//>>>>>>> c45265bd175468b32fd2e65c80426a9f7c9394a7
    }

    private class JSONCall extends AsyncTask<Void, Void, String> {


        private ProgressDialog Dialog = new ProgressDialog(JSONStudy.this);


        protected void onPreExecute() {


            Dialog.setMessage("Please wait..");
            Dialog.show();


        }

        public String readBugzilla() {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://jsonplaceholder.typicode.com/posts");
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    Log.e(JSONStudy.class.toString(), "Failed to download file");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        private String makeRequest(String path, Object params) throws Exception {
            DefaultHttpClient httpclient = new DefaultHttpClient();

            HttpPost httpost = new HttpPost(path);
            Gson gson = new Gson();
            StringBuilder builder = new StringBuilder();
            StringEntity se = new StringEntity(gson.toJson(params));
            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            try {

                HttpResponse response = httpclient.execute(httpost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    Log.e(JSONStudy.class.toString(), "Failed to download file");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        protected String doInBackground(Void... urls) {
            return readBugzilla();
        }

        private Data processResponse(String response) throws JsonSyntaxException {

            Data obj = null;

            Gson gson = new Gson();

            obj = gson.fromJson(response, Data.class);
            Log.i("", "" + obj);
            return obj;
        }

        protected void onPostExecute(String data) {

            ArrayList<User> users = new ArrayList<User>();
            try {
                data = "{'data':" + data + "}";
                Data dat = processResponse(data);
                users = dat.data;
            } catch (Exception e) {

            }
            Dialog.dismiss();

            if (listAdapter == null) {
                listAdapter = new ListAdapter(users, JSONStudy.this);
            }
            listView.setAdapter(listAdapter);
        }

    }
}
