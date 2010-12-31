import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

public class ModClient 
{
	public static void main(String[] args) throws IOException
	{
		String pathFromFile = "C:/Documents and Settings/HP_Administrator/Desktop/Rahul\'s Documents/11th Grade Documents/Eclipse Workspace/ModularEquation";
		File inFile = new File(pathFromFile, "modInput.txt");
		Scanner dataIn = new Scanner(inFile);
		String pathToFile = "C:/Documents and Settings/HP_Administrator/Desktop/Rahul\'s Documents/11th Grade Documents/Eclipse Workspace/ModularEquation";
		File outFile = new File(pathToFile, "modOutput.txt");
		PrintWriter dataOut = new PrintWriter(outFile);
		
		while (dataIn.hasNextInt())
		{
			EquationSolver solver = new EquationSolver(dataIn.nextInt(), dataIn.nextInt());
			dataOut.println(solver.solveEquation());
		}
		dataIn.close();
		dataOut.close();
	}
}