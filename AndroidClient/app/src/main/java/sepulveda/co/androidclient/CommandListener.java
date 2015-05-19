package sepulveda.co.androidclient;

/**
 * Created by carlossepulveda on 19/05/15.
 */
public interface CommandListener {
    void onCommandExecuted(int responseCode, String executedCommand);
}
