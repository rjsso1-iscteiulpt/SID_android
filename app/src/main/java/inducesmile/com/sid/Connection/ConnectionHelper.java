package inducesmile.com.sid.Connection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.util.HashMap;

import inducesmile.com.sid.App.MainActivity;

public class ConnectionHelper extends AsyncTask<HashMap<String, String>, Void, JSONArray> {


    protected JSONArray doInBackground(HashMap<String, String>... params ) {

        ConnectionHandler jParser = new ConnectionHandler();
        Log.d("testing_url", params[0].get("url"));
        JSONArray jsonOutput = jParser.getJSONFromUrl(params[0].get("url"), params[0]);
        Log.d("testing_json", jsonOutput.toString());
        return jsonOutput;
    }



}
