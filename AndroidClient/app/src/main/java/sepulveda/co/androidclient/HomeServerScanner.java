package sepulveda.co.androidclient;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

public class HomeServerScanner extends AsyncTask {

    private HomeServerScannerListener listener;
    private final static String PING_PATH = "/home-automation/ping";

    public HomeServerScanner(HomeServerScannerListener listener) {
        this.listener = listener;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            return getHomeServerIp();
        } catch(Exception e) {
            return null;
        }
    }

    public String getHomeServerIp() throws IOException {
        String networkPrefix = getNetworkPrefix();

        // Loop to scan each address on the local subnet
        for (int i = 1; i < 255; i++) {
            String ip = (networkPrefix + i);
            System.out.println("Scanning " + ip);
            boolean isOpenHost = isOpenHost(ip, 80);
            if (isOpenHost && isHomeServer(ip)) {
                return ip;
            }
        }

        return null;
    }

    private String getNetworkPrefix() throws IOException {
        String iIPv4 = getLocalIp();
        if (iIPv4 == null) {
            throw new IOException("Network not found");
        }

        String network = iIPv4.substring(iIPv4.indexOf("/") + 1);
        String networkPrefix = iIPv4.substring(0, iIPv4.lastIndexOf("."));
        networkPrefix += ".";

        return networkPrefix;
    }

    private String getLocalIp() throws SocketException {
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (n.getName().equals("wlan0") && ee.hasMoreElements()) {
                InetAddress i = ee.nextElement();
                if (i.getHostAddress().startsWith("192.168.")) {
                    return i.getHostAddress();
                }
            }
        }

        return null;
    }

    private boolean isOpenHost(String ip, int port) {
        try {
            SocketAddress sockaddr = new InetSocketAddress(ip, port);
            Socket socket = new Socket();
            socket.connect(sockaddr, 300);
            closeSocket(socket);

            return true;
        } catch(Exception e) {
            return false;
        }
    }

    private void closeSocket(Socket socket) {
        try {socket.close();} catch (Exception e) {}
    }

    private boolean isHomeServer(String ip) {
        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 2000);
            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpGet httpget = new HttpGet("http://" + ip + PING_PATH);
            HttpResponse response = httpclient.execute(httpget);
            int status = response.getStatusLine().getStatusCode();

            return status == 200;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        listener.onFinish(String.valueOf(result));
    }
}
