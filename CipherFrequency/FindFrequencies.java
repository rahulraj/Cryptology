import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class FindFrequencies 
{
	public static void main(String[] args) throws IOException
	{
		Scanner kbd = new Scanner(System.in);
		System.out.println("Give the name of the input file:");
		String inputFile = kbd.nextLine();
		File inFile = new File(inputFile);
		FileInputStream fis = new FileInputStream(inFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		System.out.println("Give the name of the output file:");
		String outputFile = kbd.nextLine();
		File outFile = new File(outputFile);
		PrintWriter dataOut = new PrintWriter(outFile);
		StringBuilder input = new StringBuilder("");
		char c;
		
		while (bis.available() > 0)
		{
			c = (char)bis.read();
			input.append(c);
		}
		
		LetterCounter counter = new LetterCounter(input.toString());
		dataOut.println(counter.getFrequencies());
		fis.close();
		bis.close();
		dataOut.close();
	}
}