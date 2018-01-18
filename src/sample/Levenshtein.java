
package sample;

import javax.print.DocFlavor;

public class Levenshtein
{
    private  StringInfo strInfo;
    private  int levenshteinValue;

    public Levenshtein(StringInfo s, int l)
    {
        this.strInfo=s;
        this.levenshteinValue=l;
    }

    public  StringInfo getStrInfo() {
        return strInfo;
    }

    public  int getLevenshteinValue() {
        return levenshteinValue;
    }

    @Override
    public String toString() {
        return strInfo.getWord();
    }
}
