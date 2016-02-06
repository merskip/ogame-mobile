package pl.merskip.ogamemobile.adapter.game.pages.fleet;


import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.login.AuthorizationData;

public class FleetShipsRequest extends RequestPage {
    public FleetShipsRequest(AuthorizationData auth, String planetId) {
        super(auth, "fleet1", planetId);
    }
}
