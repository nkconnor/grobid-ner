package org.grobid.trainer.sax;

import org.grobid.core.utilities.TextUtilities;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * SAX parser for Wikipedia corpus.
 *
 * @author Patrice Lopez
 * Deprecated: use IdilliaSemDocTextStaxHandler
 */
@Deprecated
public class TextSaxHandler extends DefaultHandler {

    private StringBuffer accumulator = new StringBuffer(); // Accumulate parsed text

	private List<List<String>> textVector = null;
	private List<String> currentVector = null;
	
	public TextSaxHandler() {
		textVector = new ArrayList<List<String>>();
	}
	
	public void characters(char[] buffer, int start, int length) {
        accumulator.append(buffer, start, length);
    }

    public String getText() {
        return accumulator.toString().trim();
    }
	
	/**
	 *  Return the tokenized text as vector for sequence tagging. Each section as given by the source 
	 *  document (e.g. <p>) is a distinct list of tokens, i.e. List<String>. 
	 */
	public List<List<String>> getTextVector() {
		return textVector;
	}
	
	public void endElement(String uri,
                           String localName,
                           String qName) throws SAXException {
        if (qName.equals("txt")) {
			String token = getText();
			token = token.replace("n't", " n't");
			StringTokenizer st = new StringTokenizer(token, TextUtilities.delimiters, true);
			List<String> currentTmpVector = new ArrayList<String>();			
			while (st.hasMoreTokens()) {	
				currentTmpVector.add(st.nextToken());
			}
			
			// basic re-tokenization for numbers to match Idilia tokenization
			//currentTmpVector = retokenize(currentTmpVector);
			
			// finally remove spaces 
			for(String tok : currentTmpVector) {
			 	if (!tok.equals(" ")) {
					if (currentVector == null)	{
						currentVector = new ArrayList<String>();
					}
					currentVector.add(tok);
				}
			}
			
			//textVector.add(currentVector);
			//currentVector = null;
		}
		else if (qName.equals("sent")) {
			textVector.add(currentVector);
			currentVector = null;
		}	
		accumulator.setLength(0);
	}
	
	
	public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts)
            throws SAXException {

        accumulator.setLength(0);

		/*if (qName.equals("p") || qName.equals("headline")) {
			currentVector = new ArrayList<String>();
		}*/
	}
	
	/** 
	 * Create some larger tokens: 
	 *  - for english numbers:
	 *      around 10,000 -> ["around", " ", "10", ",", "000"] -> ["around", " ", "10,000"] 
	 */ 
	/*static public List<String> retokenize(List<String> tokens) {
		List<String> result = new ArrayList<String>();
		for(int i=0; i<tokens.size(); i++) {
			String token = tokens.get(i);
			while (isNumeric(tokens.get(i))) {
				// look ahead
				if ( (i+1<tokens.size()) && ( (tokens.get(i+1).equals(",")) || (tokens.get(i+1).equals(".")) ) ) {
					if ( (i+2<tokens.size()) && (isNumeric(tokens.get(i+2))) ) {
						token = token+tokens.get(i+1)+tokens.get(i+2);
						i = i+2;
					}
					else 
						break;
				}
				else 
					break;
			}
			result.add(token);
		}
		return result;
	}*/
	
	/*static private boolean isNumeric(String str) {  
	  	try {  
	    	double d = Double.parseDouble(str);  
	  	}  
	  	catch(NumberFormatException nfe) {  
	    	return false;  
	  	}  
	  	return true;  
	}*/
	
	
}