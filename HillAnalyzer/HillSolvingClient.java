import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.PrintWriter;

public class HillSolvingClient 
{
	public static void main(String[] args) throws IOException 
	{
		File inFile = new File("hillSolveInput.txt");
		FileInputStream fis = new FileInputStream(inFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		StringBuilder cip = new StringBuilder("");
		
		while (bis.available() > 0)
		{
			cip.append((char)(bis.read()));
		}
		
		File outFile = new File("hillSolveOutput.txt");
		PrintWriter dataOut = new PrintWriter(outFile);
		
		HillSolver sol = new HillSolver(cip.toString(), dataOut);
		
		sol.solveTwoByTwo(); // closes dataOut in the process
		
		fis.close();
		bis.close();
		
		/*HillSolver sol = new HillSolver();
		int[][] toInv = {{3, - 5},
						 {11, 13}};
		double[][] inv = sol.invertMatrix(toInv);
		for (int row = 0; row < inv.length; row++)
			for (int col = 0; col < inv.length; col++)
			{
				System.out.println(inv[row][col]);
			}
		 */
	}

}
