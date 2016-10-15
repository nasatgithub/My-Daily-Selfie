package com.androidapp.nasir.mydailyselfie;

import android.app.Activity;
import android.app.ListFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends ListFragment {
    public static String TAG="Item-Fragment";

    private String mCurrentAbsolutePicPath;
    private OnFragmentInteractionListener mListener;
    public static ListViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Change Adapter to display your content
        mAdapter=new ListViewAdapter(getActivity().getApplicationContext());
        setListAdapter(mAdapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        Log.i(TAG,"Resuming Fragment !!");
        loadSelfiePics();
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG,"Pausing Fragment !!");
        super.onPause();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(ImageItem imageItem);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onFragmentInteraction(mAdapter.getItem(position));
    }

    public void loadSelfiePics(){
        File selfieFolder=new File(Environment.getExternalStorageDirectory()+"/DCIM/Selfies");
        Log.i(TAG,"selfieFolder.exists() = "+selfieFolder.exists());
        if(selfieFolder.exists()) {
            for (File f : selfieFolder.listFiles()) {

                mCurrentAbsolutePicPath = f.getAbsolutePath();
                Log.i(TAG, "Photo at " + mCurrentAbsolutePicPath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                //Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
                if(!mAdapter.listContainsPic(f.getName()))
                    loadScaledPic(f.getName());
            }
        }
    }
    public void loadScaledPic(String picName){
        /* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = 64;
        int targetH = 64;

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentAbsolutePicPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        //   bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentAbsolutePicPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        mAdapter.add(new ImageItem(picName, bitmap));
        mAdapter.notifyDataSetChanged();

    }
}
