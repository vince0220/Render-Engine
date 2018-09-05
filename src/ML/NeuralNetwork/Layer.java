package ML.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class Layer {

	/**
	 * List of neurons in this layer
	 */
    public List<Neuron> neurons;
    
    /**
     * Left and right aligning layers relative to this layer
     */
    public Layer leftLayer, rightLayer;

    /**
     * Inits a layer with given amount of neurons
     * @param amount amount of neurons
     */
    public Layer(final int amount){
        neurons = new ArrayList<Neuron>();
        for(int i = 0; i < amount; i++){
            neurons.add(new Neuron());
        }
        Network.logger.Log("creating layer with "+amount+"neurons");
    }

    /**
     * Connects this layer left of the given layer
     * @param layer layer to connect tos
     */
    public void ConnectTo(final Layer layer){
        Network.logger.Log("Connecting layer");
        Network.logger.Log(layer);
        rightLayer = layer;
        layer.leftLayer = this;
        for(final Neuron neuron : neurons){
            for(final Neuron nextLayerNeuron : layer.neurons){
                final Connection connection = new Connection();
                neuron.outgoingConnections.add(connection);
                nextLayerNeuron.incomingConnections.add(connection);
            }
        }
    }

    /**
     * Feeds data forward
     */
    public void FeedForward(){
        Network.logger.Log("Feedforward in layer");
        for(final Neuron neuron : neurons){
            neuron.FeedForward();
        }
        if(rightLayer!=null)rightLayer.FeedForward();
    }

    /**
     * Propagates backward trough the network to calculate the errors
     */
    public void Propagate(){
        for(Neuron neuron : this.neurons){
            neuron.Propagate(this);
        }
        if(leftLayer!=null)
            leftLayer.Propagate();
    }

    /**
     * Updates the neuron values and connection weights
     * @param learnRate learnrate of the ai
     */
    public void Update(final float learnRate){
        for(Neuron neuron : neurons){
            neuron.Update(learnRate);
        }
        if(rightLayer!=null)
            rightLayer.Update(learnRate);
    }
}
