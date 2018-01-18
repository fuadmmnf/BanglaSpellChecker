package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.*;
import java.util.*;


public class Controller
{
    @FXML
    public BorderPane borderPane;
    @FXML
    private TextArea inputSentence,wrongText,engToBan;
    @FXML
    private ListView<Levenshtein> suggestions;

    private byte[] medium;
    private String input,output,testString,out;

    private int prevSize =0;

//Demo for only correcting mispelled words

    //inner class


    @FXML
    public void putCorrectSpelling(ActionEvent event)
    {
        String output="";

        String [] lines = inputSentence.getText().split("\n");

        for(String line : lines)
        {
            String [] words = line.split(" ");
            for( String s : words)
            {
                s=utfEncoding(s);
                if(!Suggestable.getInstance().getMistakes().contains(s))
                {
                    Suggestable.getInstance().setMistakes(s);

                    PriorityQueue<Levenshtein> suggests = new PriorityQueue<>(new Comparator<Levenshtein>() {
                        @Override
                        public int compare(Levenshtein o1, Levenshtein o2) {
                            if(o1.getLevenshteinValue()==o2.getLevenshteinValue()) return o2.getStrInfo().getFrequency()-o1.getStrInfo().getFrequency();
                            return o1.getLevenshteinValue()-o2.getLevenshteinValue();
                        }
                    });

                    List<StringInfo> bishuddhoList = Dictionary.getOnlyInstance().getFrequencyList();
                    for(StringInfo bishuddho: bishuddhoList)
                    {
                        int leven= calculateLevenshtein(s,s.length(),bishuddho.getWord(),bishuddho.getWord().length());
                        if(leven<=3)
                        {
                            suggests.add(new Levenshtein(bishuddho,leven));
                        }
                    }
                    Suggestable.getInstance().setSuggestions(suggests);

                    try
                    {
                        output+=suggests.peek().getStrInfo().getWord()+" ";
                    }
                    catch (NullPointerException e)
                    {
                        System.out.println(e.getStackTrace());
                    }

                }
                else
                {
                    /*                                */
                   // System.out.println(s+"       "+Suggestable.getInstance().getSuggestions().get(Suggestable.getInstance().getMistakes().indexOf(s)).peek().getStrInfo().getWord());
                    output+=((Suggestable.getInstance().getSuggestions()).get((Suggestable.getInstance().getMistakes()).indexOf(s))).peek().getStrInfo().getWord()+"\n";
                }
            }
            output+="\n";
        }

        inputSentence.setText(output);
    }

    public String convertToBangla(String input)
    {

        String bangla="";
        int strSize= input.length();
        for(int i=0; i<strSize ;i++)
        {
                String substr="";

                for(int j=i;j<strSize;j++)
                {
                    if(!Dictionary.getOnlyInstance().getPhonetics().keySet().contains(input.substring(i,j+1)))
                    {
                        i=j-1;
                        break;
                    }
                    substr=Dictionary.getOnlyInstance().getPhonetics().get(input.substring(i,j+1));

                }
           // System.out.println(substr);
            bangla+=substr;
        }

        return bangla;
    }

    public boolean isShoroborno(String ch)
    {

        if( ch.equals("অ") || ch.equals("া") || ch.equals("ি") || ch.equals("ী") || ch.equals("ু") || ch.equals("ূ") || ch.equals("ে") || ch.equals("ৈ") || ch.equals("ো") || ch.equals("ৌ") || ch.equals("ৃ") ||
        ch.equals("আ") || ch.equals("ই") || ch.equals("ঈ") || ch.equals("উ") || ch.equals("ঊ") || ch.equals("এ") || ch.equals("ঐ") || ch.equals("ও") || ch.equals("ঔ") || ch.equals("ঋ"))
            return true;
        return false;
    }

    public String getReplacer(String str)
    {
        if(str.equals("আ")) return"া";
        else if(str.equals("ই")) return"ি";
        else if(str.equals("অ")) return "অ";
        else if(str.equals("ঈ")) return"ী";
        else if(str.equals("উ")) return"ু";
        else if(str.equals("ঊ")) return"ূ";
        else if(str.equals("এ")) return"ে";
        else if(str.equals("ঐ")) return"ৈ";
        else if(str.equals("ও")) return"ো";
        else if(str.equals("ঔ")) return"ৌ";
        else if(str.equals("ঋ")) return"ৃ";
        return "";
    }

    @FXML
    public void detectMouse(MouseEvent event)
    {
        convertLanguage();
    }
    @FXML
    public void detectKeyboard(KeyEvent event)
    {
        convertLanguage();
    }

    public void convertLanguage()
    {
        String output="";
        String [] lines = engToBan.getText().trim().split("\n");
        for(String line : lines)
        {
            String [] words = line.trim().split("\\s");
            for(int wordNum=0; wordNum<words.length;wordNum++)
            {

                String banglawords = convertToBangla(words[wordNum]);

                String banglaOutput="";


                for(int i=1; i<banglawords.length();i++)
                {

                    if(isShoroborno(banglawords.substring(i,i+1)) && !isShoroborno(banglawords.substring(i-1,i)))
                    {
                        String replacer=getReplacer(banglawords.substring(i,i+1));
                        banglawords = banglawords.substring(0,i)+replacer+ banglawords.substring(i+1,banglawords.length()) ;
                    }
                    else if(!isShoroborno(banglawords.substring(i,i+1)) && !isShoroborno(banglawords.substring(i-1,i)))
                    {
                        String restwords=banglawords.substring(i,banglawords.length());

                        banglawords = banglawords.substring(0,i)+Dictionary.getOnlyInstance().getHoshonto()+ restwords;
                        i++;
                    }
                }
                if(banglawords.length()!=0)
                    banglawords= banglawords.substring(0,1)+banglawords.substring(1,banglawords.length()).replaceAll("অ","");

                output+=banglawords+" ";
            }
            output+="\n";
        }

        inputSentence.setText(output);
        /*
        }
        prevSize=inputSentence.getText().length();
        */
    }





    @FXML
    public void dynamicSuggestions(KeyEvent event)
    {
            String input = inputSentence.getText();

            int caretPos= inputSentence.getCaretPosition();
            int startPos=caretPos;
            int endPos= caretPos;
            if(caretPos==0 || input.charAt(caretPos-1)==' ') suggestions.getItems().clear();
            else
            {
                while(startPos>0 && input.charAt(startPos-1)!=' ')
                    startPos--;
                while(endPos<input.length() && input.charAt(endPos)!=' ')
                    endPos++;

                generateSuggestions(input.substring(startPos,endPos));

            }
    }


    public void generateSuggestions(String word)
    {

        PriorityQueue<Levenshtein> suggests = new PriorityQueue<>(new Comparator<Levenshtein>() {
            @Override
            public int compare(Levenshtein o1, Levenshtein o2) {
                if(o1.getLevenshteinValue()==o2.getLevenshteinValue()) return o2.getStrInfo().getFrequency()-o1.getStrInfo().getFrequency();
                return o1.getLevenshteinValue()-o2.getLevenshteinValue();
            }
        });
        word=utfEncoding(word);
        if(!Suggestable.getInstance().getMistakes().contains(word) && word!=null)
        {
            Suggestable.getInstance().setMistakes(word);



            List<StringInfo> bishuddhoList = Dictionary.getOnlyInstance().getFrequencyList();
            for(StringInfo bishuddho: bishuddhoList)
            {
                int leven= calculateLevenshtein(word,word.length(),bishuddho.getWord(),bishuddho.getWord().length());
                if(leven<=3)
                {

                    suggests.add(new Levenshtein(bishuddho,leven));

                }
            }
            Suggestable.getInstance().setSuggestions(suggests);
            suggestions.getItems().addAll(suggests);

        }
        else if(Suggestable.getInstance().getMistakes().contains(word))
        {
            try
            {
                suggestions.getItems().addAll(Suggestable.getInstance().getSuggestions().get(Suggestable.getInstance().getMistakes().indexOf(word)));
            }
            catch (NullPointerException e)
            {
                System.out.println(e.getStackTrace());
            }

        }
    }
    @FXML
    public void wordSuggestions(KeyEvent event)
    {

            if(event.isControlDown() )
            {
                String selectedText = inputSentence.getSelectedText();
                generateSuggestions(selectedText);
            }
            else
                suggestions.getItems().clear();



            String output="";
            for(String lines : inputSentence.getText().split("\n"))
            {
                for(String words : lines.split(" "))
                {
                    if(!Dictionary.getOnlyInstance().searchWord(words) && !words.equals(""))
                    {
                        output+=words+"\n";
                    }
                }
            }
            wrongText.setText(output);

    }


    private int calculateLevenshtein(String s1, int s1Length, String s2, int s2Length)
    {

        int [] costs = new int [s2Length + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= s1Length; i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= s2Length; j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), s1.charAt(i - 1) == s2.charAt(j - 1) ? nw : nw + 1);
               // if(cj>4) return 10;
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[s2Length];
    }


    @FXML
    public void putSuggestion(KeyEvent event)
    {
        if(event.isControlDown() && event.isAltDown())
        {
            String selectedSuggestion = suggestions.getSelectionModel().getSelectedItem().getStrInfo().getWord();

            if(inputSentence.getSelectedText().length()>=1)
            {
                String input = inputSentence.getText();

                String selectedText = inputSentence.getSelectedText();

                int startPos= inputSentence.getCaretPosition();
                while(startPos>0 && inputSentence.getText().charAt(startPos-1)!=' ')
                    startPos--;

                inputSentence.setText(input.substring(0,startPos)+selectedSuggestion+input.substring(startPos+selectedText.length(),input.length()));


                 /*increasing frequency*/
                Dictionary.getOnlyInstance().updateFrequency(selectedSuggestion);
            }
        }

       // inputSentence.getSelectedText().replace(inputSentence.getSelectedText(), suggestions.getSelectionModel().getSelectedItem().getStrInfo().getWord());

    }


    private String utfEncoding(String str)
    {
        try
        {
            byte [] bytes = str.getBytes("UTF8");
            return new String(bytes,"UTF8");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
