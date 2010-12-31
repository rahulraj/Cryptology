/*
 * Class to compare the monoalphabets and use their data to solve for less common letters
 */

public class MonoalphabetComparer 
{
	private MonoalphabetAnalyzer[] analyzers;
	
	public MonoalphabetComparer(MonoalphabetAnalyzer[] monos)
	{
		analyzers = monos;
	}
	
	public String firstGuess()
	{
		for (MonoalphabetAnalyzer mon : analyzers)
		{
			mon.makeFirstGuess();
		}
		return getDeciphered();
	}
	
	public void analyzeWithDepth()
	{
		// use knowledge of some of the more common letters (solved locally) to solve the less common ones
		// NOTE: This uses data collected from the Shakespeare plays!
		// Data from Abraham Sinkov's Elementary Cryptanalysis: A Mathematical Approach is also used
		// But it is the secondary source to the Shakespeare plays.
		// It will be necessary to do some modifications for ciphertexts different from Shakespeare!
		for (MonoalphabetAnalyzer mono : analyzers)
		{
			mono.solveFreqs(); // the most common letters are solved locally
			// these are E, T, O, and A
		}
		
		DigraphAnalyzer[] diAnalyze = new DigraphAnalyzer[analyzers.length];
		for (int loc = 0; loc < (analyzers.length - 1); loc++)
		{
			diAnalyze[loc] = new DigraphAnalyzer(analyzers[loc], analyzers[loc + 1], false);
		}
		diAnalyze[diAnalyze.length - 1] = new DigraphAnalyzer(analyzers[analyzers.length - 1],
															  analyzers[0], true);
		
		TrigraphAnalyzer[] triAnalyze = new TrigraphAnalyzer[analyzers.length];
		if (analyzers.length == 2)
		{
			// only two alphabets
			triAnalyze[0] = new TrigraphAnalyzer(analyzers[0], analyzers[1], analyzers[0], 2);
			triAnalyze[1] = new TrigraphAnalyzer(analyzers[1], analyzers[0], analyzers[1], 2);
		}
		for (int loc = 0; loc < (analyzers.length - 2); loc++)
		{
			triAnalyze[loc] = new TrigraphAnalyzer(analyzers[loc], analyzers[loc + 1], analyzers[loc + 2], -1);
		}
		triAnalyze[triAnalyze.length - 2] = new TrigraphAnalyzer(analyzers[analyzers.length - 2],
											analyzers[analyzers.length - 1], analyzers[0], 2);
		triAnalyze[triAnalyze.length - 1] = new TrigraphAnalyzer(analyzers[analyzers.length - 1],
											analyzers[0], analyzers[1], 1);
		
		for (DigraphAnalyzer di : diAnalyze)
			di.solveH();
		for (DigraphAnalyzer di : diAnalyze)
			di.solveN();
		for (DigraphAnalyzer di : diAnalyze)
			di.solveR();
		for (DigraphAnalyzer di : diAnalyze)
			di.solveU();
			
		for (TrigraphAnalyzer tri : triAnalyze)
			tri.solveD();
		for (TrigraphAnalyzer tri : triAnalyze)
			tri.solveI();
		for (TrigraphAnalyzer tri : triAnalyze)
			tri.solveY();
		for (TrigraphAnalyzer tri : triAnalyze)
			tri.solveG();
		
		for (DigraphAnalyzer di : diAnalyze)
			di.solveS(); // solveS() requires that "I" be solved
		for (DigraphAnalyzer di : diAnalyze)
			di.solveQ();
		for (DigraphAnalyzer di : diAnalyze)
			di.solveM();
		
		// update the plaintext strings
		for (MonoalphabetAnalyzer m : analyzers)
		{
			m.makePlaintext();
		}
	}
	
	public String getDeciphered()
	{
		StringBuilder pl = new StringBuilder("");
		int alphCount = 0;
		int wordCount = 0;
		for (int anIndex = 0; anIndex < analyzers[0].getLength(); anIndex++)
		{
			for (MonoalphabetAnalyzer mono : analyzers)
			{
				pl.append(mono.getPlainLetterAt(anIndex));
				alphCount++;
				if (alphCount == 5)
				{
					alphCount = 0;
					wordCount++;
					pl.append(' '); // for readability
				}
				if (wordCount == 10)
				{
					wordCount = 0;
					pl.append("\n"); // also for readability
				}
			}
		}
		pl.trimToSize(); // delete whitespace
		
		pl.append("\n\n");
		for (MonoalphabetAnalyzer mono : analyzers)
		{
			pl.append("Keys for: " + mono.toString() + "\n");
			pl.append(mono.getKeys() + "\n");
		}
		return pl.toString();
	}
}