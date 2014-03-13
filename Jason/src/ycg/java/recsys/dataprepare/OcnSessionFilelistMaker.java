package ycg.java.recsys.dataprepare;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import ycg.java.io.FileUtil;

public class OcnSessionFilelistMaker {
	
	private static final String HOME = "E:\\Dataset\\Ocn";
	
	public static void main(String[] args) throws FileNotFoundException {
		String rootDirectory = HOME + "\\raw\\session\\";
		List<String> filenames = FileUtil.getAllFiles(rootDirectory);
		PrintWriter writer = new PrintWriter(HOME + "\\raw\\session_filelist.txt");
		for (String filename : filenames) {
			writer.println(filename);
		}
		writer.close();
	}
	
}
