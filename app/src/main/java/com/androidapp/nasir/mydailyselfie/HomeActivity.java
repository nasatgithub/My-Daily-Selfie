package com.androidapp.nasir.mydailyselfie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeActivity extends Activity implements ItemFragment.OnFragmentInteractionListener
{
    public static String TAG="Home-Activity";
    private String PIC_PREFIX="Selfie_";
    private String mCurrentAbsolutePicPath;
    private int REQUEST_TAKE_SMALL_PIC=1;
    private int REQUEST_TAKE_BIG_PIC=2;
    public static String[] ITEMS={"A","B","C"};
    public FragmentManager fragmentManager;
    public static View mImageIconView;
    private String imageFileName;
    private  File tempFileCreated;
    private boolean onActivityResultCalled=false;
    private BroadcastReceiver mRefreshReceiver;
    public static final int IS_ALIVE = Activity.RESULT_FIRST_USER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragmentManager=getFragmentManager();
        installFragment();
        setupAlarm();

    }

    public void installFragment(){
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container1, new ItemFragment(), ItemFragment.TAG);
        fragmentTransaction.commit();
    }

    public void setupAlarm(){
        AlarmManager aMgr=(AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(getApplicationContext(),NotifySelfie.class);
        PendingIntent pi= PendingIntent.getBroadcast(this,0,intent,0);
        aMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+ (60 * 1000),pi);
        Log.i(TAG,"AlarmSet");
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.i(TAG, "Resuming HOME ACTVITIY");
        if(!onActivityResultCalled){
            if(tempFileCreated!=null)
                tempFileCreated.delete();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
        else if(id == R.id.action_open_camera){
            try {
                sendCameraIntentBigPic();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendCameraIntentBigPic() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f=null;
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            try{
                f=setupFileForPic();
                tempFileCreated=f;
                mCurrentAbsolutePicPath=f.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                onActivityResultCalled=false;
                startActivityForResult(takePictureIntent, REQUEST_TAKE_BIG_PIC);
            }
            catch (Exception e){
                Log.i(TAG,"Exception at sendCameraIntentBigPic - "+e.getMessage());
            }
        }
        else
        {
            Log.i(TAG,"ERROR !!! NO INTENT TO RESOLVE THE REQUEST");
            Toast.makeText(this,"Error!! No Camera Application",Toast.LENGTH_LONG);
        }
    }

    public File setupFileForPic() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = PIC_PREFIX + timeStamp + "_";
        //File albumF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File albumF=new File(Environment.getExternalStorageDirectory()+"/DCIM/Selfies");
        if(!albumF.mkdirs()){
            if(!albumF.exists())
                Log.i(TAG,"Error!! Failed to create Seflie directoy");
        }
        File imageF = File.createTempFile(imageFileName, ".jpg", albumF);
        return imageF;
    }

    public void sendCameraIntentSmallPic(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_SMALL_PIC);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult is Called !!!");
        onActivityResultCalled=true;
        if (requestCode == REQUEST_TAKE_SMALL_PIC && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView=(ImageView)findViewById(R.id.image);
            mImageView.setImageBitmap(imageBitmap);
        }
        else if(requestCode == REQUEST_TAKE_BIG_PIC && resultCode == RESULT_OK){
                addGalleryPic();
        }
    }

    public void addGalleryPic(){
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentAbsolutePicPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onFragmentInteraction(ImageItem imageItem) {
        Toast.makeText(getApplicationContext(),"Opening "+imageItem.getImageId(),Toast.LENGTH_SHORT).show();
        sendImageViewerIntent(imageItem.getImageId());

    }
    public void sendImageViewerIntent(String imageId){
        Intent intent=new Intent(this,ImageViewActivity.class);
        intent.putExtra("imageId",imageId);
        startActivity(intent);
    }
}
