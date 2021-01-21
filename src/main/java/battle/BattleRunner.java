package battle;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.GymObservationSpace;
import org.nd4j.linalg.factory.Nd4j;
import robocode.control.*;
import robocode.control.events.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

//
// Application that demonstrates how to run two sample robots in Robocode using the
// RobocodeEngine from the robocode.control package.
//
// @author Flemming N. Larsen
//
public class BattleRunner {

    private static String packageName;
    private static String robotName;
    private static String robocodeInstallationPath;

    public static int NUM_BATTLES = 250;

    private static void loadConfiguration() throws IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "config.properties";
        Properties configProps = new Properties();
        configProps.load(new FileInputStream(appConfigPath));

        packageName = configProps.getProperty("package");
        robotName = configProps.getProperty("robot");
        robocodeInstallationPath = configProps.getProperty("robocodeInstallation");
    }

    public static void copyDirectory(String source, String destination) throws IOException {
        File sourceDirectory = new File(source);
        File destinationDirectory = new File(destination);
        FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
    }

    private static void prepareCompiledBotsForRobocode() throws IOException {

        String compiledBotsDirectory = "robots/" + packageName;
        File file = new File(compiledBotsDirectory);
        if (!file.exists()) {
            Files.createDirectory(Paths.get(compiledBotsDirectory));
        }
        copyDirectory("target/classes/" + packageName, compiledBotsDirectory);
        copyDirectory("target/classes/" + packageName, robocodeInstallationPath + "/" + compiledBotsDirectory);
    }

    public static void main(String[] args) throws IOException {

        loadConfiguration();
        prepareCompiledBotsForRobocode();

        // Disable log messages from Robocode
        RobocodeEngine.setLogMessagesEnabled(false);
        RobocodeEngine engine = new RobocodeEngine(new java.io.File(robocodeInstallationPath));

        ArrayList<Double> scores = new ArrayList<>();
        engine.addBattleListener(new BattleObserver(scores));
        engine.setVisible(true);

        int numberOfRounds = NUM_BATTLES;
        BattlefieldSpecification battlefield = new BattlefieldSpecification(640, 640); // 800x600

        String customRobot = packageName + '.' + robotName + '*';
        RobotSpecification[] selectedRobots =
                engine.getLocalRepository("sample.Crazy,sample.SittingDuck,sample.Crazy,sample.Crazy,sample.Crazy,sample.Crazy," + customRobot);

        Nd4j.getRandom();

        BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);

        engine.runBattle(battleSpec, true); // waits till the battle finishes

        engine.close();
        System.exit(0);
    }
}

//
// Our private battle listener for handling the battle event we are interested in.
//
class BattleObserver extends BattleAdaptor {

    ArrayList<Double> scores;

    public BattleObserver(ArrayList<Double> scores){
        this.scores = scores;
    }

    // Called when the battle is completed successfully with battle results
    public void onBattleCompleted(BattleCompletedEvent e) {
        System.out.println("-- Battle has completed --");

        for (robocode.BattleResults result : e.getSortedResults()) {
            System.out.println("  " + result.getTeamLeaderName() + ": " + result.getScore());
        }
    }

    // Called when the game sends out an information message during the battle
    public void onBattleMessage(BattleMessageEvent e) {
        System.out.println("Msg> " + e.getMessage());
    }

    // Called when the game sends out an error message during the battle
    public void onBattleError(BattleErrorEvent e) {
        System.out.println("Err> " + e.getError());
    }
}