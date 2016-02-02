package pl.merskip.ogamemobile.adapter.game;

/**
 * Wynik bez tworzenia wyniku
 */
public class EmptyResult extends ResultPage<Void> {
    @Override
    protected Void onCreateResult() {
        return null;
    }
}
