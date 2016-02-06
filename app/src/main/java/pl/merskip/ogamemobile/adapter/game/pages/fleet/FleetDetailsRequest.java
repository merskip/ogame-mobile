package pl.merskip.ogamemobile.adapter.game.pages.fleet;


import org.jsoup.Connection;

import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.login.AuthorizationData;

public class FleetDetailsRequest extends RequestPage {

    private FleetShipsResult.Set shipsResult;

    public FleetDetailsRequest(AuthorizationData auth, FleetShipsResult.Set shipsResult) {
        this(auth, "", shipsResult);
    }

    public FleetDetailsRequest(AuthorizationData auth, String planetId,
                               FleetShipsResult.Set shipsResult) {
        super(auth, "fleet2", planetId);
        this.shipsResult = shipsResult;
    }

    @Override
    protected Connection createConnection() {
        Connection connection = super.createConnection()
                .method(Connection.Method.POST)

                .data("galaxy", shipsResult.galaxy)
                .data("system", shipsResult.system)
                .data("position", shipsResult.position)

                .data("type", shipsResult.type)
                .data("mission", shipsResult.mission)
                .data("speed", shipsResult.speed);

        for (FleetShipsResult.Ship ship : shipsResult.ships) {
            String paramName = "am" + ship.id;
            connection.data(paramName, String.valueOf(ship.amount));
        }

        return connection;
    }
}
