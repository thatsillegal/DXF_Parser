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
            dxfparser.parse("C:\\Users\\zixua\\OneDrive\\Codings\\dxfparser\\Drawing3layers.dxf", "utf-8");//THats right!
        }catch(Exception e){
            e.printStackTrace();
        }
        DXFDocument doc = dxfparser.getDocument();

        //循环提取所有图层的物件
        DXFUtil dxfUtil = new DXFUtil();//这是一个静态对象，所以方法啥的都可以不用
        List<DXFLayer> all_layers = dxfUtil.getDXFLayers(doc);

        ArrayList<TxtInfo> textinfos = new ArrayList<>();
        ArrayList<PolyInfo> polyInfos = new ArrayList<>();



        for(DXFLayer a:all_layers){
            System.out.println("图层名："+ a.getName());

            if(a.getName().equals("Text")){
                List<DXFText> texts = a.getDXFEntities(DXFConstants.ENTITY_TYPE_TEXT);
                if(texts ==null){
                    System.out.println("没有文字");
                }else {
                    System.out.println("RAW文字总数："+texts.size());
                    for(int i = 0;i<texts.size();i++){
                        DXFText txt = texts.get(i);
                        String words = txt.getText();
                        words = words.replaceAll("\\D","");//把非数字的全部去掉
                        if(!words.isEmpty()){
                            int floor = Integer.parseInt(words);
                            TxtInfo thisinfo = new TxtInfo(txt.getInsertPoint().getX(),txt.getInsertPoint().getY(),txt.getInsertPoint().getZ(),floor,a.getName());
                            textinfos.add(thisinfo);
                        }
                    }
                    System.out.println("筛选后文字总数： "+textinfos.size());
                }

            }else {
                List<DXFLWPolyline> plines = a.getDXFEntities(DXFConstants.ENTITY_TYPE_LWPOLYLINE);
                if(plines ==null){  //nullPointerException 空指针 意思是找不到那个实例对象 所以先判断到底有没有 null
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
                    for(DXFLWPolyline l:plines){
                        polyInfos.add(new PolyInfo(l,a.getName()));
                    }
                }

            }
        }

        //要从静态函数中调用之外的函数，除非那个函数也是static，否则只能创建一个实例
        ArrayList<Building> buildings = new ArrayList<>();
        Parsing p = new Parsing();
        buildings=p.matchData(textinfos,polyInfos);



        //输出JSON 格式
        ArrayList<String> layernames = new ArrayList<>();
        for(DXFLayer a:all_layers) {
            layernames.add(a.getName());
        }
        JSONXfom jgen = new JSONXfom();
        String jsonString = jgen.toJSON(buildings,layernames); //This is under debug!!!!

        //输出JSON文件
        File file = new File("buildings2.json"); //这个当前目录不是指的class所在的src目录，而是项目的根目录
        if(file.createNewFile()){
            System.out.println("Successfully created!");
        }else{
            System.out.println("File has existed.");
        }
        FileOutputStream output = new FileOutputStream(file);
        output.write(jsonString.getBytes(StandardCharsets.UTF_8));
        output.close();
    }

    private ArrayList<Building> matchData(ArrayList<TxtInfo> txtInfos, ArrayList<PolyInfo> polyInfos){

        //判断并且产生Building ，使用倒序，速度和准确度更高
        ArrayList<Building> buildings = new ArrayList<>();
        for(int i= polyInfos.size()-1;i>=0;i--){
            for(int j =txtInfos.size()-1;j>=0;j--){
                if(WB_GeometryOp2D.contains2D(txtInfos.get(j).getPoint(),polyInfos.get(i).getPolygon())){
                    buildings.add(new Building(polyInfos.get(i).getPolygon(),txtInfos.get(j).getFloor(),polyInfos.get(i).getLayername()));
                    polyInfos.remove(i);
                    txtInfos.remove(j);
                    break;
                }
                if(j == 0){
                    buildings.add(new Building(polyInfos.get(i).getPolygon(),0,polyInfos.get(i).getLayername())); //没有信息的 给0层
                }
            }
        }
        System.out.println("Buildings List num: "+ buildings.size());
        //返回
        return buildings;
    }
}
