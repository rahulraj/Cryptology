
public class LetterPair 
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
	
	public void setFrequency(int freq)
	{
		frequency = freq;
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
	
	public int getRow()
	{
		return myRow;
	}
	
	public int getCol()
	{
		return myCol;
	}
	
	public String toString()
	{
		String str = "";
		char first = (char)(myRow + 97);
		char second = (char)(myCol + 97);
		str += first + "" + second + ": " + frequency + "\n";
		return str;
	}
}
