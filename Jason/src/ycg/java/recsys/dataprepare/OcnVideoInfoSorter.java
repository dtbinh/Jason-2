package ycg.java.recsys.dataprepare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class OcnVideoInfoSorter {

	private static final String HOME = "E:\\Dataset\\Ocn";
	
	public void run() {
		try {
			sortByVideoID();
			sortByCreateTime();
			sortByContent();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void sortByVideoID() throws FileNotFoundException {
		File inputFile = new File(HOME + "\\video\\video_info.txt");
		HashMap<Integer, String> records = new HashMap<Integer, String>();
		Scanner scanner = new Scanner(inputFile);
		while (scanner.hasNext()) {
			String record = scanner.nextLine();
			int id = Integer.parseInt(record.substring(0, record.indexOf('|')));
			records.put(id, record);
		}
		scanner.close();
		Scanner scanner2 = new Scanner(new File(HOME + "\\video\\video_id.txt"));
		int N = scanner2.nextInt();
		scanner2.close();
		File outputFile = new File(HOME + "\\video\\video_sort_id.txt");
		PrintWriter pw = new PrintWriter(outputFile);
		for (int id = 0; id < N; ++id) {
			String record = records.get(id);
			if (record == null) {
				pw.print(id);pw.print('|');
				pw.print(0);pw.print('|');
				pw.print("missing");pw.print('|');
				pw.print("missing");pw.print('|');
				pw.println("missing");
			} else {
				pw.println(record);
			}
		}
		pw.close();
		
	}

	private void sortByCreateTime() throws FileNotFoundException {
		File inputFile = new File(HOME + "\\video\\video_info.txt");
		ArrayList<String> records = new ArrayList<String>();
		Scanner scanner = new Scanner(inputFile);
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			records.add(line);
		}
		Collections.sort(records, new Comparator<String>() {
			@Override
			public int compare(String record1, String record2) {
				Calendar t1 = parseTime(extractCreateTime(record1));
				Calendar t2 = parseTime(extractCreateTime(record2));
				return t1.compareTo(t2);
			}

			private Calendar parseTime(String text) {
				int year = Integer.parseInt(text.substring(0, 4));
				int month = Integer.parseInt(text.substring(5, 7));
				int date = Integer.parseInt(text.substring(8, 10));
				int hourOfDay = Integer.parseInt(text.substring(11, 13));
				int minute = Integer.parseInt(text.substring(14, 16));
				int second = Integer.parseInt(text.substring(17,19));
				Calendar time = Calendar.getInstance();
				time.set(year, month, date, hourOfDay, minute, second); 
				return time;
			}

			private String extractCreateTime(String record) {
				String[] fields = record.split("[|]");
				return fields[2];
			}
		});
		scanner.close();
		File outputFile = new File(HOME + "\\video\\video_sort_time.txt");
		PrintWriter pw = new PrintWriter(outputFile);
		for (String r : records) {
			pw.println(r);
		}
		pw.close();		
	}

	private void sortByContent() throws FileNotFoundException {
		File inputFile = new File(HOME + "\\video\\video_info.txt");
		ArrayList<String> records = new ArrayList<String>();
		Scanner scanner = new Scanner(inputFile);
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			records.add(line);
		}
		Collections.sort(records, new Comparator<String>() {
			@Override
			public int compare(String record1, String record2) {
				int pos1 = record1.lastIndexOf('|');
				int pos2 = record2.lastIndexOf('|');
				return record1.substring(pos1+1).compareTo(record2.substring(pos2+1));
			}
		});
		scanner.close();
		File outputFile = new File(HOME + "\\video\\video_sort_content.txt");
		PrintWriter pw = new PrintWriter(outputFile);
		for (String r : records) {
			pw.println(r);
		}
		pw.close();
	}
	
	public static void main(String[] args) {
		new OcnVideoInfoSorter().run();
	}
}
