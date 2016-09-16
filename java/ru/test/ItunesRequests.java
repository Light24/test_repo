package ru.test;

import android.util.Log;
import android.util.Pair;

import javax.json.Json;
import javax.json.JsonReader;

import java.io.Reader;
import java.io.StringReader;

/**
 * Created by Light on 16.09.2016.
 */
public class ItunesRequests extends BaseAsyncNetManager
{
    private static final Integer REQUEST_LIMIT = 300;
    private static final String URI = "https://itunes.apple.com/search";

    public String getUri()
    {
        return URI;
    }


    public void get_itunes_items(String term)
    {
        AsyncRequest currentRequest = new AsyncRequest();
        Request requestData = new Request();
        requestData.addItem("limit", String.valueOf(REQUEST_LIMIT));
        requestData.addItem("term", term);
        currentRequest.execute(new Pair[]{new Pair(requestData, new ResponseObjectGetItunesItems())});
        Log.d("myLogs", "get_itunes_items");
    }

    class ResponseObjectGetItunesItems extends ResponseObject
    {
        public void doResponse(Request requestData)
        {
            Reader reader = new StringReader(data());
            JsonReader json = Json.createReader(reader);
            ItunesManager.instance().fromJson(json.readObject());
        }
    }
}
