package pl.merskip.ogamemobile.adapter.pages;

/**
 * Wynik bez tworzenia wyniku
 */
public class EmptyResult extends ResultPage<Void> {
    @Override
    protected Void onCreateResult() {
        return null;
    }
}
