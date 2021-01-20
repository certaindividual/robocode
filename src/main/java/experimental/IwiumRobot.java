package experimental;

import battle.RoboEnv;
import iwium.QLearningForRobots;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;
import org.nd4j.linalg.learning.config.RmsProp;
import robocode.*;

import java.io.File;
import java.io.IOException;

public class IwiumRobot extends AdvancedRobot {

    @Override
    public void run() {
        DataManager manager = null;
        try {
            File f = getDataDirectory();
            manager = new DataManager(f.toString(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        QLearning.QLConfiguration ROBOT_QL =
                new QLearning.QLConfiguration(
                        2136,    //Random seed
                        200,    //Max step By epoch
                        150000, //Max step
                        150000, //Max size of experience replay
                        32,     //size of batches
                        500,    //target update (hard)
                        10,     //num step noop warmup
                        0.01,   //reward scaling
                        0.99,   //gamma
                        1.0,    //td-error clipping
                        0.1f,   //min epsilon
                        1000,   //num step for eps greedy anneal
                        true    //double DQN
                );

        DQNFactoryStdDense.Configuration ROBOT_NET =
                DQNFactoryStdDense.Configuration.builder()
                        .l2(0)
                        .updater(new RmsProp(0.000025))
                        .numHiddenNodes(300)
                        .numLayer(2)
                        .build();

        RoboEnv mdp = new RoboEnv(this);
        QLearningForRobots ql = new QLearningForRobots(mdp, ROBOT_NET, ROBOT_QL, manager);

        ql.trainEpoch();
    }
}
