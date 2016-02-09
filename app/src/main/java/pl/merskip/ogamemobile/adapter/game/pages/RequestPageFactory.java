package pl.merskip.ogamemobile.adapter.game.pages;

import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetShipsRequest;
import pl.merskip.ogamemobile.adapter.login.AuthorizationData;

public class RequestPageFactory {

    private RequestPageFactory() { }

    public static RequestPage getRequestPage
            (AuthorizationData auth, String pageName, String planetId) {
        switch (pageName) {
            case "fleet":
            case "fleet1":
                return new FleetShipsRequest(auth, planetId);
            default:
                return new RequestPage(auth, pageName, planetId);
        }
    }

}
