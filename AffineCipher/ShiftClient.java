import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

public class ShiftClient 
{
	public static void main(String[] args) throws IOException
	{
		String pathFromFile = "C:/Documents and Settings/HP_Administrator/Desktop/Rahul\'s Documents/11th Grade Documents/Eclipse Workspace/AffineCipher";
		File inFile = new File(pathFromFile, "shiftInput.txt");
		//Scanner dataIn = new Scanner(inFile);
		//String pathToFile = "C:/Documents and Settings/HP_Administrator/Desktop/Rahul\'s Documents/11th Grade Documents/Eclipse Workspace/AffineCipher";
		File outFile = new File(pathFromFile, "shiftOutput.txt");
		PrintWriter dataOut = new PrintWriter(outFile);
		
		Scanner dataIn = new Scanner(System.in);
		
			// reads a, b, uncoded message (in that order)
		int mult = dataIn.nextInt();
		int aShift = dataIn.nextInt();
		String toShift = dataIn.nextLine();
		LetterShifter shift = new LetterShifter(mult, aShift, toShift);
		System.out.println(shift.shiftMessage());
		//dataIn.close();
	}
}
