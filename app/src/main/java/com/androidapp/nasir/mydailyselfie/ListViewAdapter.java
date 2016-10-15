package com.androidapp.nasir.mydailyselfie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NasirAhmed on 16-Jul-15.
 */
public class ListViewAdapter extends ArrayAdapter<ImageItem> {
    public static String TAG="Adapter";
    private List<ImageItem> listImageItem;

    public ListViewAdapter(Context context){
    super(context,R.layout.fragment_item_list);
    listImageItem=new ArrayList<ImageItem>();
    }

    @Override
    public int getCount() {

        return listImageItem.size();

    }
    public void add(ImageItem imageItem){
    listImageItem.add(imageItem);
    }
    public boolean listContainsPic(String picId){
        for(ImageItem iItem:listImageItem){
            if(iItem.getImageId().equals(picId))
                return true;
        }
        return false;
    }
    @Override
    public ImageItem getItem(int position) {
        if(listImageItem!=null)
            return listImageItem.get(position);
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView() called ");
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fragment_item_list, parent, false);
            if(getCount()==1){
              HomeActivity.mImageIconView=(ImageView) convertView.findViewById(R.id.imageIcon);
            }
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.iIcon = (ImageView) convertView.findViewById(R.id.imageIcon);
            viewHolder.iTitle = (TextView) convertView.findViewById(R.id.imageDesc);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        ImageItem item = getItem(position);

        viewHolder.iIcon.setImageBitmap(item.getImage());
        viewHolder.iTitle.setText(String.valueOf(item.getImageId()));

        return convertView;
    }

    /**
     * The view holder design pattern prevents using findViewById()
     * repeatedly in the getView() method of the adapter.
     *
     * @see http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
     */
    private static class ViewHolder {
        ImageView iIcon;
        TextView iTitle;
    }

}
