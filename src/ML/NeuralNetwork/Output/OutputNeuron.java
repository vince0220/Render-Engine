package ML.NeuralNetwork.Output;

import ML.MLUtils;
import ML.NeuralNetwork.Layer;
import ML.NeuralNetwork.Neuron;

public class OutputNeuron extends Neuron {

	/**
	 * Neuron preference value target
	 */
    public float target = 0;

    /**
     * Calculate the error by calculating the difference between the target value and the actual value
     */
    public void CalculateError(){
        error = target-value;
        delta = MLUtils.SigmoidDerivative(value) * error;
    }
}
