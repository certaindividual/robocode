package experimental;

import iwium.Action;
import iwium.QLearningForRobots;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.nd4j.linalg.learning.config.RmsProp;
import robocode.*;

public class IwiumRobot extends AdvancedRobot {

    @Override
    public void run() {
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

        // QLearningForRobots<> ql = new QLearningForRobots();

        while(true) {
            ahead(100.0);
        }
    }
}
