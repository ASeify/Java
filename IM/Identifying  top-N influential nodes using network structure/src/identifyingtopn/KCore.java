package identifyingtopn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class KCore {

    public List kCoreCalc(Graph g) throws InterruptedException  {
        List<String> paramsList = new ArrayList<String>();
        boolean repeatFlag = true;
        int k = 0;
        String param = "";
        while (!g.nodeMap.isEmpty()) {
            float tempMk = 0;
            int nk = 1;
            while (repeatFlag) {
                repeatFlag = false;
                List<String> keys = new ArrayList<String>();
                for (String key : g.nodeMap.keySet()) {
                    if (k < 1) {
                        g.nodeMap.get(key).setDegree();
                    }
                    if (g.nodeMap.get(key).neighbors.size() <= k) {
                        param = g.nodeMap.get(key).identifier;
                        param += ":";
                        param += (k);//KD
                        param += ":";
                        param += nk;
                        param += ":";
                        param += g.nodeMap.get(key).degree;
                        paramsList.add(param);
                        keys.add(key);
                        repeatFlag = true;
                    }
                }
                if (keys.size() > 0) {
                    tempMk += 1;
                }
                for (String key : keys) {
                    g.deleteNode(key);
                }
                nk++;
            }
//            if (g.nodeMap.size() > 0) {
//                    g.draw();
//                    TimeUnit.SECONDS.sleep(5);
//                }
            float mk = g.getMk();
            if (mk < tempMk) {
                g.setMk(tempMk);
            }
            k++;
            repeatFlag = true;
        }
        return paramsList;
    }

}
