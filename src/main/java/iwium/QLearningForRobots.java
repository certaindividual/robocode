package iwium;

import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscrete;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.dqn.DQNFactory;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.Encodable;
import org.deeplearning4j.rl4j.util.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rubenfiszel (ruben.fiszel@epfl.ch) on 8/6/16.
 * Based on parts of https://github.com/deeplearning4j/rl4j Apache licensed
 */
public class QLearningForRobots<O extends Encodable> extends QLearningDiscrete<O> {
    public QLearningForRobots(MDP<O, Integer, DiscreteSpace> mdp, IDQN dqn, org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning.QLConfiguration conf,
                                  DataManager dataManager) {
        super(mdp, dqn, conf, dataManager, conf.getEpsilonNbStep());
    }

    public QLearningForRobots(MDP<O, Integer, DiscreteSpace> mdp, DQNFactory factory,
                                  org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning.QLConfiguration conf, DataManager dataManager) {
        this(mdp, factory.buildDQN(mdp.getObservationSpace().getShape(), mdp.getActionSpace().getSize()), conf,
                dataManager);
    }

    public QLearningForRobots(MDP<O, Integer, DiscreteSpace> mdp, DQNFactoryStdDense.Configuration netConf,
                                  QLearning.QLConfiguration conf, DataManager dataManager) {
        this(mdp, new DQNFactoryStdDense(netConf), conf, dataManager);
    }

    @Override
    public DataManager.StatEntry trainEpoch() {
        InitMdp<O> initMdp = initMdp();
        O obs = initMdp.getLastObs();

        double reward = initMdp.getReward();
        int step = initMdp.getSteps();

        Double startQ = Double.NaN;
        double meanQ = 0;
        int numQ = 0;
        List<Double> scores = new ArrayList<>();
        while (step < getConfiguration().getMaxEpochStep() && !getMdp().isDone()) {

            if (getStepCounter() % getConfiguration().getTargetDqnUpdateFreq() == 0) {
                updateTargetNetwork();
            }

            QLStepReturn<O> stepR = trainStep(obs);

            if (!stepR.getMaxQ().isNaN()) {
                if (startQ.isNaN())
                    startQ = stepR.getMaxQ();
                numQ++;
                meanQ += stepR.getMaxQ();
            }

            if (stepR.getScore() != 0)
                scores.add(stepR.getScore());

            reward += stepR.getStepReply().getReward();
            obs = stepR.getStepReply().getObservation();
            incrementStep();
            step++;
        }

        meanQ /= (numQ + 0.001); //avoid div zero

        DataManager.StatEntry statEntry = new QLStatEntry(getStepCounter(), getEpochCounter(), reward, step, scores,
                getEgPolicy().getEpsilon(), startQ, meanQ);

        return statEntry;
    }
}
