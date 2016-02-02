package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.nodes.Document;

import java.io.IOException;

import pl.merskip.ogamemobile.adapter.ScriptData;

/**
 * Parsowanie pobranej strony
 */
public abstract class ResultPage<Result> {

    protected RequestPage request;

    protected Document document;
    protected ScriptData scriptData;

    protected Result result;

    public RequestPage getRequest() {
        return request;
    }

    public Document getDocument() {
        return document;
    }

    public ScriptData getScriptData() {
        return scriptData;
    }

    /**
     * Tworzy wynik pobierając dokument za pomocą przekazanego RequestPage
     * @param request Sposób pobrania strony
     * @return this
     */
    public ResultPage createFromRequest(RequestPage request)
            throws IOException, RequestPage.UnexpectedLogoutException {
        Document document = request.download();
        createResult(document, request);
        return this;
    }

    public Result createResult(Document document, RequestPage request) {
        this.request = request;
        this.document = document;

        scriptData = new ScriptData(document);
        if (scriptData.getContent() == null)
            scriptData = null;

        result = onCreateResult();
        return result;
    }

    protected abstract Result onCreateResult();

    public Result getResult() {
        return result;
    }

}
