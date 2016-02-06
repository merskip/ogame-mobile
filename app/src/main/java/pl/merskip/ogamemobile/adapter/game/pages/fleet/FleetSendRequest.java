package pl.merskip.ogamemobile.adapter.game.pages.fleet;


import org.jsoup.Connection;

import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.login.AuthorizationData;

public class FleetSendRequest extends RequestPage {

    private FleetDetailsResult.Set detailsResult;

    public FleetSendRequest(AuthorizationData auth, FleetDetailsResult.Set detailsResult) {
        this(auth, "", detailsResult);
    }

    public FleetSendRequest(AuthorizationData auth, String planetId,
                            FleetDetailsResult.Set detailsResult) {
        super(auth, "fleet3", planetId);
        this.detailsResult = detailsResult;
    }

    @Override
    protected Connection createConnection() {
        Connection connection = super.createConnection()
                .data("galaxy", detailsResult.galaxy)
                .data("system", detailsResult.system)
                .data("position", detailsResult.position)

                .data("speed", detailsResult.speed)
                .data("union", detailsResult.union)
                .data("acsValues", detailsResult.acsValues)

                .data("type", detailsResult.type)
                .data("mission", detailsResult.mission);

        for (FleetShipsResult.Ship ship : detailsResult.ships) {
            String paramName = "am" + ship.id;
            connection.data(paramName, String.valueOf(ship.amount));
        }

        return connection;
    }
}
