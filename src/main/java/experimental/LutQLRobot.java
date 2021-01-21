package experimental;

import lut.Action;
import lut.Knowledge;
import lut.State;
import robocode.*;

import java.awt.geom.Point2D;
import java.util.Random;

public class LutQLRobot extends AdvancedRobot {

    static int count = 0;
    static int round = 0;
    static double wins = 0;

    State state;

    public static boolean init = true;
    public static Knowledge knowledge;

    String dataFile = "lut.json";

    static double reward = 0;
    static double rewardSum = 0;

    double distanceToEnemy;
    double angleToEnemy;

    static double experimentRate = 0.2;

    public void run() {
        reward = 0;
        this.state = new State(0,0,0,0,0);
        if (init) {
            knowledge = new Knowledge(dataFile);
            init = false;
        }
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);
        while (true) {
            setTurnRadarLeft(360);
            Action action = pickAction();
            runAction(action);
            this.state.updateState(getX(), getY(), getEnergy(), distanceToEnemy, angleToEnemy);
            knowledge.updateKnowledge(state, action, reward);
            count += 1;
            rewardSum += reward;
        }
    }

    public static int getRandomInteger(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    Action pickAction() {
        if (new Random().nextFloat() < experimentRate) {
            return Action.values()[getRandomInteger(0, Action.values().length - 1)];
        } else {
            // Get the action with the highest QValue for this state.
            return Action.values()[getMaxIndex(knowledge.getLut()[state.getXPos()][state.getYPos()][state.getEnergy()][state.getDistanceToEnemy()][state.getAngle()])];
        }
    }

    public static int getMaxIndex(double[] array) {
        double max = array[0];
        int index = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] >= max) {
                max = array[i];
                index = i;
            }
        }
        return index;
    }

    public void runAction(Action action) {
        switch (action) {
            case GO_AHEAD:
                ahead(100);
                break;
            case TURN_RIGHT:
                turnRight(30);
                break;
            case GO_AHEAD_WITH_TURN_LEFT:
                turnLeft(30);
                ahead(100);
                break;
            case GO_AHEAD_WITH_TURN_RIGHT:
                turnRight(30);
                ahead(100);
                break;
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        double enemyBearing = getAbsoluteBearing(e.getBearing());

        // The velocity of the enemy robot's movement.
        double enemyXVelocity = Math.sin(Math.toRadians(e.getHeading())) * e.getVelocity();
        double enemyYVelocity = Math.cos(Math.toRadians(e.getHeading())) * e.getVelocity();

        // Relative velocity of both our robots movement.
        double relativeXVelocity = enemyXVelocity;
        double relativeYVelocity = enemyYVelocity;

        // The enemies position coordinates, relative to us.
        double currentRelativeEnemyX = Math.sin(Math.toRadians(enemyBearing)) * e.getDistance();
        double currentRelativeEnemyY = Math.cos(Math.toRadians(enemyBearing)) * e.getDistance();

        // Select appropriate firepower for distance.
        double firePower = 10;

        // Calculate bullet speed.
        double bulletSpeed = 20 - firePower * 3;

        // Calculate bullet travel time.
        double timeForBulletToHit = e.getDistance()
                / (bulletSpeed + Point2D.distance(0, 0, relativeXVelocity, relativeYVelocity));

        // Predict enemy location at this time.
        double predicatedRelativeEnemyX = currentRelativeEnemyX + (relativeXVelocity * timeForBulletToHit);
        double predicatedRelativeEnemyY = currentRelativeEnemyY + (relativeYVelocity * timeForBulletToHit);

        // Convert the relative coordinates of the enemy to a bearing to aim at.
        double predictedBearing = Math.atan2(predicatedRelativeEnemyX, predicatedRelativeEnemyY) * 180 / 3.141592653589;

        selectTurnGunTowardsBearing(predictedBearing);
        fire(firePower);

        distanceToEnemy = e.getDistance();
        angleToEnemy = e.getBearing();
    }

    private double getAbsoluteBearing(double degrees) {
        double absoluteBearing = this.getHeading() + degrees;

        if (absoluteBearing > 360)
            absoluteBearing -= 360;
        if (absoluteBearing < 0)
            absoluteBearing += 360;

        return absoluteBearing;
    }

    private static double normaliseBearing(double degrees) {
        while (degrees >= 180)
            degrees -= 360;
        while (degrees < -180)
            degrees += 360;
        return degrees;
    }

    private void selectTurnGunTowardsBearing(double absoluteBearing) {
        double normalisedBearing = normaliseBearing(this.getGunHeading());
        double headingDelta = normaliseBearing(absoluteBearing - normalisedBearing);

        if (headingDelta > 0) {
            turnGunRight(headingDelta);
        } else {
            turnGunLeft(Math.abs(headingDelta));
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        reward -= 5;
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {
        reward += 10;
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        reward -= 5;
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        reward -= 5;
    }

    @Override
    public void onWin(WinEvent winEvent) {
        reward += 100;
        wins += 1;
    }

    @Override
    public void onDeath(DeathEvent deathEvent) {
        reward -= 50;
    }

    @Override
    public void onRoundEnded(RoundEndedEvent e) {
        round += 1;
        if (round % 10 == 0) {
            knowledge.saveKnowledge();
        }
    }
}
