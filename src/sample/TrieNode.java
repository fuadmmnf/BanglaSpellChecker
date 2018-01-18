package sample;

public  class TrieNode
{
    public static final int ALPHABET_NUMBER=127;
    TrieNode [] children = new TrieNode[ALPHABET_NUMBER];

    boolean isEndOfWord;

    public TrieNode()
    {
        isEndOfWord=false;
        for(int i=0;i<ALPHABET_NUMBER;i++)
            children[i]=null;
    }
}


