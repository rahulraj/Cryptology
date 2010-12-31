
public class MatrixRow implements Comparable
{
	private int[] myNumbers;
	
	public MatrixRow(int[] nums)
	{
		myNumbers = nums;
	}
	
	public int[] getNumbers()
	{
		return myNumbers;
	}
	
	public int compareTo(Object obj)
	{
		MatrixRow other = (MatrixRow)obj;
		int[] otherNum = other.getNumbers();
		if (myNumbers.length != otherNum.length)
		{
			throw new IllegalStateException("Only rows of the same length should be compared!");
		}
		for (int loc = 0; loc < myNumbers.length; loc++)
		{
			if (myNumbers[loc] != otherNum[loc])
			{
				return myNumbers[loc] - otherNum[loc];
			}
		}
		return 0; // exactly the same
	}
}