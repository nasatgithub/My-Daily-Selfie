package com.androidapp.nasir.mydailyselfie;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;


public class ImageViewActivity extends Activity {
    public static String TAG="ImageViewActivity";
    ImageView iView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        getActionBar().hide();
        Intent intent=getIntent();
        String imageId=intent.getStringExtra("imageId");
        Log.i(TAG,"image id received : "+imageId);
        iView = (ImageView) findViewById(R.id.imageDisplay);
        ImageLoader iLoader=new ImageLoader();
        iLoader.execute(imageId);

    }

    public void setImageOnView(Bitmap bmp){
        if(bmp==null)
            Toast.makeText(getApplicationContext(), "NO IMAGE FOUND !!", Toast.LENGTH_LONG).show();
        else {
           //Toast.makeText(getApplicationContext(), "IMAGE FOUND !!", Toast.LENGTH_LONG).show();
            iView.setBackgroundColor(Color.WHITE);
            iView.setImageBitmap(bmp);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ImageLoader extends AsyncTask<String,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(String... params) {
            return  fetchImage(params[0]);
        }

        public Bitmap fetchImage(String imageId){
            File picFile=new File(Environment.getExternalStorageDirectory()+"/DCIM/Selfies/"+imageId);
            if(picFile.exists()){
                //Toast.makeText(getApplicationContext(), "IMAGE FOUND !!", Toast.LENGTH_LONG).show();
                Log.i(TAG,"Pic file path "+picFile.getAbsolutePath());
                return loadScaledPic(picFile.getAbsolutePath());

            }
            else
                return null;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
           setImageOnView(bitmap);
        }

        public Bitmap loadScaledPic(String picName){
        /* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
            int targetW = iView.getMaxWidth();
            int targetH = iView.getMaxHeight();
            Log.i(TAG,"targetW = "+targetW+"\ttargetH = "+targetH);
		/* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picName, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            Log.i(TAG,"photoW = "+photoW+"\tphotoH = "+photoH);
		/* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((photoW > 2048) || (photoH > 2048)) {
                scaleFactor = (int)Math.ceil(Math.max((float)photoW/2048, (float)photoH/2048));
                Log.i(TAG,"scale factor = "+ scaleFactor);
            }

		/* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            //   bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
            Bitmap bitmap = BitmapFactory.decodeFile(picName, bmOptions);

            return bitmap;

        }
    }
}
