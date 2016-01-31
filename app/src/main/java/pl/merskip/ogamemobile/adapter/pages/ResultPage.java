package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.nodes.Document;

import pl.merskip.ogamemobile.adapter.ScriptData;

/**
 * Parsowanie pobranej strony
 */
public abstract class ResultPage<Result> {

    protected RequestPage request;

    protected Document document;
    protected ScriptData scriptData;

    public RequestPage getRequest() {
        return request;
    }

    public Document getDocument() {
        return document;
    }

    public ScriptData getScriptData() {
        return scriptData;
    }

    public Result createResult(Document document, RequestPage request) {
        this.request = request;
        this.document = document;

        scriptData = new ScriptData(document);
        if (scriptData.getContent() == null)
            scriptData = null;

        return onCreateResult();
    }

    protected abstract Result onCreateResult();

}
