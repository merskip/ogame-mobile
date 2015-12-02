package pl.merskip.ogamemobile.game;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;

public class DownloadPageNotifier {

    /**
     * Słuchacz do obsługi po pobraniu strony
     */
    public interface DownloadPageListener {

        /**
         * Zdarzenie wywoływane po pobraniu strony
         */
        void onDownloadPage(AbstractPage<?> downloadPage);
    }

    private List<DownloadPageListener> listeners;

    public DownloadPageNotifier() {
        this.listeners = new ArrayList<>();
    }

    public void addListener(DownloadPageListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners(AbstractPage<?> downloadPage) {
        for (DownloadPageListener listener : listeners)
            listener.onDownloadPage(downloadPage);
    }
}
