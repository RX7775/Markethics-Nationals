import java.util.*;
public class Conflict
{
    private Random rand;
    // Possible options
    private String[][] options = {
        {"COMPLY",
        "HIDE EVIDENCE"},
        {"GIVE IN TO DEMANDS",
        "BREAK THE STRIKE"}
    };
    // Responses
    private String[][] resString = {
        {"The investigation incriminated your business (-40 BE)",
        "The investigation found nothing major (+10 EP)",
        "The investigation uncovered your lies, and you got arrested (Game Lost)",
        "The investigation found nothing, you hid the evidence (+20 EP)"},
        {"The strikers were too angry and overthrew you (Game Lost)",
        "The strikers were satisfied by your deal, and you were forced to follow their demands (+20 EP, -30 BE)",
        "Your attempt was ultimately futile, and the strikers have overrun your business (Game Lost)",
        "Luckily, you were able to break up their strike, and come out unaffected (+10 BE)"}
    };
    // Changing EP and BE 
    public int[][] resEP = {
        {0,10,-100,20},
        {-100,20,-100,0}
    };
    public int[][] resBE = {
        {-40,0,-100,0},
        {-100,-30,-100,10}
    };
    // RNG probabilities 
    public int[][] chances = {
        {30,90}, 
        {60,40}
    };
    private int index, result;

    public Conflict()
    {
        ;
    }
    
    public void setConflict() {
        // Choose a random conflict 
        if (Math.random()*100<40) index=0;
        else index=1; 
    }
    
    public int getRes(int option, int ep) {
        // Return result
        if (option==1) {
            if (Math.random()*chances[index][0]<-ep) result=0;
            else result=1; 
        }
        else {
            if (Math.random()*100<chances[index][1]) result=2;
            else result=3; 
        }
        return result;
    }
    
    public int getIdx() {
        // Return index
        return index; 
    }
    
    public int ethicalChange() {
        // Return EP
        return resEP[index][result];
    }
    
    public int efficiencyChange() {
        // Return BE
        return resBE[index][result];
    }
    
    public String getStr() {
        // Return result Str
        return resString[index][result];
    }
    
    public String getOp(int idx) {
        // Return options
        return options[index][idx];
    }
}
