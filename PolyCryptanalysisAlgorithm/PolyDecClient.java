import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class PolyDecClient 
{
	public static void main(String[] args) throws IOException
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the name of an input file.");
		String fileIn = in.next();
		File inFile = new File(fileIn);
		FileInputStream fis = new FileInputStream(inFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		StringBuilder message = new StringBuilder("");
		
		while (bis.available() > 0)
		{
			message.append((char)bis.read());
		}
		
		System.out.println("Enter the name of a file to output frequency data.");
		fileIn = in.next();
		File freqOut = new File(fileIn);
		
		System.out.println("Enter the name of a file to output the deciphered text.");
		fileIn = in.next();
		File decOut = new File(fileIn);
		
		PolyAnalyzer pol = new PolyAnalyzer(message.toString(), freqOut, decOut);
		pol.decipher();
		
		fis.close();
		bis.close();
	}
}