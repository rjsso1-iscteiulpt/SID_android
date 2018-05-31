package inducesmile.com.sid.Connection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.util.HashMap;

import inducesmile.com.sid.App.MainActivity;

public class ConnectionHelper extends AsyncTask<HashMap<String, String>, Void, JSONArray> {


    protected JSONArray doInBackground(HashMap<String, String>... params ) {

        ConnectionHandler jParser = new ConnectionHandler();
        JSONArray jsonHumidadeTemperatura = jParser.getJSONFromUrl(MainActivity.READ_HUMIDADE_TEMPERATURA /*params[0].get("url")*/, params[0]);
        Log.d("afafef", "doInBackground: ");
        return jsonHumidadeTemperatura;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(Long result) {
    }

}
