import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class EncipherPolyalphabetically 
{
	public static void main(String[] args) throws IOException
	{
		Scanner kbd = new Scanner(System.in);
		System.out.println("Enter the name of a file with plaintext input:");
		String inputFile = kbd.nextLine();
		File inFile = new File(inputFile);
		FileInputStream fis = new FileInputStream(inFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		System.out.println("Enter the name of a file for the ciphertext output:");
		String outputFile = kbd.nextLine();
		File outFile = new File(outputFile);
		PrintWriter dataOut = new PrintWriter(outFile);
		
		StringBuilder plainText = new StringBuilder("");
		
		while (bis.available() > 0)
		{
			char c = (char)(bis.read());
			plainText.append(c);
		}
		
		System.out.println("Enter the number of alphabets to use:");
		int numAlphs = kbd.nextInt();
		PolyEncipherer enc = new PolyEncipherer(plainText.toString(), numAlphs);
		dataOut.println(enc.encipher() + "\n\n" + enc.getKeys());
		
		fis.close();
		bis.close();
		dataOut.close();
	}
}