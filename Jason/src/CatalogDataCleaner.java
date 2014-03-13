package org.imc.ocnrecsys.cleaner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class CatalogDataCleaner {
	
	public void run() {
		List<String> filenameCollector = new ArrayList<String>();
		collectAllFiles(Config.HOME + "\\Catalog", filenameCollector);
		try {
			File videoFile = new File(Config.TMP_DIR + "\\catalog\\video.txt");
			PrintWriter printWriter = new PrintWriter(videoFile);
			for (String filename : filenameCollector) {
				System.out.println("processing file:" + filename);
				File inputFile = new File(filename);
				processCatalogFile(inputFile, printWriter);
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
    private static void collectAllFiles(String root, List<String> filenames) { 
        File dir = new File(root); 
        File[] files = dir.listFiles(); 
        if (files == null) { 
            return;
        }
        for (int i = 0; i < files.length; i++) { 
            if (files[i].isDirectory()) { 
                collectAllFiles(files[i].getAbsolutePath(), filenames); 
            } else { 
				filenames.add(files[i].getAbsolutePath());                    
            }
        } 
    }

    private void processCatalogFile(File inputFile, PrintWriter pw) {
    	try {
    		SAXReader saxReader = new SAXReader();
    		Document document = saxReader.read(inputFile);
    		Element root = document.getRootElement();
    		Element offering = root.element("offering");
    		Element description = offering.element("description");
    		Element movie= offering.element("movie");
          
    		String assetName = movie.attributeValue("assetName");
    		String runTime = movie.attributeValue("runTime");
    		String createTime = offering.attributeValue("createTime");
    		String expirationDate = offering.attributeValue("expirationDate");
    		String content = description.getText();
    		String infoLine = assetName + "," 
        		  		  + runTime + ","
        		  		  + createTime + ","
        		  		  + expirationDate + ","
        		  		  + content;
    		pw.println(infoLine);
    	} catch (DocumentException e) {
			e.printStackTrace();
			File brokenFile = new File(Config.TMP_DIR + inputFile.getName());
			try {
				Scanner scanner = new Scanner(inputFile);
				PrintWriter writer = new PrintWriter(brokenFile);
				while (scanner.hasNext()) {
					writer.println(scanner.nextLine());
				}
				scanner.close();
				writer.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
    }
    
	public static void main(String[] args) {
		CatalogDataCleaner cdc = new CatalogDataCleaner();
		cdc.run();
	}

}
