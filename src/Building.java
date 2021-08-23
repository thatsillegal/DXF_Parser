import wblut.geom.WB_Polygon;

import java.util.ArrayList;

public class Building {

    private WB_Polygon polygon;
    private int floor;
    private String layername;


    public Building() {
    }

    public Building(WB_Polygon polygon, int floor, String name) {
        this.polygon = polygon;
        this.floor = floor;
        this.layername = name;
    }

    public WB_Polygon getPolygon() {
        return polygon;
    }

    public int getFloor() {
        return floor;
    }

    public String getLayername() {
        return layername;
    }
}
