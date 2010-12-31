import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

public class EquationSolver 
{
	private int a;
	private int b;
	// equation to solve is ax = b(mod 26) where the = sign represents congruency
	
	public EquationSolver(int anA, int aB)
	{
		a = anA;
		b = aB;
	}
	
	public String solveEquation()
	{
		String output = "";
		int multiplier = 0;
		switch(a)
		{
		// find the inverses that cause the coefficient of x to equal 1
		case 1:
			multiplier = 1;
			break;
		case 3:
			multiplier = 9;
			break;
		case 5:
			multiplier = 21;
			break;
		case 7:
			multiplier = 15;
			break;
		case 9:
			multiplier = 3;
			break;
		case 11:
			multiplier = 19;
			break;
		case 15:
			multiplier = 7;
			break;
		case 17:
			multiplier = 23;
			break;
		case 19:
			multiplier = 11;
			break;
		case 25:
			multiplier = 25;
			break;
		default: // numbers not relatively prime with 26
			output = "Equation not solvable";
			break;
		}
		if (!output.equals("Equation not solvable"))
		{
			multiplyMod26(multiplier);
			output += b;
		}
		return output;
	}
	
	private void multiplyMod26(int multiplier)
	{
		// b is multiplied by multiplier mod 26
		b *= multiplier;
		while (b > 26)
		{
			b -= 26;
		}
		//return 0;
	}
	
	public static void main(String[] args) throws IOException
	{
		String pathFromFile = "C:/Documents and Settings/HP_Administrator/Desktop/Rahul\'s Documents/11th Grade Documents/Eclipse Workspace/ModularEquation";
		File inFile = new File(pathFromFile, "modInput.txt");
		Scanner dataIn = new Scanner(inFile);
		String pathToFile = "C:/Documents and Settings/HP_Administrator/Desktop/Rahul\'s Documents/11th Grade Documents/Eclipse Workspace/ModularEquation";
		File outFile = new File(pathToFile, "modOutput.txt");
		PrintWriter dataOut = new PrintWriter(outFile);
		/*
		 * Replace file path name and file name with the ones
		 * corresponding to the computer on which this code is running
		 * Alternatively, have dataIn read from System.in and use System.out
		 * to output results
		 */
		
		while (dataIn.hasNextInt())
		{
			EquationSolver solver = new EquationSolver(dataIn.nextInt(), dataIn.nextInt());
			dataOut.println(solver.solveEquation());
		}
		dataIn.close();
		dataOut.close();
	}
}