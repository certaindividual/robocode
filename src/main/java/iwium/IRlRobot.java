package iwium;

import robocode.AdvancedRobot;

public abstract class IRlRobot extends AdvancedRobot {
    public abstract double popReward();
    public abstract boolean isDone();
}
