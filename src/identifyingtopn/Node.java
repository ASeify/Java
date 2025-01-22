package identifyingtopn;

import java.util.ArrayList;

class Node {

    //------- main attributes -----------------
    String identifier = null, lable = null;
    float degree = 0;
    ArrayList<String> neighbors = new ArrayList<String>();

    //------- graphical attributes -----------------
    float x, y, z, w, h, d;
    String fill;

    //-------other attributes-----------------
    float delta = 0, NGI = 0, RLGI = 0;
    float Nk = 0, KD = 0, value = 0;
    String[] source;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void updateLable(String lable) {
        this.lable = lable;
    }

    public void addNeighbor(Node neighbor) {
        this.neighbors.add(neighbor.identifier);
        this.degree = this.neighbors.size();
    }

    public void uapdateGraphics(Node graphics) {
        this.x = graphics.x;
        this.y = graphics.y;
        this.z = graphics.z;
        this.w = graphics.w;
        this.h = graphics.h;
        this.d = graphics.d;
        this.fill = graphics.fill;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public float getDelta() {
        return this.delta;
    }

    public void setNGI(float ngi) {
        this.NGI = ngi;
    }

    public float getNGI() {
        return this.NGI;
    }

    public void setNk(int nk) {
        this.Nk = nk;
    }

    public float getNk() {
        return this.Nk;
    }

    public void setKD(int kd) {
        this.KD = kd;
    }

    public float getKD() {
        return this.KD;
    }

    public void updateDegree() {
        this.degree = this.neighbors.size();
    }

    public float getDegree() {
        return this.neighbors.size();
    }

    public void setDegree() {
        this.degree = this.neighbors.size();
    }

    public void setRLGI(float rlgi) {
        this.RLGI = rlgi;
    }

    public float getRLGI() {
        return this.RLGI;
    }

    public void setSource(String[] source) {
        this.source = source;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void printNodeInfo() {
        String source = prinntNodeSource(this.source);
        System.out.println("id:" + this.identifier + " ,Lable:" + this.lable
                + " ,KD: " + this.KD + " ,nk: " + this.Nk
                + " ,Degree: " + this.degree + " ,Value: " + this.value + " ,delta: " + this.delta
                + " ,NGI: " + this.NGI + " ,RLGI: " + this.RLGI + " ,Source: " + source
        /*+" ,Neighbors List:" + this.nodeMap.get(key).neighbors*/);
    }

    public String prinntNodeSource(String[] source) {
        String str = null;
        if (source != null) {
            for (int i = 0; i < source.length; i++) {
                if (i != source.length - 1) {
                    str += source[i] + ",";
                } else {
                    str += source[i];
                }
            }
        }
        return str;
    }

}
