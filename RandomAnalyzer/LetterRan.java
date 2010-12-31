
public class LetterRan implements Comparable 
{
	private char myLetter;
	private int frequency;
	
	public LetterRan(char let)
	{
		this(let, 0);
	}
	
	public LetterRan(char let, int freq)
	{
		myLetter = let;
		frequency = freq;
	}
	
	public void incrementFreq()
	{
		frequency++;
	}
	
	public void setFrequency(int newFreq)
	{
		frequency = newFreq;
	}
	
	public char getLetter()
	{
		return myLetter;
	}
	
	public int getFrequency()
	{
		return frequency;
	}

	public int compareTo(Object obj)
	{
		LetterRan other = (LetterRan)obj;
		return frequency - other.getFrequency();
	}
	
	public String toString()
	{
		return "" + myLetter + ":\t" + frequency;
	}
}