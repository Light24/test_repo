package ru.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.test.Listeners.OnAdapterDataSetChanged;

/**
 * Created by Light on 16.09.2016.
 */
public class ItunesList extends BaseAdapter implements OnAdapterDataSetChanged
{
    private Context m_context;

    public ItunesList(Context context)
    {
        m_context = context;
        ItunesManager.instance().setChangeDataNotify(this);
    }

    @Override
    public int getCount()
    {
        return ItunesManager.instance().getCount();
    }

    @Override
    public ItunesItem getItem(int position)
    {
        return ItunesManager.instance().get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
            convertView = ((LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.main_itunes_item, parent, false);

        ItunesItem item = getItem(position);

        ((TextView) convertView.findViewById(R.id.itunes_item_name)).setText(item.getName());
        ((TextView) convertView.findViewById(R.id.itunes_item_album)).setText(item.getAlbum());
        ImageView preview = (ImageView) convertView.findViewById(R.id.itunes_item_preview);

        if (item.haveImage())
            Picasso.with(m_context).load(item.getImageUri()).into(preview);

        return convertView;
    }

    @Override
    public void notifyDataSetChanged()
    {
        Log.d("myLogs", "notifyDataSetChanged");
        super.notifyDataSetChanged();
    }
}
