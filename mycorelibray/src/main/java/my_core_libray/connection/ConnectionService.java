package my_core_libray.connection;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import my_core_libray.model.StatusDataConnection;

/**
 * Created by l.buffetti on 15/03/2018.
 */

public class ConnectionService {

    private Context context;
    public final String TAG = this.getClass().getSimpleName() + " ";

    //If server is protected -> isAuthorization = true;
    private boolean isAuthorization = false;
    private String authStr = "";
    private String basicAuth = "Basic " + new String(Base64.encode(authStr.getBytes(),Base64.NO_WRAP));

    //check connection Wifi or Mobile
    private boolean getStateConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public StatusDataConnection setGetServiceWithParams(String urlStr, HashMap<String,String> postDataParams){
        StatusDataConnection statusDataConnection = new StatusDataConnection();
        statusDataConnection.setError(false);
        if(getStateConnection()) {
            StringBuilder stringBuffer = new StringBuilder("");
            URL url;
            try{
                url = new URL(urlStr + "?"+ getPostDataString(postDataParams));
                HttpURLConnection connTx = (HttpURLConnection) url.openConnection();
                if(isAuthorization) {
                    connTx.setRequestProperty("Authorization", basicAuth);
                }
                connTx.setReadTimeout(15000);
                connTx.setConnectTimeout(15000);
                connTx.setRequestMethod("GET");
                connTx.setRequestProperty("Content-Type", "x-www-form-urlencoded");
                connTx.connect();
                int responseCode=connTx.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connTx.getInputStream()));
                    String line = "";
                    while ((line=bufferedReader.readLine())!= null){
                        stringBuffer.append(line);
                    }
                }
                connTx.disconnect();
            }catch (Exception e){
                Log.e(TAG + "Error connection", e.getMessage());
                statusDataConnection.setMessageError("Error connection" + e.getMessage());
                statusDataConnection.setError(true);
            }
            if(stringBuffer.length()>0){
                statusDataConnection.setJsonObject(stringBuffer.toString());
            }else {
                statusDataConnection.setJsonObject("");
            }
        }else{
            statusDataConnection.setMessageError("Error connection" + " - no internet ");
            statusDataConnection.setError(true);
        }
        return statusDataConnection;
    }


    public StatusDataConnection setJsonPostService(String urlStr, HashMap<String,String> postDataParams, JSONObject postDataJsonParams, boolean typeOutPutMediaType){
        StatusDataConnection statusDataConnection = new StatusDataConnection();
        statusDataConnection.setError(false);
        if(getStateConnection()) {
            StringBuilder stringBuffer = new StringBuilder("");
            URL url;
            try{
                url = new URL(urlStr);
                OutputStream os = null;

                HttpURLConnection connTx = (HttpURLConnection) url.openConnection();
                if(isAuthorization) {
                    connTx.setRequestProperty("Authorization", basicAuth);
                }
                connTx.setReadTimeout(15000);
                connTx.setConnectTimeout(15000);
                connTx.setRequestMethod("POST");
                connTx.setDoInput(true);
                connTx.setDoOutput(true);
                if(typeOutPutMediaType){ //if parameter is type json
                    connTx.setRequestProperty("Content-Type", "application/json");
                    String messagejson = postDataJsonParams.toString();
                    connTx.setFixedLengthStreamingMode(messagejson.getBytes().length);
                    //open
                    connTx.connect();
                    //setup send
                    os = new BufferedOutputStream(connTx.getOutputStream());
                    os.write(messagejson.getBytes());
                    //clean up
                    os.flush();
                    os.close();
                }else{
                    os = connTx.getOutputStream();
                    connTx.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connTx.setRequestProperty("charset", "utf-8");
                    connTx.setRequestProperty("Content-Length", Integer.toString(postDataParams.toString().getBytes().length));
                    connTx.setUseCaches(false);
                    //open
                    connTx.connect();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                }
                int responseCode=connTx.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connTx.getInputStream()));
                    String line = "";
                    while ((line=bufferedReader.readLine())!= null){
                        stringBuffer.append(line);
                    }
                }
                connTx.disconnect();
            }catch (Exception e){
                Log.e(TAG + "Error connection", e.getMessage());
                statusDataConnection.setMessageError("Error connection" + e.getMessage());
                statusDataConnection.setError(true);
            }
            if(stringBuffer.length()>0){
                statusDataConnection.setJsonObject(stringBuffer.toString());
            }else {
                statusDataConnection.setJsonObject("");
            }
        }else{
            statusDataConnection.setMessageError("Error connection" + " - no internet ");
            statusDataConnection.setError(true);
        }
        return statusDataConnection;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first) {
                first = false;
            }
            else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }



    public StatusDataConnection setPostService(Context context, String url, String authStr, boolean isAuthorization, HashMap<String, String> postDataParams, JSONObject postDataJsonParams, boolean typeOutPutMediaType){
        this.context = context;
        this.authStr = authStr;
        this.isAuthorization = isAuthorization;
        return setJsonPostService(url, postDataParams, postDataJsonParams, typeOutPutMediaType);
    }

    public StatusDataConnection setGetService(Context context, String url, String authStr, boolean isAuthorization, HashMap<String, String> postDataParams){
        this.context = context;
        this.authStr = authStr;
        this.isAuthorization = isAuthorization;
        return setGetServiceWithParams(url, postDataParams);
    }


}
