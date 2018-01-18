package sample;

import java.io.*;
import java.util.*;

public class Dictionary
{
    public static TrieNode root= new TrieNode();
    private static List<String> wordsList= new ArrayList<>();
    private static List<StringInfo> frequencyList=new ArrayList<>();
    private final String starter="ঀ";
    private static final Dictionary onlyInstance= new Dictionary();
    private  HashMap<String, String> phonetics = new HashMap<String, String>();
    private final String hoshonto ="্";

    public String getHoshonto() {
        return hoshonto;
    }

    public static Dictionary getOnlyInstance() {
        return onlyInstance;
    }


    public   List<StringInfo> getFrequencyList() {
        return frequencyList;
    }


    public HashMap<String, String> getPhonetics() {
        return phonetics;
    }



    private Dictionary()
    {
        try
        {
            String s="";
            File f= new File("res/Phonetics.txt");
            FileReader fr= new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);

            try
            {
                while((s=bfr.readLine())!=null)
                {

                    String words[] = s.split("\\s");
                    phonetics.put(words[0],words[1]);
                }
            }
            catch (NullPointerException e)
            {
                System.out.println("MAra khailam");
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }


        try
        {
            if(new File("res/WordFrequency.txt").exists())
            {
                File f= new File("res/WordFrequency.txt");
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);

                try
                {
                    String s="  ";
                    while ((s = bfr.readLine()) != null)
                    {
                        String [] wordFreq= s.split("\\s");
                        wordFreq[0]=utfEncoding(wordFreq[0]);
                        insertWord(wordFreq[0]);
                        try {
                            wordsList.add(wordFreq[0]);
                            frequencyList.add(new StringInfo(wordFreq[0], Integer.parseInt(wordFreq[1])));
                        }
                        catch (NullPointerException e)
                        {
                            System.out.println(e.getStackTrace());
                        }
                    }
                    bfr.close();
                    fr.close();
                }
                catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else
            {
                File f= new File("res/Dictionary.txt");
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);

                try
                {
                    String s="  ";
                    while ((s = bfr.readLine()) != null)
                    {
                        s=utfEncoding(s);
                        insertWord(s);
                        try {
                            wordsList.add(s);
                            frequencyList.add(new StringInfo(s, 0));
                        }
                        catch (NullPointerException e)
                        {
                            System.out.println(e.getStackTrace());
                        }
                    }
                    bfr.close();
                    fr.close();
                }
                catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }


    public void insertWord(String key)
    {
        int length = key.length();
        int index;

        TrieNode current = root;

        for (int level = 0; level < length; level++)
        {
            index = (int)key.charAt(level) -(int) starter.charAt(0);
            if (current.children[index] == null)
                current.children[index] = new TrieNode();

            current = current.children[index];
        }


        current.isEndOfWord = true;
    }

    public boolean searchWord(String key)
    {
        int length = key.length();
        int index;

        TrieNode current = root;


        for (int level = 0; level < length; level++)
        {
            index = (int)key.charAt(level) -(int)starter.charAt(0);
            if (current.children[index] == null)
               return false;

            current = current.children[index];
        }

        // mark last node as leaf
        if(current.isEndOfWord && current!=null) return true;
        return false;
    }


    public void printWords()
    {
        track(root,"");
    }

    public void track(TrieNode current,String word)
    {
        if(current!=null)
        {
            if(current.isEndOfWord) System.out.println(word);
            for(int i=0;i<127;i++)
            {
                track(current.children[i],word+(char)((int)starter.charAt(0)+i));
            }
        }
    }

    public void updateFrequency(String word)
    {
        int index= wordsList.indexOf(word);
        StringInfo old = frequencyList.get(index);
        frequencyList.remove(index);
        frequencyList.add(index,new StringInfo(word,old.getFrequency()+1));
    }


    public void loadSuggestions()
    {
        try
        {
            String line,s=null;
            FileReader fr= new FileReader(new File("res/Suggestions.txt"));
            BufferedReader bfr = new BufferedReader(fr);
            PriorityQueue<Levenshtein> suggestions=null;
            int i=0;
            while((line=bfr.readLine())!=null)
            {
                line= utfEncoding(line);
                String[] lineArr = line.split("\t");
                if(lineArr[0].equals("0"))
                {

                    s=lineArr[1];

                    if(i==1)
                    {
                        Suggestable.getInstance().setSuggestions(suggestions);
                    }
                    else i=1;


                    suggestions= new PriorityQueue<>(new Comparator<Levenshtein>() {
                        public int compare(Levenshtein o1, Levenshtein o2) {
                            if (o1.getLevenshteinValue() == o2.getLevenshteinValue()) {
                                return o1.getStrInfo().getFrequency() - o2.getStrInfo().getFrequency();
                            }
                            return o1.getLevenshteinValue() - o2.getLevenshteinValue();
                        }
                    });

                }
                else
                {
                    suggestions.add(new Levenshtein(new StringInfo(lineArr[1],Integer.parseInt(lineArr[2])),Integer.parseInt(lineArr[3])));
                }
            }
            bfr.close();
            fr.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }



    public String utfEncoding(String str)
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

