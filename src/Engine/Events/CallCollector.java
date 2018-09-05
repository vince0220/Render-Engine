package Engine.Events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CallCollector<T> {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private String methodName;
    private ArrayList<Method> calls = new ArrayList<Method>();
    private ArrayList<T> objects = new ArrayList<T>();

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public CallCollector(String methodName){
        this.methodName = methodName;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void Call(){
        for(int i = 0; i < calls.size(); i++){
            CallMethod(calls.get(i),objects.get(i));
        }
    }
    public void Clear(){
        calls.clear();
        objects.clear();
    }

    public void AddCall(T call){
        if(call != null) {
            Method m = FindMethod(call);
            if (m != null) {
                calls.add(m);
                objects.add(call);
            } // add method
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Method FindMethod(T call){
        try {
            return call.getClass().getMethod(methodName);
        } catch (SecurityException e) {}
        catch (NoSuchMethodException e) {}
        return null;
    }
    private void CallMethod(Method m,T call){
        try {
            m.invoke(call);
        } catch (IllegalArgumentException e) {}
        catch (IllegalAccessException e) {}
        catch (InvocationTargetException e) {}
    }
}
