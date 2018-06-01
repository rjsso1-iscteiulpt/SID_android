package inducesmile.com.sid.Connection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.util.HashMap;

import inducesmile.com.sid.App.MainActivity;

public class ConnectionHelper extends AsyncTask<HashMap<String, String>, Void, JSONArray> {


    protected JSONArray doInBackground(HashMap<String, String>... params ) {

        ConnectionHandler jParser = new ConnectionHandler();
        String url = params[0].get("url");
        params[0].remove("url");
        JSONArray jsonOutput = jParser.getJSONFromUrl(url, params[0]);
        return jsonOutput;
    }



}
