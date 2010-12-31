
public class TrigraphRan implements Comparable
{
	private int myRow; // first letter
	private int myCol; // second letter
	private int myThird; // third letter
	private int frequency;
	private boolean listed;
	
	public TrigraphRan(int row, int col, int third)
	{
		myRow = row;
		myCol = col;
		myThird = third;
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
	
	public char getFirst()
	{
		return (char)(myRow + 65);
	}
	
	public char getSecond()
	{
		return (char)(myCol + 65);
	}
	
	public char getThird()
	{
		return (char)(myThird + 65);
	}
	
	public String getLetters()
	{
		return "" + getFirst() + getSecond() + getThird();
	}
	
	public void setListed(boolean list)
	{
		listed = list;
	}
	
	public boolean isListed()
	{
		return listed;
	}
	
	public int compareTo(Object obj)
	{
		TrigraphRan other = (TrigraphRan)obj;
		return frequency - other.getFrequency();
	}
	
	public String toString()
	{
		String str = "";
		char first = (char)(myRow + 65);
		char second = (char)(myCol + 65);
		char third = (char)(myThird + 65);
		str += first + "" + second + third + ": " + frequency + "\n";
		return str;
	}
}