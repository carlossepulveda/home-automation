package sepulveda.co.androidclient;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by carlossepulveda on 19/05/15.
 */
public class CommandExecutor extends AsyncTask {

    public static final String SERVER_PATH = "http://192.168.1.107:80";
    private CommandListener listener;
    private String command;

    public CommandExecutor(CommandListener listener, String command) {
        this.listener = listener;
        this.command = command;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String msg = "";
        try {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(SERVER_PATH + "/control/voice/command");

            JSONObject json = new JSONObject();
            json.put("command", command);
            StringEntity se = new StringEntity( json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            int status = response.getStatusLine().getStatusCode();

            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
    }


    /*
     * It processes the result returned by doInBackground
     */
    @Override
    protected void onPostExecute(Object result) {
        int response = Integer.parseInt(result + "");
        listener.onCommandExecuted(response, command);
    }
}
