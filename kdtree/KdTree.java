import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * @author psy
 * @date 5/26/19.
 */
public class KdTree {
    private Node tree;
    private int size;
    private static class Node{
        public static final boolean VERTICAL = true;
        public static final boolean HORIZONTAL = false;

        private Point2D p;
        private Node lb;
        private Node rt;
        private RectHV rect;

        private boolean orientation;
    }


    // construct an empty set of points
    public KdTree(){
        this.tree = null;
        size = 0;
    }
    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }
    // number of points in the set
    public int size(){
        return size;
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        this.tree = insert(p, tree, Node.HORIZONTAL, 0,0, 1,1);
    }

    private Node insert(Point2D p, Node subTree, boolean parentOrientation, double xmin, double ymin, double xmax, double ymax){
        if (subTree == null) {
            subTree = new Node();
            subTree.p = p;
            subTree.orientation = !parentOrientation;
            subTree.rect = new RectHV(xmin, ymin, xmax, ymax);
            size ++;
            return subTree;
        }

        if (subTree.p.equals(p)) {
            return subTree;
        }

        if (subTree.orientation == Node.VERTICAL) {
            if (p.x() < subTree.p.x())
                subTree.lb = insert(p, subTree.lb, subTree.orientation,
                        subTree.rect.xmin(), subTree.rect.ymin(), subTree.p.x(), subTree.rect.ymax());
            else
                subTree.rt = insert(p, subTree.rt, subTree.orientation,
                        subTree.p.x(), subTree.rect.ymin(), subTree.rect.xmax(), subTree.rect.ymax());

        } else {
            if (p.y() < subTree.p.y())
                subTree.lb = insert(p, subTree.lb, subTree.orientation,
                        subTree.rect.xmin(), subTree.rect.ymin(), subTree.rect.xmax(), subTree.p.y()
                );
            else
                subTree.rt = insert(p, subTree.rt, subTree.orientation,
                        subTree.rect.xmin(),subTree.p.y(),subTree.rect.xmax(),subTree.rect.ymax()
                );
        }

        return subTree;
    }
    // does the set contain point p?
    public boolean contains(Point2D p){
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(p, tree);
    }

    private boolean contains(Point2D p, Node subTree) {
        if (subTree == null) {
            return false;
        }
        else {
            if (subTree.p.equals(p)) {
                return true;
            }

            if (subTree.orientation == Node.VERTICAL){
                if (p.x() < subTree.p.x() )
                    return contains(p, subTree.lb);
                else
                    return contains(p, subTree.rt);
            }

            else {
                if (p.y() < subTree.p.y() )
                    return contains(p, subTree.lb);
                else
                    return contains(p, subTree.rt);
            }
        }
    }

    // draw all points to standard draw
    public void draw(){
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        draw(tree, null);
    }

    private void draw(Node tree, Point2D prev) {
        if (tree != null) {
            StdDraw.point(tree.p.x(),tree.p.y());
            draw(tree.lb, tree.p);
            draw(tree.rt, tree.p);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect){
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        List<Point2D> list = new ArrayList<>();

        return  range(rect, tree, list);
    }

    private List<Point2D> range(RectHV rect, Node tree, List<Point2D> list) {
        if (tree == null)
            return list;

        if (rect.contains(tree.p))
            list.add(tree.p);

        if (tree.orientation == Node.VERTICAL) {
            if (rect.xmax() < tree.p.x()) {
                return range(rect, tree.lb, list);
            }
            if (rect.xmin() > tree.p.x()) {
                return range(rect, tree.rt, list);
            }
            range(rect, tree.lb, list);
            return range(rect, tree.rt, list);


        } else {
            if (rect.ymax() < tree.p.y()) {
                return range(rect, tree.lb, list);
            }
            if (rect.ymin() > tree.p.y()) {
               return range(rect, tree.rt, list);
            }
            range(rect, tree.lb, list);
            return range(rect, tree.rt, list);
        }
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p){
        if (p == null) {
            throw new IllegalArgumentException();
        }


        return nearest(p, tree, Double.POSITIVE_INFINITY);
    }


    private Point2D nearest(Point2D p, Node subTree, double distance) {
        if (subTree == null) {
            return null;
        }

        if (subTree.rect.distanceTo(p) >= distance) {
            return null;
        }

        Point2D closestPoint = null;
        double bestDistance = distance;
        double calcDistance;

        calcDistance = p.distanceTo(subTree.p);
        if (calcDistance < bestDistance) {
            closestPoint = subTree.p;
            bestDistance = calcDistance;
        }


        Node leftSubtree = subTree.lb;
        Node rightSubtree = subTree.rt;

        if (leftSubtree != null && rightSubtree != null) {
            if (leftSubtree.rect.distanceTo(p) > rightSubtree.rect.distanceTo(p)) {
                leftSubtree = subTree.rt;
                rightSubtree = subTree.lb;
            }
        }

        Point2D leftBestPoint = nearest( p, leftSubtree, bestDistance);
        Point2D rightBestPoint = nearest(p, rightSubtree, bestDistance);


        if (leftBestPoint != null) {
            calcDistance = p.distanceTo(leftBestPoint);
            if (calcDistance < bestDistance) {
                closestPoint = leftBestPoint;
                bestDistance = calcDistance;
            }
        }


        if (rightBestPoint != null) {
            calcDistance = p.distanceTo(rightBestPoint);
            if (calcDistance < bestDistance) {
                closestPoint = rightBestPoint;
                bestDistance = calcDistance;
            }
        }

        return closestPoint;
    }



    public static void main(String[] args){
        test1();
        test2();
        test3();
        test4();
        test5();
    }

    private static void test4() {
        KdTree kdTree = new KdTree();
        Point2D p1 = new Point2D(0.372, 0.497);
        kdTree.insert(p1);
        Point2D p2 = new Point2D(0.144, 0.179);
        kdTree.insert(p2);
        Point2D p3 = new Point2D(0.417, 0.362);
        kdTree.insert(p3);

        Point2D test = new Point2D(0.314453125, 0.328125);

        assert kdTree.nearest(test).equals(p3);


    }

    private static void test5() {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.372, 0.372));
        kdTree.insert(new Point2D(0.372, 0.372));
        kdTree.insert(new Point2D(0.372, 0.372));

        assert kdTree.size == 1;

    }

    private static void test3() {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.1, 0.1));
        kdTree.insert(new Point2D(0.2, 0.2));

        Point2D nearest = kdTree.nearest(new Point2D(0.3, 0.3));

        assert nearest.x() == 0.2;
    }

    private static void test2() {
        KdTree kdTree = new KdTree();
        List<Point2D> points = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Point2D p = new Point2D(0.1 * i, 0.1*i);
            kdTree.insert(p);
            points.add(p);
        }

        Iterable<Point2D> res = kdTree.range(new RectHV(0.0, 0.0, 1.1, 1.1));

        for (Point2D re : res) {
            assert points.contains(re);
        }
    }

    private static void test1() {
        KdTree kdTree = new KdTree();
        List<Point2D> points = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Point2D p = new Point2D(0.1 * i, 0.1*i);
            kdTree.insert(p);
            points.add(p);
        }

        for (Point2D point : points) {
            assert kdTree.contains(point);
        }

    }
}
