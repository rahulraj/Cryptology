
public class LetterPair implements Comparable
{
	private int myRow; // its row in the 2D array, representing its first letter
	private int myCol; // its column in the 2D array, representing its second letter
	private int frequency; // number of the pair in the coded message
	private boolean listed; // whether or not this has been listed in the output already
	
	public LetterPair(int row, int col)
	{
		myRow = row;
		myCol = col;
		frequency = 0;
		listed = false;
	}
	
	public void incrementFreqency()
	{
		frequency++;
	}
	
	public int getFrequency()
	{
		return frequency;
	}
	
	public void setListed(boolean list)
	{
		listed = list;
	}
	
	public boolean isListed()
	{
		return listed;
	}
	
	public String digraphAsString()
	{
		return "" + (char)(myRow + 65) + (char)(myCol + 65);
	}
	
	public String toString()
	{
		String str = "";
		char first = (char)(myRow + 65);
		char second = (char)(myCol + 65);
		str += first + "" + second + ": " + frequency + "\n";
		return str;
	}
	
	public int compareTo(Object obj)
	{
		LetterPair other = (LetterPair)obj;
		return frequency - other.getFrequency();
	}
}
