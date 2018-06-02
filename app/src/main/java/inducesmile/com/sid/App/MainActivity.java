package inducesmile.com.sid.App;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import inducesmile.com.sid.Connection.ConnectionHandler;
import inducesmile.com.sid.DataBase.DataBaseHandler;
import inducesmile.com.sid.DataBase.DataBaseReader;
import inducesmile.com.sid.Helper.UserLogin;
import inducesmile.com.sid.R;

public class MainActivity extends AppCompatActivity {

    private static final String IP = UserLogin.getInstance().getIp();
    private static final String username = UserLogin.getInstance().getUsername();
    private static final String PORT = UserLogin.getInstance().getPort();
    private static final String password = UserLogin.getInstance().getPassword();
    DataBaseHandler db = new DataBaseHandler(this);
    public static final String LOG_IN = "http://" + IP + ":" + PORT + "/login.php";
    public static final String READ_HUMIDADE_TEMPERATURA = "http://" + IP + ":" + PORT + "/getHumidade_Temperatura.php";
    public static final String READ_ALERTAS = "http://" + IP + ":" + PORT + "/getAlertas.php";
    public static final String READ_Cultura = "http://" + IP + ":" + PORT + "/getCultura.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HashMap<String, String> params = new HashMap<>();
        params.put("url", LOG_IN);
        params.put("username", username);
        params.put("password", password);
        ConnectionHelper jParser = new ConnectionHelper();
        JSONArray jsonLogin = null;
        try {
            jsonLogin = jParser.execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject c = null;
        int code = 0;
        try {
            c = jsonLogin.getJSONObject(0);
            code = c.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (code == 1) {
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_login);
        }
        db.dbClear();
    }

    public void drawGraph(View v) {
        Intent i = new Intent(this, GraphicActivity.class);
        startActivity(i);

    }

    public void showAlertas(View v) {
        Intent i = new Intent(this, AlertasActivity.class);
        startActivity(i);
    }

    public void refreshDB(View v) {
        EditText idCultura = findViewById(R.id.idCultura);
        if (idCultura.getText() != null) {
            writeToDB(idCultura.getText().toString());
            idCultura.onEditorAction(EditorInfo.IME_ACTION_DONE);
            updateNomeCultura();
            updateNumeroMedicoes();
            updateNumeroAlertas();

        }
    }

    public void updateNumeroMedicoes() {

        //To Do

        DataBaseReader dbReader = new DataBaseReader(db);

        Cursor cursor = dbReader.ReadHumidadeTemperatura(null);
        int totalMedicoes = cursor.getCount();
        TextView text = findViewById(R.id.numeroMedicoesInt);
        text.setText(Integer.toString(totalMedicoes));

    }

    public void updateNumeroAlertas() {

        //To Do
        DataBaseReader dbReader = new DataBaseReader(db);

        Cursor cursor = dbReader.readAlertas();
        int totalAlertas = cursor.getCount();
        TextView text = findViewById(R.id.numeroAlertasInt);
        text.setText(Integer.toString(totalAlertas));

    }

    private void updateNomeCultura() {

        //To do?
        DataBaseReader dbReader = new DataBaseReader(db);

        TextView nomeCultura_tv = findViewById(R.id.nomeCultura_tv);
        Cursor cursor = dbReader.readCultura();
        String nomeCultura = null;
        while (cursor.moveToNext()) {
            nomeCultura = cursor.getString(cursor.getColumnIndex("NomeCultura"));
        }

        if (nomeCultura != null) {
            nomeCultura_tv.setText(nomeCultura);
            nomeCultura_tv.setTextColor(Color.BLACK);
        } else {
            nomeCultura_tv.setText("Cultura Invalida!");
            nomeCultura_tv.setTextColor(Color.RED);
        }

        nomeCultura_tv.setVisibility(View.VISIBLE);
    }

//A minha base de dados pode não ser exatamente igual à vossa ou podem concluir que é melhor implementar isto de outra maneira, para mudarem a base de dados no android usem as classes DatabaseConfig(criação) e DatabaseHandler(escrita)

    public void writeToDB(String idCultura) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HashMap<String, String> params = new HashMap<>();
            params.put("url", READ_HUMIDADE_TEMPERATURA);
            params.put("username", username);
            params.put("password", password);
            params.put("idCultura", idCultura);
            ConnectionHelper jParser = new ConnectionHelper();
            JSONArray jsonHumidadeTemperatura = null;
            try {
                jsonHumidadeTemperatura = jParser.execute(params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            db.dbClear();
            if (jsonHumidadeTemperatura != null) {
                for (int i = 0; i < jsonHumidadeTemperatura.length(); i++) {
                    JSONObject c = jsonHumidadeTemperatura.getJSONObject(i);
                    int idMedicao = c.getInt("IDMedicao");
                    String horaMedicao = c.getString("HoraMedicao");
                    double valorMedicaoTemperatura = c.getDouble("ValorMedicaoTemperatura");
                    double valorMedicaoHumidade = c.getDouble("ValorMedicaoHumidade");
                    String dataMedicao = c.getString("DataMedicao");
                    db.insert_Humidade_Temperatura(idMedicao, Integer.parseInt(idCultura), horaMedicao, valorMedicaoTemperatura, valorMedicaoHumidade, dataMedicao);
                }
            }

            params.remove("url");
            params.put("url", READ_ALERTAS);
            JSONArray jsonAlertas = null;
            jParser = new ConnectionHelper();
            try {
                jsonAlertas = jParser.execute(params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (jsonAlertas != null) {
                for (int i = 0; i < jsonAlertas.length(); i++) {
                    JSONObject c = jsonAlertas.getJSONObject(i);
                    int IDAlerta = c.getInt("IDAlerta");
                    String dataMedicao = c.getString("DataMedicao");
                    double valorMedicao = c.getDouble("ValorMedicao");
                    String horaMedicao = c.getString("HoraMedicao");
                    String nomeVariavel = c.getString("NomeVariavel");
                    String alerta = c.getString("Alerta");
                    String tipo = c.getString("Tipo");
                    db.insert_Alertas(IDAlerta, dataMedicao, valorMedicao, horaMedicao, nomeVariavel, alerta,tipo);
                }

            }

            params.remove("url");
            params.put("url", READ_Cultura);
            JSONArray jsonCultura = null;
            jParser = new ConnectionHelper();
            try {
                jsonCultura = jParser.execute(params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (jsonCultura != null) {
                for (int i = 0; i < jsonCultura.length(); i++) {
                    JSONObject c = jsonCultura.getJSONObject(i);
                    String nomeCultura = c.getString("NomeCultura");
                    db.insert_Cultura(Integer.parseInt(idCultura), nomeCultura);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class ConnectionHelper extends AsyncTask<HashMap<String, String>, Void, JSONArray> {


        protected JSONArray doInBackground(HashMap<String, String>... params ) {

            ConnectionHandler jParser = new ConnectionHandler();
            String url = params[0].get("url");
            params[0].remove("url");
            JSONArray jsonOutput = jParser.getJSONFromUrl(url, params[0]);
            return jsonOutput;
        }



    }

}
