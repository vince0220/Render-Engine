package ML.NeuralNetwork;

public class Logger {

	/**
	 * Variable that stores if the logger should log or not
	 */
    public boolean shouldLog;

    /**
     * Creates instance of logger
     * @param log
     */
    public Logger(final boolean log){
        shouldLog = log;
    }

    /**
     *  The data to log
     * @param data
     */
    public void Log(final Object data){
        if(shouldLog){
            //System.out.println(data);
        }
    }
}
