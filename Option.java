import java.util.*;
public class Option
{
    // List of possible events 
    private String[] choices = { "Invest in renewable energy sources (e.g., solar or wind power)", "Implement fair wages for all employees", "Offer paid parental leave for employees", "Provide educational scholarships or funding to underprivileged communities", "Increase transparency in financial reporting", "Reduce carbon emissions by using sustainable materials", "Support local communities by sourcing materials locally", "Offer fair trade products to support developing countries", "Donate a percentage of profits to charity", "Encourage diversity and inclusion in the workplace", "Invest in clean water initiatives in underdeveloped areas", "Offer employees mental health support programs", "Create programs to promote employee wellness (e.g., gym memberships, wellness days)", "Develop environmentally friendly packaging to reduce waste", "Promote eco-friendly transportation options for employees", "Create a program to recycle electronic waste", "Offer internships or training programs for marginalized groups", "Sponsor educational programs for children in developing countries", "Provide paid volunteer time off for employees to work on community projects", "Promote ethical sourcing of raw materials (e.g., no child labor, fair wages)", "Use child labor in production to reduce costs", "Cut wages and benefits to increase profits", "Exploit workers in developing countries with unsafe working conditions", "Use unsustainable farming or fishing practices that harm the environment", "Manufacture cheap products with hazardous chemicals that harm workers and consumers", "Engage in false advertising or misleading marketing", "Discriminate against employees based on gender, race, or sexual orientation", "Bribe officials to bypass regulations and avoid taxes", "Exploit tax loopholes to avoid paying fair taxes", "Use planned obsolescence (e.g., making products break to force repurchases)" };
    
    // EP change
    public int[] ethicalPoints = {10, 8, 7, 9, 6, 8, 7, 8, 10, 6, 8, 7, 7, 6, 7, 7, 9, 10, 8, 9, -10, -8, -9, -8, -7, -9, -10, -10, -8, -7};

    
    // BE change
    public int[] efficiencyPoints = {-4, -9, -7,-1, 2, -4, -3, -4, -1, 1, -4, 0, -5, -1, -3, -2, -1, -1, -3, 0, 7, 9, 7, 7, 6, 7, 5, 10, 8, 7};

    // Chosen decision 
    private int index;
    
    public double[] multiplier = {0.7, 1, 1.3}; 

    public Option()
    {
        // default 
        index = 0; 
        for (int i = 0; i < 30; i++) {
            if (ethicalPoints[i]<0) ethicalPoints[i]*=multiplier[GameSettings.getDifficulty()-1];
            else ethicalPoints[i]*=multiplier[3-GameSettings.getDifficulty()]; 
            if (efficiencyPoints[i]<0) efficiencyPoints[i]*=multiplier[GameSettings.getDifficulty()-1]; 
            else efficiencyPoints[i]*=multiplier[3-GameSettings.getDifficulty()]; 
        }
    }
    
    public void reroll() {
        // choose new decision prompt
        index++; 
        if (index == choices.length) index = 0; 
    }
    
    public void Swap(int i, int j) {
        // swap two decision prompts 
        String temp = choices[i];
        choices[i] = choices[j];
        choices[j] = temp;
        int Temp = ethicalPoints[i];
        ethicalPoints[i] = ethicalPoints[j];
        ethicalPoints[j] = Temp;
        Temp = efficiencyPoints[i];
        efficiencyPoints[i] = efficiencyPoints[j];
        efficiencyPoints[j] = Temp; 
    }
    
    public void randomize() {
        // randomize the order of the decision prompts 
        index = 0;
        for (int i = 1; i < choices.length; i++) {
            int index = (int)(Math.random() * i); 
            Swap(i, index); 
        }
    }
    
    public int getIndex() {
        // return index
        return index;
    }
    
    public int ethicalChange() {
        // return EP 
        return ethicalPoints[index];
    }
    
    public int efficiencyChange() {
        // return BE 
        return efficiencyPoints[index];
    }
    
    public String toString() {
        // return decision prompt
        return choices[index];
    }
}
