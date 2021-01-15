package battle;

import org.apache.commons.io.FileUtils;
import robocode.control.*;
import robocode.control.events.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//
// Application that demonstrates how to run two sample robots in Robocode using the
// RobocodeEngine from the robocode.control package.
//
// @author Flemming N. Larsen
//
public class BattleRunner {

    private static final String PACKAGE_WITH_ROBOTS_NAME = "experimental";
    private static final String ROBOT_NAME = "GoAheadRobot";

    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) throws IOException {
        File sourceDirectory = new File(sourceDirectoryLocation);
        File destinationDirectory = new File(destinationDirectoryLocation);
        FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
    }

    private static void prepareCompiledBotsCointainerForRobocode() throws IOException {

        File file = new File("robots/" + PACKAGE_WITH_ROBOTS_NAME);
        if (!file.exists()) {
            Files.createDirectory(Paths.get("robots/" + PACKAGE_WITH_ROBOTS_NAME));
        }
        copyDirectory("target/classes/" + PACKAGE_WITH_ROBOTS_NAME,
                    "robots/" + PACKAGE_WITH_ROBOTS_NAME);
    }

    public static void main(String[] args) throws IOException {

        prepareCompiledBotsCointainerForRobocode();

        // Disable log messages from Robocode
        RobocodeEngine.setLogMessagesEnabled(false);

        // Create the RobocodeEngine
        //   RobocodeEngine engine = new RobocodeEngine(); // Run from current working directory
        RobocodeEngine engine = new RobocodeEngine(new java.io.File("C:/robocode")); // Run from C:/Robocode

        // Add our own battle listener to the RobocodeEngine
        engine.addBattleListener(new BattleObserver());

        // Show the Robocode battle view
        engine.setVisible(true);

        // Setup the battle specification

        int numberOfRounds = 5;
        BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600

        String customRobot = PACKAGE_WITH_ROBOTS_NAME + '.' + ROBOT_NAME;
        RobotSpecification[] selectedRobots =
                engine.getLocalRepository("sample.RamFire,sample.Corners," + customRobot + '*');

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