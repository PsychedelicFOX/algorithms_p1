import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * @author psy
 * @date 5/26/19.
 */
public class PointSET {
    private final TreeSet<Point2D> treeSet;

    // construct an empty set of points
    public PointSET(){
        this.treeSet = new TreeSet<>();
    }
    // is the set empty?
    public boolean isEmpty() {
        return treeSet.isEmpty();
    }
    // number of points in the set
    public int size(){
        return treeSet.size();
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        treeSet.add(p);
    }
    // does the set contain point p?
    public boolean contains(Point2D p){
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return treeSet.contains(p);
    }
    // draw all points to standard draw
    public void draw(){
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        for (Point2D point2D : treeSet) {
            StdDraw.point(point2D.x(), point2D.y());
        }

    }
    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect){
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> points = new ArrayList<>();

        for (Point2D point2D : treeSet) {
            if (rect.contains(point2D)) {
                points.add(point2D);
            }
        }

        return points;

    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p){
        if (p == null) {
            throw new IllegalArgumentException();
        }

        double distance = Double.MAX_VALUE;
        Point2D nearest = null;
        for (Point2D point2D : treeSet) {
            double dx = p.x() - point2D.x();
            double dy = p.y() - point2D.y();
            double calcDst = dx * dx + dy * dy;

            if (calcDst < distance) {
                distance = calcDst;
                nearest = point2D;
            }
        }

        return nearest;
    }

    public static void main(String[] args){

    }
}
