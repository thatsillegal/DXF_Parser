import wblut.geom.WB_Polygon;

import java.util.ArrayList;

public class Building {

    private WB_Polygon polygon;
    private int floor;

    public Building() {
    }

    public Building(WB_Polygon polygon, int floor) {
        this.polygon = polygon;
        this.floor = floor;
    }

    public WB_Polygon getPolygon() {
        return polygon;
    }

    public int getFloor() {
        return floor;
    }
}
