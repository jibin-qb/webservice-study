package webservices.jibin.com.webservicesstudy;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;

/**
 * Created by jibin on 30/07/15.
 */
public class AqueryStudy {

    Activity context;
    public AqueryStudy(Activity context){
        this.context=context;
    }

    public void downloadImage(String url){
         AQuery androidQuery = new AQuery(context);
        androidQuery
                .id(R.id.imageView)
                .progress(R.id.progressBar)
                .image(url, true, true,
                        400, 0, new BitmapAjaxCallback() {

                            @Override
                            public void callback(String url,
                                                 ImageView imageView, Bitmap bm,
                                                 AjaxStatus status) {
                                if (bm != null) {
                                    imageView.setImageBitmap(bm);
                                }
                            }

                        });
    }

}
