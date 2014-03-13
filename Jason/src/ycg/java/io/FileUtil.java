package ycg.java.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	
	public static List<String> getAllFiles(String rootDirectory) {
		List<String> filenames = new ArrayList<String>();
		collectAllFiles(rootDirectory, filenames);
		return filenames;
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

}
