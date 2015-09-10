package supportive;

import supportive.parsers.btdparser.BtdParser;

public class CrChecker
{
	String crPath;
	
	public CrChecker(String crPath)
	{
		this.crPath = crPath;
		
		BtdParser btdParser = new BtdParser(crPath);
		btdParser.parse();
	}
	
	
}
