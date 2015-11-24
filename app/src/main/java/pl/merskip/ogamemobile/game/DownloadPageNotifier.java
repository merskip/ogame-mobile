package pl.merskip.ogamemobile.game;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import pl.merskip.ogamemobile.adapter.ScriptData;
import pl.merskip.ogamemobile.adapter.pages.AbstractPage;

public class DownloadPageNotifier {

    /**
     * Słuchacz do obsługi po pobraniu strony
     */
    public interface DownloadPageListener {

        /**
         * Zdarzenie wywoływane po pobraniu strony
         */
        void onDownloadPage(Document document, ScriptData scriptData);
    }

    private List<DownloadPageListener> listeners;

    public DownloadPageNotifier() {
        this.listeners = new ArrayList<>();
    }

    public void addListener(DownloadPageListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners(AbstractPage<?> downloadPage) {
        Document document = downloadPage.getDocument();
        ScriptData scriptData = downloadPage.getScriptData();

        for (DownloadPageListener listener : listeners)
            listener.onDownloadPage(document, scriptData);
    }
}
