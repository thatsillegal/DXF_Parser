import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.ArrayList;

public class JSONxfom {
    public JSONxfom() {
    }

    public String toJSON(ArrayList<Building> buildings) {
        String json = "";

        for (int i=0;i<buildings.size();i++) {

            //dotsInfo
            WB_Polygon polygon = buildings.get(i).getPolygon();
            int dots_num = polygon.getNumberOfPoints();
            String dotsInfo ="";
            for (int j = 0; j < dots_num; j++) {
                WB_Point pt = polygon.getPoint(j);
                String singleString = "["+pt.xd()+","+pt.yd()+"]";
                dotsInfo += singleString;
                if(j!=dots_num-1){
                    dotsInfo += ",";
                }
            }
            dotsInfo = "\"dots\":["+dotsInfo +"],";


            //floorInfo
            String floorInfo ="";
            floorInfo = "\"floor\":"+ buildings.get(i).getFloor();

            //buildingInfo
            String buildingInfo="";
            buildingInfo  = "{"+dotsInfo+floorInfo+"}";
            if(i!= buildings.size()-1){
                buildingInfo +=",";
            }

            //JSON File
            json += buildingInfo;

        }
        json = "["+json+"]";

        return json;
    }


}
