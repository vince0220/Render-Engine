package ML.NeuralNetwork;

import ML.MLUtils;

/**
 * Class that helps keeping track of a connection so that we can apply the command pattern for feedforward and backpropagation
 *
 * https://en.wikipedia.org/wiki/Command_pattern
 */
public class Connection {

	/**
	 * Weight of a neuron connection
	 */
    public float weight;
    
    /**
     * Value of a neuron connection
     */
    public float value = 0;

    /**
     * Constructs a connection
     */
    public Connection(){
        weight = MLUtils.Random(-0.5f,0.5f);
    }
}
