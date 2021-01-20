package battle;

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

    DiscreteSpace actionSpace;
    ObservationSpace<Box> observationSpace;

    /*
    Actions:
        0-8 turn N*45 degrees clockwise (e.g. 0 -> 0, 2 -> 90) and go forward
        9 stay still
     */

    public RoboEnv(){
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
        return null;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public MDP<Box, Integer, DiscreteSpace> newInstance() {
        return new RoboEnv();
    }
}
