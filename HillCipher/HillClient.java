import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.PrintWriter;

public class HillClient 
{
	public static void main(String[] args) throws IOException 
	{
		// read the data about the enciphering matrix
		// this key is meant to be a word where each letter represents a number in the matrix
		// A=1, B=2, C=3 ... Z=26
		// NOTE: Some resources for Hill Cipher data will start with A=0 and go to Z=25
		// I am using the system described in Abraham Sinkov's Elementary Cryptanalysis: A Mathematical Approach
		File matrixData = new File("matrixData.txt");
		Scanner matrixIn = new Scanner(matrixData);
		String key = matrixIn.nextLine();
		matrixIn.close();
	

		// read the text to encipher
		File inFile = new File("textToEncipher.txt");
		FileInputStream fis = new FileInputStream(inFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		StringBuilder plain = new StringBuilder("");
		while (bis.available() > 0)
		{
			plain.append((char)bis.read());
		}
		
		// initialize the output file PrintWriter
		File outFile = new File("hillEncipherOutput.txt");
		PrintWriter dataOut = new PrintWriter(outFile);
		
		HillEncipherer hill = new HillEncipherer(key, plain.toString());
		dataOut.print(hill.encipher());
		
		fis.close();
		bis.close();
		dataOut.close();
	}
}