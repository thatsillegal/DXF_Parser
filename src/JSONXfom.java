import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;

import java.util.ArrayList;

public class JSONXfom {
    public JSONXfom() {
    }

    public String toJSON(ArrayList<Building> buildings, ArrayList<String> layernames) {
        String json ="";

        String layerinfos="";
        for(int i=0;i<layernames.size();i++){
            layerinfos =layerinfos+"\""+layernames.get(i)+"\"";
            if(i != layernames.size()-1){
                layerinfos +=",";
            }
        }

        String buildinginfos = "";

        for (int i=0;i<buildings.size();i++) {
            //dotsInfo
            WB_Polygon polygon = buildings.get(i).getPolygon();
            int dots_num = polygon.getNumberOfPoints();
            String dotsInfo ="";
            for (int j = 0; j < dots_num; j++) {
                WB_Point pt = polygon.getPoint(j);
                String singleString = "["+String.format("%.2f",pt.xd())+","+String.format("%.2f",pt.yd())+"]"; //在这里操作，设置小数点位数
                dotsInfo += singleString;
                if(j!=dots_num-1){
                    dotsInfo += ",";
                }
            }
            dotsInfo = "\"dots\":["+dotsInfo +"],";

            //floorInfo
            String floorInfo ="";
            floorInfo = "\"floor\":"+ buildings.get(i).getFloor()+",";

            //Layerinfo
            String layerInfo="";
            layerInfo = "\"layer\":"+"\""+buildings.get(i).getLayername()+"\"";

            //buildingInfo
            String buildingInfo="";
            buildingInfo  = "{"+dotsInfo+floorInfo+layerInfo+"}";
            if(i!= buildings.size()-1){
                buildingInfo +=",";
            }

            //JSON File
            buildinginfos += buildingInfo;
        }


        json = "[["+layerinfos+"],["+buildinginfos+"]]";
        return json;
    }
}
