package iwium;

import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class RobotObservationSpace implements ObservationSpace<Box> {

    int[] shape;
    INDArray low;
    INDArray high;

    public RobotObservationSpace() {
        low = Nd4j.create(new float[] {Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE});
        high = Nd4j.create(new float[] {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE});
        shape = new int[] {5};
    }

    @Override
    public String getName() {
        return "Box(5,)";
    }

    @Override
    public int[] getShape() {
        return shape;
    }

    @Override
    public INDArray getLow() {
        return low;
    }

    @Override
    public INDArray getHigh() {
        return high;
    }
}
