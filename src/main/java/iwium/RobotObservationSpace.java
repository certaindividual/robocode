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

    /*
        Observations:
            our position (x,y)
            our heading
            our gun heading

            found bot? flag
            found bot distance
            found bot bearing
     */

    public RobotObservationSpace() {
        low = Nd4j.create(new float[]{0.0f, 0.0f,
                0.0f,
                0.0f,

                0.0f,
                0.0f,
                -180.0f
        });
        high = Nd4j.create(new float[] {640.0f, 640.0f,
            360.0f,
            360.0f,

            1.0f,
            640.0f,
            180.0f
        });
        shape = new int[] {7};
    }

    @Override
    public String getName() {
        return "Box";
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
