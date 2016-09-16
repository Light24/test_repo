package ru.test;

import android.util.Log;

import javax.json.JsonObject;

/**
 * Created by Light on 16.09.2016.
 */
public class ItunesItem
{
    private static final String JSON_TAG_NAME_TRACK_NAME      = "trackName";
    private static final String JSON_TAG_NAME_COLLECTION_NAME = "collectionName";
    private static final String JSON_TAG_NAME_ATWORK_URL_160  = "artworkUrl60";
    private static final String JSON_TAG_NAME_ATWORK_URL_1100 = "artworkUrl100";

    private String m_name;
    private String m_album;
    private String m_image_uri;


    public void fromJson(JsonObject json)
    {
        if (json.containsKey(JSON_TAG_NAME_TRACK_NAME))
            setName(json.getString(JSON_TAG_NAME_TRACK_NAME));
        else
            setName("");

        if (json.containsKey(JSON_TAG_NAME_COLLECTION_NAME))
            setAlbum(json.getString(JSON_TAG_NAME_COLLECTION_NAME));
        else
            setAlbum("");

        if (json.containsKey(JSON_TAG_NAME_ATWORK_URL_1100))
            setImageUri(json.getString(JSON_TAG_NAME_ATWORK_URL_1100));
        else if (json.containsKey(JSON_TAG_NAME_ATWORK_URL_160))
            setImageUri(json.getString(JSON_TAG_NAME_ATWORK_URL_160));
    }


    private void setName(String name)
    {
        m_name = name;
    }

    private void setAlbum(String album)
    {
        m_album = album;
    }

    private void setImageUri(String imageUri)
    {
        m_image_uri = imageUri;
    }


    public String getName()
    {
        return m_name;
    }

    public String getAlbum()
    {
        return m_album;
    }

    public boolean haveImage()
    {
        return (m_image_uri != null);
    }

    public String getImageUri()
    {
        return m_image_uri;
    }
}
