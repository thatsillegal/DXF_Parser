import wblut.geom.WB_Point;

public class TxtInfo {
    private double x,y,z;
    private int floor;
    private WB_Point point;

    public TxtInfo() {
    }

    public TxtInfo(double x, double y, double z, int floor) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.floor = floor;
        this.point = new WB_Point(x,y,z);
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

    public WB_Point getPoint() {
        return point;
    }

    public int getFloor() {
        return floor;
    }
}
