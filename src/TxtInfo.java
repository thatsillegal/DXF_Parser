import wblut.geom.WB_Point;

public class TxtInfo {
    private double x,y,z;
    private int floor;
    private WB_Point point;
    private String layername;

    public TxtInfo() {
    }

    public TxtInfo(double x, double y, double z, int floor, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.floor = floor;
        this.point = new WB_Point(x,y,z);
        this.layername  = name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getLayername() {
        return layername;
    }

    public WB_Point getPoint() {
        return point;
    }

    public int getFloor() {
        return floor;
    }
}
