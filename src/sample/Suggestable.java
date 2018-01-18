package sample;

import java.util.ArrayList;
import java.util.PriorityQueue;


public class Suggestable
{
    private static ArrayList<String> mistakes =null;
    private static ArrayList<PriorityQueue<Levenshtein> > suggestions= null;
    private static Suggestable  s = new Suggestable();

    private Suggestable()
    {
        mistakes= new ArrayList<>();
        suggestions= new ArrayList<>();
    }

    public  ArrayList<String> getMistakes() {
        return mistakes;
    }

    public  ArrayList<PriorityQueue<Levenshtein>> getSuggestions() {
        return suggestions;
    }

    public static Suggestable getInstance() {
        return s;
    }

    public  void setMistakes(String s) {
        this.mistakes.add(s);
    }

    public  void setSuggestions(PriorityQueue<Levenshtein> suggestions) {
        this.suggestions.add(suggestions);
    }


}
