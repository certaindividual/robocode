package experimental;

import battle.BattleRunner;
import battle.RoboEnv;
import iwium.IRlRobot;
import iwium.QLearningForRobots;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.util.DataManager;
import org.nd4j.linalg.learning.config.RmsProp;
import robocode.*;
import robocode.exception.AbortedException;
import robocode.exception.DeathException;
import robocode.exception.WinException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IwiumRobot extends IRlRobot {

    public double nextReward = 0.0;
    public static boolean loadModel = true;

    static QLearning.QLConfiguration ROBOT_QL =
            new QLearning.QLConfiguration(
                    10,    //Random seed
                    Integer.MAX_VALUE,    //Max step By epoch
                    Integer.MAX_VALUE, //Max step
                    150000, //Max size of experience replay
                    32,     //size of batches
                    500,    //target update (hard)
                    10,     //num step noop warmup
                    0.01,   //reward scaling
                    0.99,   //gamma
                    1.0,    //td-error clipping
                    0.1f,   //min epsilon

                    // replace when testing:

                    1,   //num step for eps greedy anneal
                    // BattleRunner.NUM_BATTLES * 800,   //num step for eps greedy anneal

                    true    //double DQN
            );

    static DQNFactoryStdDense.Configuration ROBOT_NET =
            DQNFactoryStdDense.Configuration.builder()
                    .l2(0)
                    .updater(new RmsProp(0.01))
                    .numHiddenNodes(32)
                    .numLayer(2)
                    .build();

    static DataManager manager = null;
    static QLearningForRobots ql = null;
    public static RoboEnv mdp = null;

    public double popReward() {
        double reward = nextReward;
        nextReward = 0.0f;
        return reward;
    }

    public boolean isDone() {
        return mdp.done;
    }

    public static void appendScore(double score) {
        try {
        FileWriter fw = new FileWriter("scores.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(Double.toString(score));
        bw.newLine();
        bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("RUN CALLED");
        if(manager == null) {
            System.out.println("INIT CALLED");
            try {
                File f = getDataDirectory();
                manager = new DataManager(f.toString(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mdp = new RoboEnv(this);
            if (loadModel) {
                IDQN loaded = null;
                try {
                    loaded = DQNPolicy.load("rl_model.data").getNeuralNet();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ql = new QLearningForRobots(mdp, loaded, ROBOT_QL, manager);
            } else {
                ql = new QLearningForRobots(mdp, ROBOT_NET, ROBOT_QL, manager);
            }

            try {
                manager.writeInfo(ql);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mdp.setRobot(this);

        ql.preEpoch();
        mdp.done = false;
        mdp.totalRewards = 0.0;

        DataManager.StatEntry statEntry = null;
        try {
            statEntry = ql.trainEpoch();
        } catch (DeathException | WinException | AbortedException e) {
        } finally {
            ql.postEpoch();
            appendScore(mdp.totalRewards);
            ql.incrementEpoch();
            try {
                manager.appendStat(statEntry);
                manager.writeInfo(ql);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                ql.getPolicy().save("rlsaved.data");
            } catch (IOException e) {}
        }
    }

    @Override public void onHitWall(HitWallEvent hitWallEvent) {
        nextReward -= 100.f;
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        super.onHitByBullet(event);
        nextReward -= 5.f;
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {
        super.onBulletHit(event);
        nextReward += 20.0f;
    }

    @Override
    public void onDeath(DeathEvent event) {
        System.out.println("ended");
        nextReward -= 5.0f;
        mdp.done = true;
        super.onDeath(event);
    }

    @Override
    public void onWin(WinEvent event) {
        System.out.println("ended");
        nextReward += 5.0f;
        mdp.done = true;
        super.onWin(event);
    }

    @Override
    public void onBattleEnded(BattleEndedEvent event) {
        System.out.println("ended");
        mdp.done = true;
        super.onBattleEnded(event);
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        nextReward -= 5.0f;
        super.onHitRobot(event);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        super.onScannedRobot(event);
        nextReward += 10.0;
        mdp.foundBot = event;
    }
}
