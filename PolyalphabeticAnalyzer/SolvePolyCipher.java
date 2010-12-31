import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class SolvePolyCipher 
{
	public static void main(String[] args) throws IOException
	{
		Scanner kbd = new Scanner (System.in);
		System.out.println("Enter the name of the file with the ciphertext input:");
		String inputFile = kbd.nextLine();
		File inFile = new File(inputFile);
		FileInputStream fis = new FileInputStream(inFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		StringBuilder message = new StringBuilder("");
		
		while (bis.available() > 0)
		{
			message.append((char)bis.read());
		}
		
		System.out.println("Enter the name of the file for the frequency output:");
		String freqOutName = kbd.nextLine();
		File freqOut = new File(freqOutName);
		
		System.out.println("Enter the name of the file for the plaintext output:");
		String plainOutName = kbd.nextLine();
		File decOut = new File(plainOutName);
		
		PolyAnalyzer pol = new PolyAnalyzer(message.toString(), freqOut, decOut);
		pol.decipher();
		
		fis.close();
		bis.close();
	}
}