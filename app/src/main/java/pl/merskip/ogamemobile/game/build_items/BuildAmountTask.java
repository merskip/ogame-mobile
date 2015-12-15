package pl.merskip.ogamemobile.game.build_items;

import org.jsoup.nodes.Document;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildAmount;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.game.DownloadPageTask;
import pl.merskip.ogamemobile.game.GameActivity;

/**
 * Budowanie statków/obrony
 */
public class BuildAmountTask extends DownloadPageTask<Boolean> {

    private BuildItem buildItem;
    private int amount;

    public BuildAmountTask(GameActivity activity, BuildItem buildItem, int amount) {
        super(activity);
        this.buildItem = buildItem;
        this.amount = amount;
    }

    @Override
    protected AbstractPage<Boolean> createDownloadPage() {
        String currentPage = activity.getCurrentPage();
        BuildAmount build = new BuildAmount(auth, currentPage);
        build.setBuildItem(buildItem);
        build.setAmount(amount);
        build.setToken(getTokenForBuild());
        return build;
    }

    private String getTokenForBuild() {
        Document document = activity.getMainDocument();
        return document.select("form[name=form] input[name=token]").attr("value");
    }

    @Override
    protected void afterDownload(Boolean result) {

    }
}
