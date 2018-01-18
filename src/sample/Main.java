package sample;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("SPL3");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args)
    {
       // String name = JOptionPane.showInputDialog("Enter your name");
        Dictionary.getOnlyInstance().loadSuggestions();
        launch(args);
        saveSuggestions();
        saveWordFrequency();
    }


    public static void saveSuggestions()
    {
        try
        {
            FileWriter fr= new FileWriter(new File("res/Suggestions.txt"));
            PriorityQueue<Levenshtein> Q=null;
            for(String key : Suggestable.getInstance().getMistakes())
            {

                try
                {

                    fr.write(Dictionary.getOnlyInstance().utfEncoding("0\t"+key+"\n"));

                    Q=Suggestable.getInstance().getSuggestions().get(Suggestable.getInstance().getMistakes().indexOf(key));
                    while(Q.size()!=0)
                    {

                        Levenshtein temp= Q.remove();
                        System.out.println(temp.getStrInfo().getWord());

                        fr.write(Dictionary.getOnlyInstance().utfEncoding("1\t"+temp.getStrInfo().getWord()+"\t"+temp.getStrInfo().getFrequency()+ "\t"+temp.getLevenshteinValue()+"\n"));
                    }
                }
                catch (IOException e)
                {
                    System.out.println("mara kenu khailam");
                }

            }
            fr.close();
        }
        catch (IOException e)
        {
            System.out.println("mara khasu");
        }
    }


    public static void saveWordFrequency()
    {
        int i=0;
        FileWriter fwr=null;
        try
        {
            fwr= new FileWriter("res/WordFrequency.txt");

            for(StringInfo strInfo: Dictionary.getOnlyInstance().getFrequencyList())
            {
                try
                {
                    if(i==0)
                    {
                        i=1;
                    }
                    else fwr.write("\n");
                    fwr.write(strInfo.getWord()+"\t"+strInfo.getFrequency());
                }
                catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
            fwr.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

}
