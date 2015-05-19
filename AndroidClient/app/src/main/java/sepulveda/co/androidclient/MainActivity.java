package sepulveda.co.androidclient;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements CommandListener, HomeServerScannerListener {

    private static final String TTS_LANGUAGE = "es-ES";
    private static final String SPEECH_INPUT_LANGUAGE = "es-ES";
    private ImageButton btnSpeak;
    private ProgressBar progressBar;
    private TextToSpeech textToSpeechController;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("INIT", "Initializing app");

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        initializeTTSController();
    }


    private void initializeTTSController() {
        Log.i("INIT", "Initializing TTS controller");
        final MainActivity activity = this;
        textToSpeechController = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            Locale locale = new Locale(TTS_LANGUAGE);
                            textToSpeechController.setLanguage(locale);
                            activity.onInit();
                        }
                    }
                });
    }

    private void onInit() {
        scanHomeServer();
    }

    private void scanHomeServer() {
        TextView scanLabel = (TextView) findViewById(R.id.scanServerLabel);
        ProgressBar scanBar = (ProgressBar) findViewById(R.id.progressScan);

        scanLabel.setText("Buscando servidor ...");
        scanLabel.setVisibility(View.VISIBLE);
        scanBar.setVisibility(View.VISIBLE);

        new HomeServerScanner(this).execute();
    }

    public void onClickScanButton(View view) {
        view.setVisibility(View.INVISIBLE);
        scanHomeServer();
    }

    public void onClickSpeechButton(View view) {
        showSpeechInput();
    }

    /**
     * It shows google speech input dialog
     **/
    private void showSpeechInput() {
        try {
            Intent speechIntent = createSpeechIntent();
            startActivityForResult(speechIntent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            showUnSupportedSpeechInput();
        }
    }

    private Intent createSpeechIntent() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, SPEECH_INPUT_LANGUAGE);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        return intent;
    }

    private void showUnSupportedSpeechInput() {
        Toast.makeText(getApplicationContext(),
                getString(R.string.speech_not_supported),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                processSpeechResult(resultCode, data);
                break;
            }

        }
    }

    private void processSpeechResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && null != data) {
            progressBar.setVisibility(View.VISIBLE);
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String txt = result.get(0);
            new CommandExecutor(this, txt).execute(null);
        }
    }

    @Override
    public void onCommandExecuted(int code, String command) {
        String msg = "Ocurrió un error desconocido";

        if (code == 200) {
            msg = "Comando ejecutado satisfactoriamente : " + command;
        }
        if (code == 404) {
            msg = "Comando no encontrado : " + command;
        }
        if (code == 500) {
            msg = "Ocurrió un error al intentar ejecutar el comando : " + command;
        }

        progressBar.setVisibility(View.INVISIBLE);
        textToSpeechController.speak(msg , TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onFinish(String ip) {
        TextView scanLabel = (TextView) findViewById(R.id.scanServerLabel);
        ProgressBar scanBar = (ProgressBar) findViewById(R.id.progressScan);
        scanBar.setVisibility(View.INVISIBLE);

        if (ip == null || ip.equals("null")) {
            System.out.println("Hide everything");
            ((View) findViewById(R.id.scanButton)).setVisibility(View.VISIBLE);
            View layout = (View) findViewById(R.id.linearLayout);
            layout.setVisibility(View.INVISIBLE);
            scanLabel.setText("Servidor no encontrado");
            scanLabel.setVisibility(View.VISIBLE);
        } else {
            ((View) findViewById(R.id.scanButton)).setVisibility(View.INVISIBLE);
            View layout = (View) findViewById(R.id.linearLayout);
            layout.setVisibility(View.VISIBLE);
            scanLabel.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
