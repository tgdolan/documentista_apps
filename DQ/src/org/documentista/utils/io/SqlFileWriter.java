package org.documentista.utils.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

public class SqlFileWriter{
	
	private URI fileURI;
	private FileWriter fileWriter;
	
	
	public void writeLine(String line ) {}

	public SqlFileWriter(URI fileURI) throws IOException {
		fileURI = fileURI;
		File sqlFile = new File(fileURI);
		fileWriter = new FileWriter(sqlFile);		
	}
	
	public void writeToFile(String fileContents) throws IOException {
		
		fileWriter.write(fileContents.toCharArray());
	}

}
