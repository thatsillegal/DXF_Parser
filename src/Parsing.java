import org.kabeja.dxf.*;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp2D;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.hemesh.HE_Mesh;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Parsing {

    public Parsing() {
    }

    public static void main(String[] args) throws IOException {
        Parser dxfparser = ParserBuilder.createDefaultParser();
        try {
//            dxfparser.parse("C:\\Users\\zixua\\OneDrive\\Codings\\dxfparser\\Drawing2.dxf", DXFParser.DEFAULT_ENCODING);
            dxfparser.parse("C:\\Users\\zixua\\OneDrive\\Codings\\dxfparser\\Drawing2.dxf", "utf-8");//THats right!
        }catch(Exception e){
            e.printStackTrace();
        }
        DXFDocument doc = dxfparser.getDocument();

        //提取线对象
        DXFLayer layer = doc.getDXFLayer("JMD");
        List<DXFLWPolyline> plines = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LWPOLYLINE); //注意是LWPOLYLINE 在R14后引进的，是为了减少老式的POLYLINE对存储空间的占用
        List<DXFText> texts = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_TEXT);
        ArrayList<TxtInfo> textinfos = new ArrayList<>();

        if(plines ==null){ //nullPointerException 空指针 意思是找不到那个实例对象 所以先判断到底有没有 null
            System.out.println("没有多段线");
        }else {
            System.out.println("删除前的线条总数： "+plines.size());
            //优雅地删除not CLOSED的元素 ,使用倒序或者使用iterator，因为iterator其实本质上就是倒序的
            Iterator it = plines.iterator();
            while(it.hasNext()){
                DXFLWPolyline cur = (DXFLWPolyline)it.next();
                if(!cur.isClosed()){
                    it.remove();
                }
            }
            System.out.println("删除后的线条总数： "+plines.size());
        }

        if(texts ==null){ //nullPointerException 空指针 意思是找不到那个实例对象 所以先判断到底有没有 null
            System.out.println("没有文字");
        }else {
            System.out.println("删除前的文字总数： "+texts.size());
            for(int i = 0;i<texts.size();i++){
                DXFText txt = texts.get(i);
                String words = txt.getText();
                words = words.replaceAll("\\D","");//把非数字的全部去掉
                if(!words.isEmpty()){
                    int floor = Integer.parseInt(words);
                    TxtInfo thisinfo = new TxtInfo(txt.getInsertPoint().getX(),txt.getInsertPoint().getY(),txt.getInsertPoint().getZ(),floor);
                    textinfos.add(thisinfo);
                }
//                System.out.println(words);
//                System.out.println("["+txt.getInsertPoint().getX()+","+txt.getInsertPoint().getY()+","+txt.getInsertPoint().getZ()+"]");
            }
            System.out.println("删除后的文字总数： "+textinfos.size());
        }

        //要从静态函数中调用之外的函数，除非那个函数也是static，否则只能创建一个实例
        ArrayList<Building> buildings = new ArrayList<>();
        Parsing p = new Parsing();
        buildings=p.matchData(textinfos,plines);

//        for(Building building:buildings){
//            System.out.println("楼层有: "+building.getFloor());
//            System.out.println("polygon的点一共有："+building.getPolygon().getNumberOfPoints());
//        }

        //输出JSON 格式
        JSONxfom jgen = new JSONxfom();
        String jsonString = jgen.toJSON(buildings); //This is under debug!!!!
//        System.out.println(jsonString);

        //输出JSON文件
        File file = new File("buildings.json"); //这个当前目录不是指的class所在的src目录，而是项目的根目录
        if(file.createNewFile()){
            System.out.println("Successfully created!");
        }else{
            System.out.println("File has existed.");
        }
        FileOutputStream output = new FileOutputStream(file);
        output.write(jsonString.getBytes(StandardCharsets.UTF_8));
        output.close();
    }

    private ArrayList<Building> matchData(ArrayList<TxtInfo> txtInfos, List<DXFLWPolyline> plines){

        //获得所有的polygon
        ArrayList<WB_Polygon> polygons = new ArrayList<>();
        for(DXFLWPolyline pline: plines){
            ArrayList<WB_Point> points2 = new ArrayList<>();
            for(int i=0;i<pline.getVertexCount();i++){
                points2.add(new WB_Point(pline.getVertex(i).getX(),pline.getVertex(i).getY(),pline.getVertex(i).getZ()));
            }
            WB_Polygon poly = new WB_Polygon(points2);
            polygons.add(poly);
        }


//        for(int i=0;i< txtInfos.size();i++){
//            for(int j=0;j<polygons.size();j++){
//                if(WB_GeometryOp2D.contains2D(txtInfos.get(i).getPoint(),polygons.get(j))){
//                    buildings.add(new Building(polygons.get(j),txtInfos.get(i).getFloor()));
//                }
//            }
//        }

        //判断并且产生Building ，使用倒序，速度和准确度更高
        ArrayList<Building> buildings = new ArrayList<>();
        for(int i= polygons.size()-1;i>=0;i--){
            for(int j =txtInfos.size()-1;j>=0;j--){
                if(WB_GeometryOp2D.contains2D(txtInfos.get(j).getPoint(),polygons.get(i))){
                    buildings.add(new Building(polygons.get(i),txtInfos.get(j).getFloor()));
                    polygons.remove(i);
                    txtInfos.remove(j);
                    break;
                }
                if(j == 0){
                    buildings.add(new Building(polygons.get(i),0)); //没有信息的 给0层
                }
            }
        }

        System.out.println("Buildings List num: "+ buildings.size());

        //返回
        return buildings;
    }

}
