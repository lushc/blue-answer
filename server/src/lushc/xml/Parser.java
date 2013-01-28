package lushc.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import lushc.exceptions.XMLParsingException;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.Serializer;

/**
 * Utilises the XOM library to provide methods to parse XML
 * into a XOM document which can be traversed, or write a XOM document
 * to XML.
 * 
 * This class is an improved upon of the XML package found in 
 * original group project work in the CS2010 module.
 * 
 * @author Chris Lush
 */
public class Parser {

	public static Document readFromFile(File file) throws XMLParsingException {

		if (isValidFile(file)) {

			try {

				// Create the XOM Document object
				return new Builder().build(file);
			}
			catch (ParsingException ex) {

				throw new XMLParsingException("XML is malformed");
			}
			catch (IOException ex) {

				throw new XMLParsingException("File cannot be read");
			}
		}
		else {

			throw new XMLParsingException();
		}
	}

	public static void writeToFile(Document doc, File file) {

		FileOutputStream toFile = null;
		Serializer output = null;

		try {

			// Create a new FileOutputStream, specifying where to write
			toFile = new FileOutputStream(file);

			// Outputs the Document object to file
			output = new Serializer(toFile, "UTF-8");

			// Formatting options
			output.setIndent(4);
			output.setMaxLength(0);

			// Serializes the document onto the output stream
			output.write(doc);
		}
		catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		catch (IOException e) {

			e.printStackTrace();
		}
	}

	private static boolean isValidFile(File file) {

		// Accepted file formats
		String extensions[] = { "txt", "xml" };

		// The file's extension
		String ext = getExtension(file);

		if (ext != null) {

			for (int i = 0; i < extensions.length; i++) {

				// Match the extension against the array of valid ones
				if (ext.equals(extensions[i])) {

					return true;
				}
			}
		}

		return false;
	}

	private static String getExtension(File file) {

		String ext = "";
		String name = file.getName();
		int i = name.lastIndexOf('.');

		if (i > 0 && (i < name.length() - 1)) {

			// Generate the extension
			ext = name.substring(i + 1).toLowerCase();
		}

		return ext;
	}

}
