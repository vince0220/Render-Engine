package Engine.Core.Input;

import javafx.scene.input.KeyCode;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InputManager implements KeyListener,MouseListener {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Singleton
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private static InputManager _i;

    /**
     * Singleton instance of input manager
     * @return
     */
    public static InputManager I(){
        if(_i == null){
            _i = new InputManager();
        }
        return _i;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Map<Integer,Integer> keyStates = new HashMap<Integer, Integer>();
    private Map<Integer,ArrayList<Integer>> keyHistory = new HashMap<Integer, ArrayList<Integer>>();

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Update input manager
     */
    public void UpdateManager(){
        for(int key : keyStates.keySet()){
            UpdateState(key,keyStates.get(key)); // update keys
        }
    }

    // Outputs
    /**
     * Check if a certain key is hold down
     * @param code key code
     * @return true for hold down, false for not
     */
    public boolean GetKey(KeyCode code){
        return CheckState(KeycodeToInt(code),2);
    }

    /**
     * Check if a certain key is pressed down
     * @param code key code
     * @return true if key is down
     */
    public boolean GetKeyDown(KeyCode code){
        return CheckState(KeycodeToInt(code),new int[]{1});
    }

    /**
     * Check if a certain key is released up
     * @param code key code
     * @return true if key is up released
     */
    public boolean GetKeyUp(KeyCode code){
        return CheckState(KeycodeToInt(code),3);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void UpdateKey(int code, int state){
        UpdateKey(code,state,new int[0]);
    }
    private void UpdateKey(int code, int state,int[] connections){
        EnsureKey(code);

        int current = keyStates.get(code);
        if(connections != null && connections.length > 0){
            boolean connected = false;
            for(int i = 0; i < connections.length; i++){
                if(current == connections[i]){connected = true;}
            }
            if(!connected){return;} // dont update since cant connect
        }

        // update
        keyStates.replace(code,state); // update key state
    }
    private void EnsureKey(int code){
        if(!keyStates.containsKey(code)){ keyStates.put(code,0); }
    }
    private void EnsureHistory(int code){
        if(!keyHistory.containsKey(code)){keyHistory.put(code,new ArrayList<Integer>());}
    }
    private void AddHistory(int code, int state){
        EnsureHistory(code);
        keyHistory.get(code).add(state);
    }

    private boolean CheckState(int code,int state){
        return CheckState(code,new int[]{state});
    }
    private boolean CheckState(int code, int[] states){
        EnsureKey(code);

        int currentState = keyStates.get(code);

        boolean match = false;
        for(int i = 0; i < states.length; i++){
            if(currentState == states[i]){
                match = true;
                break;
            }
        }

        return match;
    }
    private boolean CheckHistory(int code, int state){
        return CheckHistory(code,new int[]{state});
    }
    private boolean CheckHistory(int code, int[] states){
        EnsureHistory(code);
        
        ArrayList<Integer> history = keyHistory.get(code);

        boolean match = false;
        for(int i = 0; i < states.length; i++){
            if(history.contains(states[i])){
                match = true;
                break;
            }
        }

        return match;
    }
    private void UpdateState(int code, int state){
        EnsureHistory(code);

        ArrayList<Integer> History = keyHistory.get(code);
        if(History.size() > 0) {
            UpdateKey(code, History.get(0)); // update key state
            History.remove(0);
        } else {
            if(state == 1){ // if down set to pressed
                state = 2;
            }
            UpdateKey(code, state); // update key state
        }
    }


    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Listeners
    // ------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(!CheckHistory(e.getKeyCode(),1) && CheckState(e.getKeyCode(),new int[]{0,3})){
            AddHistory(e.getKeyCode(),1);
        }

    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(!CheckHistory(e.getKeyCode(),3) && CheckState(e.getKeyCode(),new int[]{1,2})){
            AddHistory(e.getKeyCode(),3);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Statics
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public static int KeycodeToInt(KeyCode code){
        switch (code){
            case CANCEL: return 3;
            case BACK_SPACE: return 8;
            case TAB: return 9;
            case ENTER: return 10;
            case CLEAR: return 12;
            case SHIFT: return 16;
            case CONTROL: return 17;
            case ALT: return 18;
            case PAUSE: return 19;
            case CAPS: return 20;
            case KANA: return 21;
            case FINAL: return 24;
            case KANJI: return 25;
            case ESCAPE: return 27;
            case CONVERT: return 28;
            case NONCONVERT: return 29;
            case ACCEPT: return 30;
            case MODECHANGE: return 31;
            case SPACE: return 32;
            case PAGE_UP: return 33;
            case PAGE_DOWN: return 34;
            case END: return 35;
            case HOME: return 36;
            case LEFT: return 37;
            case UP: return 38;
            case RIGHT: return 39;
            case DOWN: return 40;
            case COMMA: return 44;
            case MINUS: return 45;
            case PERIOD: return 46;
            case SLASH: return 47;
            case DIGIT0: return 48;
            case DIGIT1: return 49;
            case DIGIT2: return 50;
            case DIGIT3: return 51;
            case DIGIT4: return 52;
            case DIGIT5: return 53;
            case DIGIT6: return 54;
            case DIGIT7: return 55;
            case DIGIT8: return 56;
            case DIGIT9: return 57;
            case SEMICOLON: return 59;
            case EQUALS: return 61;
            case A: return 65;
            case B: return 66;
            case C: return 67;
            case D: return 68;
            case E: return 69;
            case F: return 70;
            case G: return 71;
            case H: return 72;
            case I: return 73;
            case J: return 74;
            case K: return 75;
            case L: return 76;
            case M: return 77;
            case N: return 78;
            case O: return 79;
            case P: return 80;
            case Q: return 81;
            case R: return 82;
            case S: return 83;
            case T: return 84;
            case U: return 85;
            case V: return 86;
            case W: return 87;
            case X: return 88;
            case Y: return 89;
            case Z: return 90;
            case OPEN_BRACKET: return 91;
            case BACK_SLASH: return 92;
            case CLOSE_BRACKET: return 93;
            case NUMPAD0: return 96;
            case NUMPAD1: return 97;
            case NUMPAD2: return 98;
            case NUMPAD3: return 99;
            case NUMPAD4: return 100;
            case NUMPAD5: return 101;
            case NUMPAD6: return 102;
            case NUMPAD7: return 103;
            case NUMPAD8: return 104;
            case NUMPAD9: return 105;
            case F1: return 112;
            case F2: return 113;
            case F3: return 114;
            case F4: return 115;
            case F5: return 116;
            case F6: return 117;
            case F7: return 118;
            case F8: return 119;
            case F9: return 120;
            case F10: return 121;
            case F11: return 122;
            case F12: return 123;
        }
        return 0; // default
    }
}
