package ML.NeuralNetwork;

import ML.MLUtils;
import ML.NeuralNetwork.Input.InputLayer;
import ML.NeuralNetwork.Output.OutputLayer;

import java.util.ArrayList;
import java.util.List;

public class Network {

	/**
	 * The neural logging helper
	 */
    public static Logger logger;

    /**
     * Neural data input layer
     */
    public InputLayer inputLayer;
    
    /**
     * Neural data output layer
     */
    public OutputLayer outputLayer;
    
    /**
     * Network learn rate
     */
    public float learnRate = 0.3f;
    private List<Layer> hiddenLayers;

    /**
     * Constructs Network 
     * @param debug describes if the network should output logs
     */
    public Network(boolean debug){
        logger = new Logger(debug);
        hiddenLayers = new ArrayList<>();
    }

    /**
     * builds the network
     * @return
     */
    public Network build(){
        connectLayers();
        return this;
    }

    /**
     *
     * @param amount
     * @return
     */
    public Network addHiddenLayer(final int amount){
        hiddenLayers.add(new Layer(amount));
        return this;
    }

    /**
     * Sets the input layer
     * @param amount
     * @return
     */
    public Network addInputLayer(final int amount){
        inputLayer = new InputLayer(amount);
        return this;
    }

    /**
     * sets the outputLayer
     * @param amount
     * @return
     */
    public Network addOutputLayer(final int amount){
        outputLayer = new OutputLayer(amount);
        return this;
    }

    /**
     * Sets the learnrate of the network
     * @param rate
     * @return
     */
    public Network setLearnRate(final float rate){
        learnRate = rate;
        return this;
    }

    /**
     * Connects all given layers
     */
    protected void connectLayers(){
        if(inputLayer==null || outputLayer == null){
            throw new Error("No in or output layer defined");
        }
        if(hiddenLayers.size()<1)throw new Error("No hidden layers defined");
        inputLayer.ConnectTo(hiddenLayers.get(0));
        for(int i = 0; i < hiddenLayers.size(); i++){
            final Layer layer = hiddenLayers.get(i);
            if(i+1==hiddenLayers.size()){
                layer.ConnectTo(outputLayer);
                break;
            }
            layer.ConnectTo(hiddenLayers.get(i+1));
        }
    }

    /**
     * Trains the network
     * @param trainingsets
     * @param iterations
     * @param until
     */
    public void train(final TrainingSet[] trainingsets, final int iterations, final float until){
        for(int i = 0; i < iterations; i++){
            final List<Float> errors = new ArrayList<Float>();
            for(TrainingSet set: trainingsets){
                outputLayer.SetTargets(set.target);
                run(set.input);
                propagate();
                update();
                set.error = outputLayer.error;
                errors.add(set.error);
            }

            float trainingError = MLUtils.CollectiveError(errors);
            if(i%1000==0)Network.logger.Log("Network error: = "+trainingError);
            if(trainingError<until)break;
        }
    }

    /**
     * Backpropagates trough the network
     */
    private void propagate(){
        outputLayer.Propagate();
    }

    /**
     * Updates the weights
     */
    private void update(){
        inputLayer.Update(learnRate);
    }

    /**
     * Runs data trough the neural network
     * @param data
     * @return
     */
    public List<Neuron> run(float[] data){
        inputLayer.SetData(data)
                  .FeedForward();
        return outputLayer.neurons;
    }
}
