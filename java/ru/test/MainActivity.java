package ru.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity
{
    private SearchView m_search_view;
    private SearchView.OnQueryTextListener m_search_listener = new SearchView.OnQueryTextListener()
    {
        @Override
        public boolean onQueryTextSubmit(String query)
        {
            new ItunesRequests().get_itunes_items(query);

            m_search_view.clearFocus();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText)
        {
            return false;
        }
    };
    private SearchView.OnCloseListener m_search_close_listener = new SearchView.OnCloseListener()
    {
        @Override
        public boolean onClose()
        {
            ItunesManager.instance().clear();
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        m_search_view = (SearchView) findViewById(R.id.itunes_search);
        m_search_view.setOnQueryTextListener(m_search_listener);
        m_search_view.setOnCloseListener(m_search_close_listener);
        ((ListView) findViewById(R.id.itunes_list)).setAdapter(new ItunesList(getApplicationContext()));
    }
}
