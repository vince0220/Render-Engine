package ML;

import java.util.List;
import java.util.Random;

public class MLUtils {

    /**
     * Returns a random range between min and maxs
     * @param min min value
     * @param max max value
     * @return
     */
    public static float Random(final float min, final float max){
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    /**
     * Sigmoids a value
     * @param value to sigmoid
     * @return sigmoided value
     */
    public static float Sigmoid(final float value){
        return (float) (1/(1+Math.pow(Math.E,0-value)));
    }

    /**
     * Collective squared error
     * @param errors
     * @return collective squared error
     */
    public static float CollectiveError(final List<Float> errors){
        return errors.stream().reduce(0f,(a,b)-> a+b*b)/errors.size();
    }


    /**
     * Derivative of sigmoid
     * @param value
     * @return dirivative of a sigmoided value
     */
    public static float SigmoidDerivative(final float value){
        return value*(1-value);
    }
}
