package ML.NeuralNetwork.Output;

import ML.NeuralNetwork.Layer;
import ML.NeuralNetwork.Network;
import ML.NeuralNetwork.Neuron;

public class OutputLayer extends Layer {

	/**
	 * Error of all the neurons in the layer
	 */
    public float error = 0;

    /**
     * Inits a layer with given amount of neurons
     *
     * @param amount amount of neurons 
     */
    public OutputLayer(int amount) {
        super(0);
        for(int i = 0; i < amount; i++){
            neurons.add(new OutputNeuron());
        }
        Network.logger.Log("creating layer with "+amount+"neurons");
    }

    /**
     * Starts backpropagation
     */
    @Override
    public void Propagate(){
        CalculateError();
        leftLayer.Propagate();
    }

    /**
     * Calculates the total error of the layer
     * @return error 
     */
    private float CalculateError(){
        float tempError = 0;
        for(int i = 0; i < neurons.size(); i++){
            final OutputNeuron neuron = (OutputNeuron)neurons.get(i);
            neuron.CalculateError();
            tempError+=neuron.error;
        }
        return error = tempError;
    }

    /**
     * Set target values for the neurons in the layer
     * @param targets neuron targets
     */
    public void SetTargets(final float[] targets){
        if(targets.length!=neurons.size()){
            throw new Error("Target size does not match with amount of output neurons");
        }
        for(int i = 0; i < targets.length; i++){
            ((OutputNeuron)neurons.get(i)).target = targets[i];
        }
    }
}
