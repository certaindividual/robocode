package battle;

import org.json.JSONArray;
import org.json.JSONObject;
import robocode.AdvancedRobot;
import iwium.RobotObservationSpace;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ActionSpace;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.nd4j.linalg.factory.Nd4j;
import org.deeplearning4j.nn.api.*;

public class RoboEnv implements MDP<Box, Integer, DiscreteSpace> {

    AdvancedRobot robot;
    DiscreteSpace actionSpace;
    ObservationSpace<Box> observationSpace;

    /*
    Actions:
        0-8 turn N*45 degrees clockwise (e.g. 0 -> 0, 2 -> 90) and go forward
        9 stay still
     */

    public RoboEnv(AdvancedRobot robot){
        this.robot = robot;
        this.actionSpace = new DiscreteSpace(9);
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
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public StepReply<Box> step(Integer a) {
        if(a >= 0 && a < 9) {
            this.robot.setTurnRight(a * 45.0);
            this.robot.ahead(75);
        } else if (a == 9) {
            this.robot.doNothing();
        }
        JSONObject info = new JSONObject();
        // temporary, figure out a reward
        return new StepReply<>(makeObservations(), 0.0, false, info);
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public MDP<Box, Integer, DiscreteSpace> newInstance() {
        return new RoboEnv(this.robot);
    }

    private Box makeObservations() {
        // temporary
        int[] obs = new int[] { 1,2,3,2,1 };
        JSONArray ja = new JSONArray(obs);
        return new Box(ja);
    }
}
