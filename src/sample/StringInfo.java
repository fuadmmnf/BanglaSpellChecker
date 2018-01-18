package sample;

public class StringInfo
{
    private  int frequency;
    private String word;

    public StringInfo(String s,int f)
    {
        word=s;
        frequency=f;
    }
    public int getFrequency() {
        return frequency;
    }

    public  void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }
}
