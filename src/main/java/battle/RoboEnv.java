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

    public Double totalRewards = 0.0;
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
        this.actionSpace = new DiscreteSpace(7);
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
                robot.setTurnLeft(90);
                robot.setAhead(100);
                robot.execute();
                break;
            case 1:
                robot.setTurnLeft(45);
                robot.setAhead(100);
                robot.execute();
                break;
            case 2:
                robot.setTurnRight(45);
                robot.setAhead(100);
                robot.execute();
                break;
            case 3:
                robot.setTurnRight(90);
                robot.setAhead(100);
                robot.execute();
                break;
            case 4:
                robot.setFire(Rules.MAX_BULLET_POWER);
                robot.setBack(50);
                robot.execute();
                break;
            case 5:
                robot.fire(Rules.MAX_BULLET_POWER);
                break;
            case 6:
                robot.setFire(Rules.MAX_BULLET_POWER);
                robot.setAhead(100);
                robot.execute();
                break;
        }
        JSONObject info = new JSONObject();
        double reward = robot.popReward();
        totalRewards += reward;
        boolean done = robot.isDone();
        return new StepReply<>(makeObservations(), reward, done, info);
    }

    public void setRobot(IRlRobot robot) {
        this.robot = robot;
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
        float fbDistance = 0;
        float fbBearing = 0;
        if(foundBot != null) {
            fbDistance = (float)foundBot.getDistance();
            fbBearing = ((float)foundBot.getBearing() + 180.0f);
        }
        float[] obs = new float[] {
                (float) robot.getX(), (float) robot.getY(),
                (float) robot.getHeading(),

                foundBot == null ? 0 : 1,
                fbDistance,
                fbBearing
        };
        JSONArray ja = new JSONArray(obs);
        foundBot = null;
        return new Box(ja);
    }
}
