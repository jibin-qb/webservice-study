package webservices.jibin.com.webservicesstudy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
        new JSONCall().execute();
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

        protected String doInBackground(Void... urls) {
            return readBugzilla();
        }
private Data processResponse(String response) throws JsonSyntaxException
    {

        Data obj = null;

        Gson gson = new Gson();
        if (responseClass() != null)
            obj = gson.fromJson(response, Data.class);
        Log.i("", "" + obj);
        return obj;
    }
        protected void onPostExecute(String data) {

            ArrayList<User> users = new ArrayList<User>();
            try {
                data = "{'data':" + data + "}";
                Data dat=processResponse(data);
users=dat.data;
            } catch (JSONException e) {

            }
            Dialog.dismiss();

            if (listAdapter == null) {
                listAdapter = new ListAdapter(users, JSONStudy.this);
            }
            listView.setAdapter(listAdapter);
        }

    }
}
