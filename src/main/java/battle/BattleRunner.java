package battle;

import org.apache.commons.io.FileUtils;
import robocode.control.*;
import robocode.control.events.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
//        copyDirectory("target/classes/" + packageName, robocodeInstallationPath + "/" + compiledBotsDirectory);
    }

    public static void main(String[] args) throws IOException {

        loadConfiguration();
        prepareCompiledBotsForRobocode();

        // Disable log messages from Robocode
        RobocodeEngine.setLogMessagesEnabled(false);

        // Create the RobocodeEngine
        //   RobocodeEngine engine = new RobocodeEngine(); // Run from current working directory
        RobocodeEngine engine = new RobocodeEngine(new java.io.File(robocodeInstallationPath));

        // Add our own battle listener to the RobocodeEngine
        engine.addBattleListener(new BattleObserver());

        // Show the Robocode battle view
        engine.setVisible(true);

        // Setup the battle specification

        int numberOfRounds = 1;
        BattlefieldSpecification battlefield = new BattlefieldSpecification(600, 600); // 800x600

        String customRobot = packageName + '.' + robotName + '*';
        RobotSpecification[] selectedRobots =
                engine.getLocalRepository("sample.Corners,sample.Crazy," + customRobot);

        BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);

        // Run our specified battle and let it run till it is over
        engine.runBattle(battleSpec, true); // waits till the battle finishes

        // Cleanup our RobocodeEngine
        engine.close();

        // Make sure that the Java VM is shut down properly
        System.exit(0);
    }
}

//
// Our private battle listener for handling the battle event we are interested in.
//
class BattleObserver extends BattleAdaptor {

    // Called when the battle is completed successfully with battle results
    public void onBattleCompleted(BattleCompletedEvent e) {
        System.out.println("-- Battle has completed --");

        // Print out the sorted results with the robot names
        System.out.println("Battle results:");
        for (robocode.BattleResults result : e.getSortedResults()) {
            System.out.println("  " + result.getTeamLeaderName() + ": " + result.getScore());
//            System.out.println("  " + result.getTeamLeaderName() + ": " + result.getSurvival());
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