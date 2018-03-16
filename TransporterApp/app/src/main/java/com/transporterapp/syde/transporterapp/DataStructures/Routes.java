package com.transporterapp.syde.transporterapp.DataStructures;

/**
 * Created by danni_000 on 2018-03-14.
 */
public class Routes {

    public final String id  ;
    public final String route;

    public Routes(String id, String route) {
        this.id = id;
        this.route = route;
    }

    public String getRouteId() { return id; }

    public String getRoute() { return route; }

    public static Routes createRoutes(String id, String route) {
        return new Routes(id, route);
    }
}
