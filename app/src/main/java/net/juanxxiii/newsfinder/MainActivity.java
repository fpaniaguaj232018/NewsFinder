package net.juanxxiii.newsfinder;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buscar(View v){
        if (!isOnline()){
            Toast.makeText(this, "No estás online, chaval", Toast.LENGTH_SHORT).show();
            return;
        }
        String texto = ((EditText)findViewById(R.id.etURL)).getText().toString();
        new URLReaderTaskInner().execute(texto);
    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }

    public class URLReaderTaskInner extends AsyncTask<String, Integer, StringBuilder>{
        TextView tvProgreso;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
            tvProgreso = (TextView)findViewById(R.id.tvProgreso);
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected StringBuilder doInBackground(String... strings) {
            int contador = 0;
            HttpURLConnection urlConnection = null;
            StringBuilder sb = new StringBuilder();
            String linea;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while((linea = br.readLine())!=null){
                    sb.append(linea);
                    publishProgress(contador++);
                }
            } catch (MalformedURLException mue){
                Log.e("PANIAGUA", mue.getMessage());
            } catch (IOException ioe){
                Log.e("PANIAGUA", ioe.getMessage());
            }
            return sb;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            tvProgreso.setText("Leyendo linea " + values[0]);
        }

        @Override
        protected void onPostExecute(StringBuilder stringBuilder) {
            super.onPostExecute(stringBuilder);
            ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
            pb.setVisibility(View.INVISIBLE);
            String texto = ((EditText)findViewById(R.id.etPalabra)).getText().toString();
            int numeroPalabras = ContadorDePalabras.contarPalabras(stringBuilder, texto);
            ((TextView)findViewById(R.id.tvResultado)).setText(String.valueOf(numeroPalabras));
        }
    }



}
