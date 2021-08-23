import org.kabeja.dxf.DXFLWPolyline;
import org.kabeja.dxf.DXFVertex;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.ArrayList;

public class PolyInfo{
    private String layername;
    private DXFLWPolyline dxflwPolyline;
    private WB_Polygon polygon;

    public PolyInfo(DXFLWPolyline _dxflwPolyline, String _layername){
        this.layername = _layername;
        this.dxflwPolyline =_dxflwPolyline;
        initPolygon();
    }

    private void initPolygon(){
        ArrayList<WB_Point> points = new ArrayList<>();
        for(int i=0;i<dxflwPolyline.getVertexCount();i++){
            DXFVertex v = dxflwPolyline.getVertex(i);
            points.add(new WB_Point(v.getX(),v.getY(),v.getZ()));
        }
        WB_Polygon poly = new WB_Polygon(points);
        this.polygon = poly;
    }

    public WB_Polygon getPolygon() {
        return polygon;
    }

    public String getLayername() {
        return layername;
    }

    public void setLayername(String layername) {
        this.layername = layername;
    }

    public DXFLWPolyline getDxflwPolyline() {
        return dxflwPolyline;
    }

    public void setDxflwPolyline(DXFLWPolyline dxflwPolyline) {
        this.dxflwPolyline = dxflwPolyline;
    }
}
