package lut;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import robocode.RobocodeFileOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Class representing the knowledge gathered during the learning process.
 */
@Getter
public class Knowledge {

    private String dataFile;

    static double learningRate = 0.5;
    static double discountFactor = 0.8;

    // Bins (discretization of space)
    public static final int XYBins = 5;  // X and Y position
    public static final int EnergyBins = 3;  // Energy
    public static final int DistBins = 4; // Distance to enemy
    public static final int AngleBins = 3; // Angle to enemy

    // Lookup table of Q-learning algorithm
    // state + action -> value
    // state: [xPos][yPos][energy][distanceToEnemy][angleToEnemy]
    // action: GO_AHEAD, GO_BACK, TURN_RIGHT, TURN_LEFT, FIRE_STRONG, FIRE, DO_NOTHING
    private double[][][][][][] lut = new double[XYBins][XYBins][EnergyBins][DistBins][AngleBins][Action.values().length];

    public Knowledge(String dataFile) {
        this.dataFile = dataFile;
        File file = new File(dataFile);
            if (file.exists()) {
                loadKnowledge();
            }
    }

    public void saveKnowledge() {

        try (Writer writer = new FileWriter(dataFile)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(lut, writer);
        } catch (IOException e) {
            System.err.println("Saving knowledge failed");
            e.printStackTrace();
        }
    }

    void loadKnowledge() {

        try {
            File file = new File(dataFile);
            if (file.exists()) {
                String lut = FileUtils.readFileToString(file, "UTF-8");
                Gson gson = new GsonBuilder().create();
                this.lut = gson.fromJson(lut, double[][][][][][].class);
            }
        } catch (IOException e) {
            System.err.println("Loading knowledge failed");
            e.printStackTrace();
        }
    }

    public void updateKnowledge(State state, Action action, double reward) {

        double maxValue = Arrays.stream(
                lut[state.getXPos()][state.getYPos()][state.getEnergy()][state.getDistanceToEnemy()][state.getAngle()])
                .max().getAsDouble();

        lut[state.getXPos()][state.getYPos()][state.getEnergy()][state.getDistanceToEnemy()][state.getAngle()][action.ordinal()] = (1.0 - learningRate)
                * lut[state.getXPos()][state.getYPos()][state.getEnergy()][state.getDistanceToEnemy()][state.getAngle()][action.ordinal()]
                + learningRate * (reward + discountFactor * maxValue);
    }
}
