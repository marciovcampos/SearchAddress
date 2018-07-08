package br.com.marcio.searchaddress;

import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // DECLARAÇÃO DOS OBJETOS VISUAIS
    private EditText edtZipCode;
    private TextInputEditText edtAddressLine1;
    private TextInputEditText edtAddressLine2;
    private TextInputEditText edtAddressLine3;
    private TextInputEditText edtCity;
    private TextInputEditText edtState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // REFERÊNCIA AOS OBJETOS VISUAIS USANDO A CLASSE R
        edtZipCode = findViewById(R.id.edtZipCodeId);
        edtAddressLine1 = findViewById(R.id.edtAddressLine1Id);
        edtAddressLine2 = findViewById(R.id.edtAddressLine2Id);
        edtAddressLine3 = findViewById(R.id.edtAddressLine3Id);
        edtCity = findViewById(R.id.edtCityId);
        edtState = findViewById(R.id.edtStateId);
    }

    // EXIBE UMA MENSAGEM TOAST PARA O USUÁRIO
    public void print(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    // PROCEDURE TO EXECUTE THE ONCLICK OF THE BUTTON
    public void onClickSearch(View view){

        String cep = edtZipCode.getText().toString();

        if(cep == null || cep.equals("")){
            print(getString(R.string.errorZipCodeRequired));
        }else {

            WebServiceEndereco webServiceEndereco = new WebServiceEndereco();
            webServiceEndereco.execute(cep);
        }
    }

    // CLASS TO EXECUTE AsyncTask
    public class WebServiceEndereco extends AsyncTask<String, Void, String> {

        //METHOD THAT MAKES HTTP REQUIREMENT
        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://viacep.com.br/ws/" + strings[0] + "/json/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String linha;
                StringBuffer buffer = new StringBuffer();
                while((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                    buffer.append("\n");
                }

                return buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return null;
        }

        // METHOD THAT CONFIGURE THE RESPONSE OF THE HTTP METHOD
        @Override
        protected void onPostExecute(String s) {

            if(s == null)
                print(getString(R.string.errorRetriveData));
            else {
                try {

                    JSONObject json = new JSONObject(s);

                    edtAddressLine1.setText(json.getString("logradouro"));
                    edtAddressLine2.setText(json.getString("complemento"));
                    edtAddressLine3.setText(json.getString("bairro"));
                    edtCity.setText(json.getString("localidade"));
                    edtState.setText(json.getString("uf"));

                    print(getString(R.string.sucessRetriveData));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class WebAPI extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
