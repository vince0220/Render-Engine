package ML.NeuralNetwork.Input;

import ML.NeuralNetwork.Connection;
import ML.NeuralNetwork.Layer;
import ML.NeuralNetwork.Network;
import ML.NeuralNetwork.Neuron;

public class InputLayer extends Layer {
	
    /**
     * Inits a layer with given amount of neurons
     *
     * @param amount neuron amount
     */
    public InputLayer(int amount) {
        super(amount);
    }

    /**
     * Feeds the network data forward
     */
    @Override
    public void FeedForward(){
        Network.logger.Log("Feeding forward from inputlayer");
        for(Neuron neuron : neurons){
            for(Connection connection : neuron.outgoingConnections){
                connection.value = neuron.value;
            }
        }
        rightLayer.FeedForward();
    }

    /**
     * Sets initial neuron data
     * @param data neuron data
     */
    public Layer SetData(final float[] data){
        Network.logger.Log("setting data to input layer");
        if(data.length!=neurons.size()){
            throw new Error("Input size does not match with amount of input neurons");
        }
        for(int i = 0; i < data.length; i++){
            Network.logger.Log("setting value "+data[i]+" to neuron "+i);
            neurons.get(i).value = data[i];
        }
        return this;
    }
}
