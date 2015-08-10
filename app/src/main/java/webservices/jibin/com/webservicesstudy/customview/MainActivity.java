package webservices.jibin.com.webservicesstudy.customview;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TouchExampleView tch=new TouchExampleView(this);
		
		setContentView(tch);
	}


}
