/*
 * Class to format the text samples in the format of five-letter blocks, twenty blocks to a line
 * All letters are uppercase
 * Also, all non-letter elements are removed
 * This is the format of deciphered text outputted by the analyzer programs
 * This can be used for testing the deciphered text's accuracy with the correct answer
 */

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TesterClient 
{
	public static void main(String[] args) throws IOException
	{
		// format the "correct" answer
		File inFile = new File("formatInput.txt");
		File outFile = new File("formatOutput.txt");
		
		TextFormatter tf = new TextFormatter(inFile, outFile);
		tf.formatText(); // closes files in process
		
		StringBuilder testData = new StringBuilder("");
		for (int numAlphs = 2; numAlphs <= 26; numAlphs++)
		{
			System.out.println("Testing with: " + numAlphs + " alphabets");
			// get the output of the deciphering program after enciphering the message
			inFile = new File("formatInput.txt"); // reopen this file
			StringBuilder toEnc = new StringBuilder("");
			FileInputStream aFis = new FileInputStream(inFile);
			BufferedInputStream aBis = new BufferedInputStream(aFis);
			while (aBis.available() > 0)
			{
				toEnc.append((char)(aBis.read()));
			}
			File toTest = new File("toTest.txt");
			PolyEncipherer poly = new PolyEncipherer(toEnc.toString(), numAlphs);
			String encd = poly.encipher();
			PolyAnalyzer polAnalyze = new PolyAnalyzer(encd, null, toTest, numAlphs);
			polAnalyze.automatedDecipher();
			
			// compare the correct answer and the deciphered text
			outFile = new File("formatOutput.txt"); // reopens file
			FileInputStream fis = new FileInputStream(outFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			StringBuilder correct = new StringBuilder("");
			while (bis.available() > 0)
			{
				char in = (char)(bis.read());
				if (in >= 65 && in <= 90)
				{
					correct.append(in);
				}
			}
			

			DecipheredTester dt = new DecipheredTester(correct, toTest, numAlphs);
			testData.append(dt.testData());
		}
		File results = new File("testResults.txt");
		PrintWriter resultOut = new PrintWriter(results);
		resultOut.println(testData);
		resultOut.close();
		
		// done signal
		JFrame fr = new JFrame("Done!");
		fr.setLocation(300, 300);
		fr.setSize(300, 300);
		fr.setAlwaysOnTop(true);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setVisible(true);
	}
}