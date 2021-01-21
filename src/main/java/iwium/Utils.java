package iwium;

import static java.awt.geom.Point2D.distance;

public class Utils {
//    private void selectTurnGunTowardsBearing(double absoluteBearing) {
//        double normalisedBearing = normaliseBearing(this.getGunHeading());
//        double headingDelta = normaliseBearing(absoluteBearing - normalisedBearing);
//
//        if (headingDelta > 0) {
//            turnGunRight(headingDelta);
//        } else {
//            turnGunLeft(Math.abs(headingDelta));
//        }
//    }
//
//    static int discretizeAngle(double bearing) {
//        int bearingDisc = 0;
//
//        if ((bearing > 0) && (bearing <= 90)) {
//            bearingDisc = 0;
//        } else if ((bearing > 90) && (bearing <= 180)) {
//            bearingDisc = 1;
//        } else if ((bearing > 180) && (bearing <= 270)) {
//            bearingDisc = 2;
//        } else if ((bearing > 270) && (bearing <= 360)) {
//            bearingDisc = 3;
//        }
//
//        return bearingDisc;
//    }
//
//    static int discretizeDistance(double distance) {
//        int distanceDisc = 0;
//
//        if ((distance > 0) && (distance <= 213)) {
//            distanceDisc = 0;
//        } else if ((distance > 213) && (distance <= 426)) {
//            distanceDisc = 1;
//        } else if ((distance > 426) && (distance <= 639)) {
//            distanceDisc = 2;
//        } else if ((distance > 639) && (distance <= 850)) {
//            distanceDisc = 3;
//        }
//
//        return distanceDisc;
//    }
//
//    static double absoluteBearing(double x1, double y1, double x2, double y2) {
//        double xo = x2 - x1;
//        double yo = y2 - y1;
//        double hyp = distance(x1, y1, x2, y2);
//        double arcSin = Math.toDegrees(Math.asin(xo / hyp));
//        double bearing = 0;
//
//        if (xo > 0 && yo > 0) { // both pos: lower-Left
//            bearing = arcSin;
//        } else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
//            bearing = 360 + arcSin; // arcsin is negative here, actuall 360 - ang
//        } else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
//            bearing = 180 - arcSin;
//        } else if (xo < 0 && yo < 0) { // both neg: upper-right
//            bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
//        }
//
//        return bearing;
//    }
}
