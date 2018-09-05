package ML.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

import ML.MLUtils;
import ML.NeuralNetwork.Connection;

public class Neuron {

	/**
	 * List of incoming and outgoing neuron connections
	 */
    public List<Connection> incomingConnections,outgoingConnections;
    
    /**
     * Neuron input values, error and derivatives
     * 
     * https://en.wikipedia.org/wiki/Artificial_neuron
     */
    public float value,error,delta,bias;

    /**
     * Constructs a neural neuron 
     */
    public Neuron(){
        incomingConnections = new ArrayList<Connection>();
        outgoingConnections = new ArrayList<Connection>();

        error = 0;
        delta = 0;
        value = 0;
        bias = MLUtils.Random(-0.5f,0.5f);
    }

    /**
     * Feeds data forward
     */
    public void FeedForward(){
        Network.logger.Log("\nFeedforward in neuron");
        Transfer();
        Activate();
        for(final Connection connection : outgoingConnections){
            connection.value = value;
        }
    }

    /**
     * Propagates backward trough the network to calculate the errors
     */
    public void Propagate(final Layer layer){
        CalculateError(layer);
    }

    /**
     * Calculates neuron error and delta by multiplying the connection weight with the delta of the right layer neuron delta
     * @param parentLayer
     */
    protected void CalculateError(final Layer parentLayer){
        float tempError = 0;
        for(final Neuron neuron : parentLayer.rightLayer.neurons){
            for(final Connection connection : neuron.incomingConnections){
                for(final Connection outputConnection : outgoingConnections){
                    if(connection.equals(outputConnection)){
                        tempError += connection.weight * neuron.delta;
                    }
                }
            }
        }
        error = tempError;
        delta = MLUtils.SigmoidDerivative(value) * error;
    }

    /**
     * Updates the neuron values and connection weights
     * @param learnRate
     */
    public void Update(final float learnRate){
        for(final Connection connection : incomingConnections){
            connection.weight += delta * connection.value * learnRate;
        }
        bias+=learnRate*delta;
    }

    /**
     * Processes incoming values
     */
    private void Transfer(){
        if(incomingConnections.size()<1)return;
        float sum = 0;
        for(Connection connection : incomingConnections){
            Network.logger.Log("connection weight = "+connection.weight);
            Network.logger.Log("connection value = "+connection.value);
            Network.logger.Log("connection product = "+connection.value*connection.weight);
            sum+=connection.value*connection.weight;
        }
        Network.logger.Log("total value for transfer = "+sum);
        sum += bias;
        Network.logger.Log("total value and bias = "+sum);
        value = sum;
    }

    /**
     * Processes output
     */
    private void Activate(){
        value = MLUtils.Sigmoid(value);
        Network.logger.Log("After sigmoid = "+value);
    }
}
