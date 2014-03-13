package org.imc.ocnrecsys.cleaner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.Scanner;


public class SessionDataCleaner {
	
	public void run() {
		File path = new File(Config.HOME+"\\Session");
		String[] filenames = path.list(new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String name) {
				return name.startsWith("ow_rental_sitv.")
					&& name.endsWith(".txt");
			}
		});
		for (String filename : filenames) {
			File inputFile = new File(path.getAbsolutePath() + "\\" + filename);
			String outputFilename = "session" + filename.substring(filename.indexOf('.')+1);
			File outputFile = new File(Config.TMP_DIR + "\\session\\" + outputFilename);
			System.out.println("processing file:"+filename);
			processSessionFile(inputFile, outputFile);
		}
	}
	
	private void processSessionFile(File inputFile, File outputFile) {
		try {
			Scanner scanner = new Scanner(inputFile);
			PrintWriter printWriter = new PrintWriter(outputFile);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.startsWith("SQL>")) continue;
				String[] fileds = line.split("\\|");
				String STB_NO = fileds[0].trim();
				String ASSET_NAME = fileds[4].trim();
				String START_TIME = fileds[5].trim();
				String END_TIME = fileds[6].trim();
				String formatted = 
						  STB_NO + "," 
						+ ASSET_NAME + "," 
						+ START_TIME + "," 
						+ END_TIME;
				printWriter.println(formatted);
			}
			scanner.close();
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static void main(String[] args) {
		SessionDataCleaner sdc = new SessionDataCleaner();
		sdc.run();
	}

}
