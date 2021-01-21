package battle;

import experimental.IwiumRobot;
import iwium.IRlRobot;
import iwium.RobotObservationSpace;
import org.json.JSONArray;
import org.json.JSONObject;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

public class RoboEnv implements MDP<Box, Integer, DiscreteSpace> {

    public boolean done = false;
    public ScannedRobotEvent foundBot = null;

    IRlRobot robot;
    DiscreteSpace actionSpace;
    ObservationSpace<Box> observationSpace;

    /*
    Actions:
        turn left
        light turn left
        light turn right
        turn right
        forward 100
        backward 100
        shoot
        stay still
     */

    public RoboEnv(IRlRobot robot){
        this.robot = robot;
        this.actionSpace = new DiscreteSpace(8);
        this.observationSpace = new RobotObservationSpace();
    }

    @Override
    public ObservationSpace<Box> getObservationSpace() {
        return this.observationSpace;
    }

    @Override
    public DiscreteSpace getActionSpace() {
        return this.actionSpace;
    }

    @Override
    public Box reset() {
        System.out.println("reset called");
        return makeObservations();
    }

    @Override
    public void close() {
        System.out.println("close called");
    }

    @Override
    public StepReply<Box> step(Integer a) {
        switch(a){
            case 0:
                robot.turnLeft(40);
                break;
            case 1:
                robot.turnLeft(20);
                break;
            case 2:
                robot.turnRight(20);
                break;
            case 3:
                robot.turnRight(40);
                break;
            case 4:
                robot.ahead(100);
                break;
            case 5:
                robot.back(100);
                break;
            case 6:
                robot.fireBullet(Rules.MAX_BULLET_POWER);
                break;
            case 7:
                break;
        }
        JSONObject info = new JSONObject();
        // temporary, figure out a reward
        double reward = robot.popReward();
        boolean done = robot.isDone();
        return new StepReply<>(makeObservations(), reward, done, info);
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public MDP<Box, Integer, DiscreteSpace> newInstance() {
        return new RoboEnv(this.robot);
    }

    private Box makeObservations() {
        int fbDistance = 0;
        int fbBearing = 0;
        if(foundBot != null) {
            fbDistance = (int) (foundBot.getDistance() / 32);
            fbBearing = (int) (foundBot.getBearing() / 36);
        }
        int[] obs = new int[] {
            (int)(robot.getX() / 64), (int)(robot.getY() / 64),
            (int)(robot.getHeading() / 36),
            (int)(robot.getGunHeading() / 36),

            foundBot == null ? 0 : 1,
            fbDistance,
            fbBearing
        };
        JSONArray ja = new JSONArray(obs);
        return new Box(ja);
    }
}
