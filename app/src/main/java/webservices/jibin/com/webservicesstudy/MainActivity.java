package webservices.jibin.com.webservicesstudy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends Activity {
	private final String NAMESPACE = "http://www.w3schools.com/webservices/";
	private final String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
	private final String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
	private final String METHOD_NAME = "CelsiusToFahrenheit";
	private String TAG = "WEBSERVICE";
	private static String fahren;
	Button b;
	TextView tv;
	EditText et;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        initViews();

        ImageView view=(ImageView)findViewById(R.id.imag);
        final AnimationDrawable myAnimationDrawable
                = (AnimationDrawable)view.getDrawable();

                        myAnimationDrawable.start();

		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Check if Celcius text control is not empty
				if (et.getText().length() != 0 && et.getText().toString() != "") {
					//Get the text control value
				new AsyncCallWS().execute(et.getText().toString());
				//If text control is empty
				} else {
					tv.setText("Please enter Celcius");
				}
			}
		});
	}

    private void initViews() {
//        et = (EditText) findViewById(R.id.editText1);
//        tv = (TextView) findViewById(R.id.tv_result);
//        b = (Button) findViewById(R.id.button1);
    }

    public void getFahrenheit(String celsius) {
		//Create request
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		//Property which holds input parameters
		PropertyInfo celsiusPI = new PropertyInfo();
		//Set Name
		celsiusPI.setName("Celsius");
		//Set Value
		celsiusPI.setValue(celsius);
		//Set dataType
		celsiusPI.setType(double.class);
		//Add the property to request object
		request.addProperty(celsiusPI);
		//Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		//Set output SOAP object
		envelope.setOutputSoapObject(request);
		//Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			//Invole web service
			androidHttpTransport.call(SOAP_ACTION, envelope);
			//Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			//Assign it to fahren static variable
			fahren = response.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void gotoNext(View view) {
        Intent intent=new Intent(this,JSONStudy.class);
        startActivity(intent);
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			Log.i(TAG, "doInBackground");
			getFahrenheit(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, "onPostExecute");
			tv.setText(fahren + "� F");
		}

		@Override
		protected void onPreExecute() {
			Log.i(TAG, "onPreExecute");
			tv.setText("Calculating...");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.i(TAG, "onProgressUpdate");
		}

	}

}
