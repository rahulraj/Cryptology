
public class Trigraph 
{
	private int myRow; // first letter
	private int myCol; // second letter
	private int myThird; // third letter
	private int frequency;
	private boolean listed;
	
	public Trigraph(int row, int col, int third)
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
	
	public void setListed(boolean list)
	{
		listed = list;
	}
	
	public boolean isListed()
	{
		return listed;
	}
	
	public String toString()
	{
		String str = "";
		char first = (char)(myRow + 97);
		char second = (char)(myCol + 97);
		char third = (char)(myThird + 97);
		str += first + "" + second + third + ": " + frequency + "\n";
		return str;
	}
	

}
