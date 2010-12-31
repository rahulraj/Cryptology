
public class Matrix implements Comparable
{
	private int[][] matNums;
	
	public Matrix(int[][] mNums)
	{
		matNums = mNums;
	}
	
	public int[][] getMatrix()
	{
		return matNums;
	}
	
	public int compareTo(Object obj)
	{
		Matrix other = (Matrix)obj;
		int[][] otherMat = other.getMatrix();
		if (matNums.length != otherMat.length || matNums[0].length != otherMat[0].length)
		{
			throw new IllegalStateException("Only matrices of the same dimensions should be compared!");
		}
		for (int row = 0; row < matNums.length; row++)
			for (int col = 0; col < matNums[0].length; col++)
			{
				if (matNums[row][col] != otherMat[row][col])
				{
					return matNums[row][col] - otherMat[row][col];
				}
			}
		return 0; // the same
	}

}
