package ycg.java.recsys.dataprepare;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class OcnVideoInfoMaker {
	
	private static final String HOME = "E:\\Dataset\\Ocn";
	
	public void run() {
		try {
			List<String> catalogFilenames = loadCatalogFilenames();
			HashMap<String, Integer> assetIDMap = loadAssetToVideoIDMapping();
			PrintWriter pw1 = new PrintWriter(HOME + "\\video\\video_info.txt");
			PrintWriter pw2 = new PrintWriter(HOME + "\\video\\video_nosession.txt");
			for (String filename : catalogFilenames) {
				System.out.println("processing file:" + filename);
				processCatalogFile(filename, assetIDMap, pw1, pw2);
			}
			pw1.close();
			pw2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private List<String> loadCatalogFilenames() throws FileNotFoundException {
		List<String> filenames = new ArrayList<String>();
		Scanner scanner = new Scanner(new File(HOME + "\\raw\\catalog_filelist.txt"));
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			filenames.add(line);
		}
		scanner.close();
		return filenames;
	}
	
    private HashMap<String, Integer> loadAssetToVideoIDMapping() throws FileNotFoundException {
    	HashMap<String, Integer> mapping = new HashMap<String, Integer>();
    	Scanner scanner = new Scanner(new File(HOME + "\\video\\video_id.txt"));
    	int numVideo = scanner.nextInt();
    	for (int i = 0; i < numVideo; ++i) {
    		int id = scanner.nextInt();
    		String asset = scanner.next();
    		int pos = asset.lastIndexOf('.');
    		if (pos > 0) { 
    			asset = asset.substring(0, pos);
    		}
    		mapping.put(asset, id);
    	}
    	scanner.close();
    	return mapping;
	}

    private void processCatalogFile(String filename, HashMap<String, Integer> assetIDMap, 
    			PrintWriter pw1, PrintWriter pw2) {
    	try {
    		SAXReader saxReader = new SAXReader();
    		Document document = saxReader.read(new File(filename));
    		Element root = document.getRootElement();
    		Element offering = root.element("offering");
    		Element description = offering.element("description");
    		Element movie= offering.element("movie");
          
    		String assetName = movie.attributeValue("assetName");
    		String runTime = movie.attributeValue("runTime");
    		String createTime = offering.attributeValue("createTime");
    		String expirationDate = offering.attributeValue("expirationDate");
    		String content = description.getText();
    		int pos = assetName.lastIndexOf(':');
    		if (pos > 0) {
    			assetName = assetName.substring(pos + 1);
    		}
    		Integer videoID = assetIDMap.get(assetName);
    		if (videoID == null) {
    			String infoLine = assetName + "|" 
    					+ runTime + "|"
    					+ createTime + "|"
    					+ expirationDate + "|"
    					+ content;
    			pw2.println(infoLine);
    			System.out.println(infoLine);
    		} else {
    			String infoLine = videoID + "|" 
    					+ runTime + "|"
    					+ createTime + "|"
    					+ expirationDate + "|"
    					+ content;
    			pw1.println(infoLine);
    			System.out.println(infoLine);
    		}
    	} catch (DocumentException e) {
			e.printStackTrace();
    	} 
    }
    
	public static void main(String[] args) {
		new OcnVideoInfoMaker().run();
	}

}
