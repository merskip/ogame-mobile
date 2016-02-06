package pl.merskip.ogamemobile.adapter.game.pages.fleet;


import org.jsoup.Connection;

import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.login.AuthorizationData;

public class FleetMovementRequest extends RequestPage {

    private FleetSendResult.Set sendResult;

    public FleetMovementRequest(AuthorizationData auth, FleetSendResult.Set sendResult) {
        this(auth, "", sendResult);
    }

    public FleetMovementRequest(AuthorizationData auth, String planetId,
                                FleetSendResult.Set sendResult) {
        super(auth, "movement", planetId);
        this.sendResult = sendResult;
    }


    @Override
    @SuppressWarnings("SpellCheckingInspection")
    protected Connection createConnection() {
        Connection connection = super.createConnection()
                .method(Connection.Method.POST)

                .data("holdingtime", sendResult.holdingTime)
                .data("expeditiontime", sendResult.expeditionTime)

                .data("token", sendResult.token)

                .data("galaxy", sendResult.galaxy)
                .data("system", sendResult.system)
                .data("position", sendResult.position)

                .data("type", sendResult.type)
                .data("mission", sendResult.mission)
                .data("union2", sendResult.union2)
                .data("holdingOrExpTime", "0")

                .data("speed", sendResult.speed)
                .data("acsValues", sendResult.acsValues)

                .data("prioMetal", "1")
                .data("prioCrystal", "1")
                .data("prioDeuterium", "1")

                .data("metal", sendResult.metal)
                .data("crystal", sendResult.crystal)
                .data("deuterium", sendResult.deuterium);


        for (FleetShipsResult.Ship ship : sendResult.ships) {
            String paramName = "am" + ship.id;
            connection.data(paramName, String.valueOf(ship.amount));
        }

        return connection;
    }


}
