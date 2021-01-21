package lut;

import lombok.Getter;

@Getter
public class State {

    // Robocode environment constants
    public static final int MAX_POSITION = 600;
    public static final int MAX_ENERGY = 100;
    public static final int MAX_DISTANCE = 850;
    public static final int MAX_ANGLE = 360;

    private int xPos;
    private int yPos;
    private int energy;
    private int distanceToEnemy;
    private int angle;

    public State(double xPos, double yPos, double energy, double distanceToEnemy, double angle) {
        this.xPos = discretize(xPos, Knowledge.XYBins, MAX_POSITION);
        this.yPos = discretize(yPos, Knowledge.XYBins, MAX_POSITION);
        this.energy = discretize(energy, Knowledge.EnergyBins, MAX_ENERGY);
        this.distanceToEnemy = discretize(distanceToEnemy, Knowledge.DistBins, MAX_DISTANCE);
        this.angle = discretize(angle, Knowledge.AngleBins, MAX_ANGLE);
    }

    public void updateState(double xPos, double yPos, double energy, double distanceToEnemy, double angle) {
        this.xPos = discretize(xPos, Knowledge.XYBins, MAX_POSITION);
        this.yPos = discretize(yPos, Knowledge.XYBins, MAX_POSITION);
        this.energy = discretize(energy, Knowledge.EnergyBins, MAX_ENERGY);
        this.distanceToEnemy = discretize(distanceToEnemy, Knowledge.DistBins, MAX_DISTANCE);
        this.angle = discretize(angle, Knowledge.AngleBins, MAX_ANGLE);
    }

    int discretize(double value, int nrOfBins, int maxVal) {
        int bucketSize = maxVal / nrOfBins;

        for(int i=1; i<=nrOfBins;i++) {
            if(value <= i*bucketSize) {
                return i-1;
            }
        }
        return nrOfBins-1;
    }
}
