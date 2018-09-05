package Game.AI;

import ML.NeuralNetwork.Network;
import ML.NeuralNetwork.TrainingSet;

import java.util.ArrayList;
import java.util.List;

public class Brain {

    private static Brain _instance;
    
    /**
     * Returns and creates the brain instance
     * @return
     */
    public static Brain GetInstance(){
        return _instance = _instance == null? new Brain() : _instance;
    }

    /**
     * Neural network used for training the ai/brain
     */
    public Network network;

    /**
     * List of trainingsbatches
     */
    public List<TrainingSet> batch;

    private boolean preTrained = false;

    private Brain(){
        batch = new ArrayList<>();
        network = new Network(false)
                .addInputLayer(1)
                .addHiddenLayer(4)
                .addHiddenLayer(4)
                .addOutputLayer(1)
                .setLearnRate(0.1f)
                .build();
        presetData();
    }

    private void presetData(){
        batch.add(new TrainingSet(new float[] { 0 }, new float[] {0}));

        batch.add(new TrainingSet(new float[] { -0.3f }, new float[] {1}));
        batch.add(new TrainingSet(new float[] { 0.3f }, new float[] {0}));
    }

    /**
     * Pre-trains de ai
     */
    public void PreTrain(){
        if(preTrained)return;
        System.out.println("training..");
        network.train(toArray(batch),/*Integer.MAX_VALUE*/2000,0.0002f);//400 gameplay
        preTrained = true;
    }

    /**
     * Learn from avarage
     * @param activities
     */
    public void LearnFromAvarage(final List<Activity> activities){
        if(activities.size()<1)return;

        float avX = 0;
        float avY = 0;
        float navX = 0;
        float navY = 0;

        for(final Activity act : activities){
            if(act.success){
                avX+=act.x;
                avY+=act.y;
            } else {
                navX+=act.x;
                navY+=act.y;
            }
        }
        avX/=activities.size();
        avY/=activities.size();
        navX/=activities.size();
        navY/=activities.size();

        batch.add(new TrainingSet(new float[] { /*avX,*/ avY}, new float[] { 1 }));
        batch.add(new TrainingSet(new float[] { /*navX,*/ navY}, new float[] { 0 }));
        network.train(toArray(batch),5000,0.00002f);
    }

    /**
     * Lears from activities
     */
    public void Learn(){
        network.train(toArray(batch),5000,0.0002f);
    }

    /**
     * Learns from activities
     * @param activities all the activities of the birds
     */
    public void Learn(final List<Activity> activities)
    {
        if (activities.size() < 1) return;

        final TrainingSet[] sets = new TrainingSet[activities.size()];
        for(int i = 0; i < activities.size(); i++)
        {
            final Activity activity = activities.get(i);
            if (activity != null)
            {
                TrainingSet set = new TrainingSet(
                        new float[] { /*activity.x,*/ activity.y }, new float[] { activity.success ? 1 : 0 });
                sets[i] = set;
            }
        }
        if (sets.length < 1) return;
        network.train(sets, 1000, 0.002f);
    }

    /**
     * Converts a activity list to a activity array
     * @param activities
     * @return
     */
    private TrainingSet[] toArray(final List<TrainingSet> activities){
        final TrainingSet[] sets = new TrainingSet[activities.size()];
        for(int i = 0; i < activities.size(); i++)sets[i]=activities.get(i);
        return sets;
    }

}
