import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * @author psy
 * @date 5/16/19.
 */
public class BruteCollinearPoints {
    private final List<LineSegment> lineSegments = new LinkedList<>();


    public BruteCollinearPoints(Point[] points) {
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

        for (int i = 0; i < pts.length; i++) {
            for (int j = i+1; j < pts.length; j++) {
                double pqSlope = pts[i].slopeTo(pts[j]);
                for (int k = j+1; k < pts.length; k++) {
                    double prSlope = pts[i].slopeTo(pts[k]);
                    if (prSlope != pqSlope) {
                        continue;
                    }
                    for (int n = k+1; n < pts.length; n++) {
                        double psSlope = pts[i].slopeTo(pts[n]);

                        if (pqSlope == psSlope) {
                            lineSegments.add(new LineSegment(pts[i], pts[n]));
                        }
                    }
                }
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
