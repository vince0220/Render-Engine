package ML.NeuralNetwork;

public class TrainingSet {

	/**
	 * Contains trainings input
	 */
    public float[] input;
    
    /**
     * Contains ideaal targets
     */
    public float[] target;
    
    /**
     * contains training errors
     */
    public float error;

    /**
     * Creates a neuron trainingsample
     * @param input
     * @param target
     */
    public TrainingSet(final float[] input, final float[] target){
        this.input = input;
        this.target = target;

        error = 0;
    }
}
