import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author psy
 * @date 5/16/19.
 */
public class FastCollinearPoints {
    private final List<LineSegment> lineSegments = new LinkedList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        Point[] pts = Arrays.copyOf(points, points.length);

        for (int i = 0; i < pts.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        Arrays.sort(pts);

        for (int i = 1; i < pts.length; i++) {
            if (pts[i].compareTo(pts[i-1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < points.length; i++) {
            Point origin = points[i];

            Arrays.sort(pts);
            Arrays.sort(pts, origin.slopeOrder());

            int min = 1;
            int max = 1;


            while (min < pts.length) {
                while (max < pts.length && origin.slopeTo(pts[max]) == origin.slopeTo(pts[min]))
                    max++;
                if (max - min >= 3) {
                    Point p = pts[min];
                    Point s = pts[max-1];
                    if (!(pts[min].compareTo(origin) < 0))
                        p = origin;
                    if (origin == p)
                        lineSegments.add(new LineSegment(p, s));
                }
                min = max;
            }


        }

    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] res = new LineSegment[lineSegments.size()];
        return lineSegments.toArray(res);
    }
}
