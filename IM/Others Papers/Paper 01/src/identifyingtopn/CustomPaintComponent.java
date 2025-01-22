package identifyingtopn;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

class CustomPaintComponent extends Component implements MouseWheelListener, MouseListener, MouseMotionListener {

    private final int ARR_SIZE = 4;
    private double zoomFactor = 1;
    private double prevZoomFactor = 1;
    private boolean zoomer;
    private boolean dragger;
    private boolean released;
    private double xOffset = 0;
    private double yOffset = 0;
    private int xDiff;
    private int yDiff;
    private Point startPoint;
    private boolean zoomReset = false;
    private int n = 1;
    private boolean first = true;

    Map<String, Node> nodeMap = new HashMap<String, Node>();
    List<Edge> edgeList = new ArrayList<Edge>();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    boolean isDirected = false, isWeighted = false;
    float maxRLGI = 1;
    int width = (int) screenSize.getWidth();
    int height = (int) screenSize.getHeight();

    CustomPaintComponent(Graph graph) {
        this.nodeMap = graph.nodeMap;
        this.edgeList = graph.edgeList;
        this.isDirected = graph.getIsDirected();
        this.isWeighted = graph.getIsWeighted();
        if (graph.getMaxRLGI() != -1) {
            this.maxRLGI = graph.getMaxRLGI();
        }
        initComponent();
    }

    private void initComponent() {
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void paint(Graphics g) {
        super.paint(g);

        // Retrieve the graphics context; this object is used to paint shapes
        Graphics2D g2d = (Graphics2D) g;
        n = nodeMap.size();
        n = (n / 250);
        if (n == 0) {
            n = 1;
        }
        if (zoomer) {
            AffineTransform at = new AffineTransform();

            double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
            double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

            double zoomDiv = zoomFactor / prevZoomFactor;

            xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
            yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

            at.translate(xOffset, yOffset);
            at.scale(zoomFactor, zoomFactor);
            prevZoomFactor = zoomFactor;
            g2d.transform(at);
            zoomer = false;
        }

        if (dragger) {
            AffineTransform at = new AffineTransform();
            at.translate(xOffset + xDiff, yOffset + yDiff);
            at.scale(zoomFactor, zoomFactor);
            g2d.transform(at);

            if (released) {
                xOffset += xDiff;
                yOffset += yDiff;
                dragger = false;
            }

        }
        if (zoomReset || first) {
            for (int k = 1; k < 10; k += 5) {
                if (n == k || (n < k && n > (k - 5))) {
                    zoomFactor = 1;
                    prevZoomFactor = 1;
                    xOffset = (width * (n / (10 * k)));
                    yOffset = (height * (n / (10 * k)));
                    AffineTransform at = new AffineTransform();
                    at.translate(xOffset, yOffset);
                    if (n == 1) {
                        zoomFactor /= 1.1;
                        prevZoomFactor = zoomFactor;
                    } else {
                        for (int i = 0; i < ((k / 5) * n); i++) {
                            zoomFactor /= (1.1);
                        }
                        prevZoomFactor = zoomFactor;
                    }
                    at.scale(zoomFactor, zoomFactor);
                    g2d.transform(at);
                }
            }
            zoomReset = false;
            first = false;
        }
        int edgesScale = 1;
        if (nodeMap.size() > 0) {
            edgesScale = (edgeList.size() / nodeMap.size());
        }
        if (edgesScale
                < 10 && edgeList.size()
                < 500) {
            float sourceX, sourceY, destinationX, destinationY;
            g2d.setColor(Color.GRAY);
            if (isDirected) {
                for (Edge edge : edgeList) {
                    sourceX = (edge.source.x);
                    sourceY = (edge.source.y);
                    destinationX = edge.destination.x;
                    destinationY = edge.destination.y;
                    g2d.drawLine(castDoubleToNearestInt(sourceX), castDoubleToNearestInt(sourceY), castDoubleToNearestInt(destinationX), castDoubleToNearestInt(destinationY));

                    double l = Math.sqrt(Math.pow((destinationX - sourceX), 2) + Math.pow((destinationY - sourceY), 2));
                    double dWeight = l / 5;
                    double newWeightX = ((destinationX + (((sourceX - destinationX) / (l) * dWeight))));
                    double newWeighty = ((destinationY + (((sourceY - destinationY) / (l) * dWeight))));

                    drawArrow(g2d, edge);

                    if (isWeighted) {
                        g2d.setColor(Color.MAGENTA);
                        g2d.setFont(new Font("Times New Roman (Headings CS)", Font.BOLD, 12));
                        g2d.drawString(Float.toString(edge.weight), castDoubleToNearestInt(newWeightX), castDoubleToNearestInt(newWeighty));
                        g2d.setColor(Color.GRAY);
                    }
                }
            } else {
                if (!isWeighted) {
                    for (Edge edge : edgeList) {
                        sourceX = (edge.source.x);
                        sourceY = (edge.source.y);
                        destinationX = edge.destination.x;
                        destinationY = edge.destination.y;
                        g2d.drawLine(castDoubleToNearestInt(sourceX), castDoubleToNearestInt(sourceY), castDoubleToNearestInt(destinationX), castDoubleToNearestInt(destinationY));
                    }

                } else {
                    for (Edge edge : edgeList) {
                        sourceX = (edge.source.x);
                        sourceY = (edge.source.y);
                        destinationX = edge.destination.x;
                        destinationY = edge.destination.y;
                        g2d.drawLine(((int) sourceX), ((int) sourceY), ((int) destinationX), ((int) destinationY));
                        double l = Math.sqrt(Math.pow((destinationX - sourceX), 2) + Math.pow((destinationY - sourceY), 2));
                        double dWeight = l / 5;
                        int newWeightX = (int) (destinationX + (((sourceX - destinationX) / (l) * dWeight)));
                        int newWeighty = (int) (destinationY + (((sourceY - destinationY) / (l) * dWeight)));
                        g2d.setColor(Color.MAGENTA);
                        g2d.setFont(new Font("Times New Roman (Headings CS)", Font.BOLD, 12));
                        g2d.drawString(Float.toString(edge.weight), newWeightX, newWeighty);
                        g2d.setColor(Color.GRAY);
                    }
                }
            }
        }

        Random rand = new Random();
        int x = 0, y = 0, w = 0, h = 0;
        if (maxRLGI == 0 || maxRLGI < 0) {
            maxRLGI = 1;
        }
        for (String key : nodeMap.keySet()) {
            // to draw a filled oval use : g2d.fillOval(x, y, w, h)
            if ((nodeMap.get(key).x != 0 && nodeMap.get(key).y != 0)) {
                if (nodeMap.get(key).x < 100 && nodeMap.get(key).y < 100) {
                    x = (int) nodeMap.get(key).x * (10 * n);
                    y = (int) nodeMap.get(key).y * (10 * n);
                } else {
                    x = (int) nodeMap.get(key).x;
                    y = (int) nodeMap.get(key).y;
                }
            } else {
                nodeMap.get(key).setX(rand.nextInt((width * n) - 100));
                while (nodeMap.get(key).getX() < 100) {
                    nodeMap.get(key).setX(rand.nextInt((width * n) - 100));
                }
                nodeMap.get(key).setY(rand.nextInt((height * n) - 100));
                while (nodeMap.get(key).getY() < 100) {
                    nodeMap.get(key).setY(rand.nextInt((height * n) - 100));
                }
                g2d.setColor(Color.BLACK);
                x = ((int) nodeMap.get(key).x);
                y = ((int) nodeMap.get(key).y);
            }
            if (nodeMap.get(key).RLGI > 0) {
                if ((nodeMap.get(key).RLGI / maxRLGI) < 0.25) {
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Times New Roman (Headings CS)", Font.PLAIN, (12 + (int) (12 * (nodeMap.get(key).RLGI)))));
                } else if ((nodeMap.get(key).RLGI / maxRLGI) >= 0.25 && (nodeMap.get(key).RLGI / maxRLGI) < 0.5) {
                    g2d.setColor(Color.GREEN);
                    g2d.setFont(new Font("Times New Roman (Headings CS)", Font.PLAIN, (12 + (int) (12 * (nodeMap.get(key).RLGI)))));
                } else if ((nodeMap.get(key).RLGI / maxRLGI) >= 0.5 && (nodeMap.get(key).RLGI / maxRLGI) < 0.75) {
                    g2d.setColor(Color.BLUE);
                    g2d.setFont(new Font("Times New Roman (Headings CS)", Font.PLAIN, (12 + (int) (12 * (nodeMap.get(key).RLGI)))));
                } else {
                    g2d.setColor(Color.RED);
                    g2d.setFont(new Font("Times New Roman (Headings CS)", Font.PLAIN, (12 + (int) (12 * (nodeMap.get(key).RLGI)))));
                }
                w = 5 + (int) (nodeMap.get(key).RLGI * 20);
                h = w;
            } else {
                g2d.setColor(Color.DARK_GRAY);
                w = 5;
                h = w;
                g2d.setFont(new Font("Times New Roman (Headings CS)", Font.PLAIN, 12));
            }
            x = x - (w / 2);
            y = y - (h / 2);
            g2d.fillOval(x, y, w, h);
            if (nodeMap.get(key).lable != null) {
                g2d.drawString(nodeMap.get(key).lable, x, y);
            } else {
                g2d.drawString(nodeMap.get(key).identifier, x, y);
            }

        }

    }

    void drawArrow(Graphics g1, Edge line) {
        Graphics2D ga = (Graphics2D) g1.create();

        double x1 = line.source.x;
        double y1 = line.source.y;
        double x2 = line.destination.x;
        double y2 = line.destination.y;

        double l = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
        double d = l / 10;
        double newX = ((x2 + (((x1 - x2) / (l) * d))));
        double newY = ((y2 + (((y1 - y2) / (l) * d))));

        double dx = x2 - x1, dy = y2 - y1;
        double angle = (Math.atan2(dy, dx));
        angle = (-1) * Math.toDegrees(angle);
        if (angle < 0) {
            angle = 360 + angle;
        }
        angle = (-1) * angle;
        angle = Math.toRadians(angle);

        AffineTransform at = new AffineTransform();
        at.translate(newX, newY);
        at.rotate(angle);
        ga.transform(at);

        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(5, 0);
        arrowHead.addPoint(-5, 5);
        arrowHead.addPoint(-2, -0);
        arrowHead.addPoint(-5, -5);
        ga.fill(arrowHead);
        ga.drawPolygon(arrowHead);
        //ga.dispose();
    }

    public int castDoubleToNearestInt(double input) {
        int y;
        if (input >= 0.0d) {    // `x` is positive
            
            y = (int) (input + 0.5);
        } else {            // `x` is negative
            y = (int) (input - 0.5);
        }

        return y;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        zoomer = true;

        //Zoom in
        if (e.getWheelRotation() < 0) {
            zoomFactor *= 1.1;
            repaint();
        }
        //Zoom out
        if (e.getWheelRotation() > 0) {
            zoomFactor /= 1.1;
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point curPoint = e.getLocationOnScreen();
        xDiff = curPoint.x - startPoint.x;
        yDiff = curPoint.y - startPoint.y;

        dragger = true;
        repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        zoomReset = true;
        repaint();

    }

    @Override
    public void mousePressed(MouseEvent e) {
        released = false;
        startPoint = MouseInfo.getPointerInfo().getLocation();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        released = true;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
