import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFLayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DXFUtil {
    public static List<DXFLayer> getDXFLayers(DXFDocument dxfdoc){
        List<DXFLayer> layers = new ArrayList<DXFLayer>();
        Iterator iter = dxfdoc.getDXFLayerIterator();
        while(iter.hasNext()){
            DXFLayer layer = (DXFLayer) iter.next();
//            System.out.println("LayerName: "+layer.getName());
            layers.add(layer);
        }
        return layers;
    }
}
