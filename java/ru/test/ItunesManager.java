package ru.test;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import ru.test.Listeners.OnAdapterDataSetChanged;

/**
 * Created by Light on 16.09.2016.
 */
public class ItunesManager
{
    private static ItunesManager m_instance;
    private WeakReference<OnAdapterDataSetChanged> m_on_adapter_data_set_changed;
    private List<ItunesItem> m_items;


    private ItunesManager()
    {
        m_items = new ArrayList<ItunesItem>();
    }

    public static ItunesManager instance()
    {
        if (m_instance == null)
            m_instance = new ItunesManager();

        return m_instance;
    }


    public void fromJson(JsonObject json)
    {
        clear();

        JsonArray jsonArray = json.getJsonArray("results");
        Log.d("myLog", "jsonArray.size() " + jsonArray.size());
        for (int index = 0; index != jsonArray.size(); ++index)
        {
            JsonObject jsonItem = jsonArray.getJsonObject(index);
            ItunesItem item = new ItunesItem();
            item.fromJson(jsonItem);
            m_items.add(item);
        }
        notifyDataChange();
    }


    public void setChangeDataNotify(OnAdapterDataSetChanged onAdapterDataSetChanged)
    {
        m_on_adapter_data_set_changed = new WeakReference<>(onAdapterDataSetChanged);
    }

    public void notifyDataChange()
    {
        if (m_on_adapter_data_set_changed != null && m_on_adapter_data_set_changed.get() != null)
            m_on_adapter_data_set_changed.get().notifyDataSetChanged();
    }


    public void clear()
    {
        m_items.clear();
        notifyDataChange();
    }

    public void add(ItunesItem item)
    {
        m_items.add(item);
        notifyDataChange();
    }


    public ItunesItem get(int index)
    {
        return m_items.get(index);
    }

    public int getCount()
    {
        return m_items.size();
    }
}
