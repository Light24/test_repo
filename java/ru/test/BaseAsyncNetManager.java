package ru.test;

import android.os.AsyncTask;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.util.Pair;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * Created by Light on 16.09.2016.
 */

public abstract class BaseAsyncNetManager
{
    public abstract String getUri();

    public class AsyncRequest extends AsyncTask<Pair<Request, ResponseObject>, Void, Pair<Request, ResponseObject>>
    {
        protected Pair<Request, ResponseObject> doInBackground(Pair<Request, ResponseObject>... args)
        {
            Request request = args[0].first;
            ResponseObject response = args[0].second;
            response.data(RequestObject.doRequest(getUri(), request));
            return new Pair(request, response);
        }

        protected void onPostExecute(Pair<Request, ResponseObject> args)
        {
            ResponseObject responseObject = args.second;
            responseObject.doResponse(args.first);
        }
    }

    public class Request
    {
        private Map<String, String> m_encodeData = new HashMap<String, String>();
        private Object m_object;

        private String m_file_path;
        private String m_file_name;


        public Request()
        {

        }


        public void addItem(String key, String value)
        {
            m_encodeData.put(key, value);
        }

        public void setObject(Object object) {
            m_object = object;
        }

        public void setFilePath(String filePath)
        {
            m_file_path = filePath;
        }

        public void setFileName(String fileName)
        {
            m_file_name = fileName;
        }


        public Map<String, String> getItems()
        {
            return m_encodeData;
        }

        public Object getObject() {
            return this.m_object;
        }

        public String getFileName()
        {
            return m_file_name;
        }

        public String getFilePath()
        {
            return m_file_path;
        }

        public String getFileType()
        {
            return "image/jpeg";
        }
    }

    public static class RequestObject
    {
        public static String doRequest(String urlTo, Request request)
        {
            Map<String, String> parmas = request.getItems();
            String filepath = request.getFilePath();
            String filefield = request.getFileName();
            String fileMimeType = request.getFileType();

            Log.d("myLogs-doRequest", "url: " + urlTo);
            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;
            InputStream inputStream = null;

            String twoHyphens = "--";
            String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
            String lineEnd = "\r\n";

            String result = "";

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;


            try {
                URL url = new URL(urlTo);
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                outputStream = new DataOutputStream(connection.getOutputStream());

                if (filepath != null) {
                    String[] q = filepath.split("/");
                    int idx = q.length - 1;

                    File file = new File(filepath);
                    FileInputStream fileInputStream = new FileInputStream(file);


                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
                    outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
                    outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

                    outputStream.writeBytes(lineEnd);

                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {
                        outputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    outputStream.writeBytes(lineEnd);
                }

                Iterator<String> keys = parmas.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = parmas.get(key);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                    outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.write(value.getBytes("UTF-8"));
                    outputStream.writeBytes(lineEnd);
                }

                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                if (200 != connection.getResponseCode()) {
                    return null;
                }

                inputStream = connection.getInputStream();
                int readBytesTotal = 0;
                int bytesSize = AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT;
                byte[] bytes = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                while (true) {
                    int readBytes = inputStream.read(bytes, readBytesTotal, bytesSize - readBytesTotal);
                    if (readBytes > 0) {
                        readBytesTotal += readBytes;
                        if (bytesSize - readBytesTotal <= 100) {
                            byte[] _bytes = bytes;
                            bytesSize *= 5;
                            bytes = new byte[bytesSize];
                            int index = -1;
                            for (byte _byte : _bytes) {
                                index++;
                                bytes[index] = _byte;
                            }
                        }
                    } else {
                        inputStream.close();
                        connection.disconnect();
                        String str2 = new String(bytes, 0, readBytesTotal, "UTF-8");
                        Log.d("myLogs", "response: " + str2);
                        return str2;
                    }
                }
            } catch (IOException e) {
                Log.d("myLogs", "error: " + urlTo + "e " + e.getMessage() + "e.getStackTrace " + e.getStackTrace().toString());
                return null;
            }

        }
    }

    public abstract class ResponseObject
    {
        private String m_data;


        public abstract void doResponse(Request request);

        public void data(String data) {
            this.m_data = data;
        }

        public String data() {
            return this.m_data;
        }
    }
}
