package pl.merskip.ogamemobile.game;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.game.ResultPage;

public class DownloadPageNotifier {

    /**
     * Słuchacz do obsługi po pobraniu strony
     */
    public interface DownloadPageListener {

        /**
         * Zdarzenie wywoływane po pobraniu strony
         */
        void onDownloadPage(ResultPage resultPage);
    }

    private List<DownloadPageListener> listeners;

    public DownloadPageNotifier() {
        this.listeners = new ArrayList<>();
    }

    public void addListener(DownloadPageListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners(ResultPage resultPage) {
        for (DownloadPageListener listener : listeners)
            listener.onDownloadPage(resultPage);
    }
}
