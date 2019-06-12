package com.dp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class DataWriter {	
	public static void writeFileFromList(String listname, String filename) throws FileNotFoundException, IOException {
		Scanner listScanner = new Scanner(new FileInputStream(listname));
		ArrayList<String> files = new ArrayList<>();
		while (listScanner.hasNextLine()) {
			files.add(listScanner.nextLine());
		}
		listScanner.close();
		String listOfFiles[] = new String[files.size()];
		for(int i = 0; i < files.size(); i++) {
			listOfFiles[i] = files.get(i);
		}
		writeFile(listOfFiles, filename);
	}
	
	public static void writeFile(String[] list, String filename) throws FileNotFoundException, IOException {
		ArrayList<InputStream> files = new ArrayList<>();
		for (int i = 0; i < list.length; i++) {
			files.add(new FileInputStream(list[i]));
		}
		writeFile(files, list, filename);
	}
	
	public static void writeFile(ArrayList<InputStream> files, String[] names, String filename) throws FileNotFoundException, IOException {
		int num = names.length;
		PrintStream data = new PrintStream(filename);
		data.println(DataInfo.VERSION);
		data.println(num);
		for (int i = 0; i < num; i++) {
			data.println(names[i]);
		}
		StringBuilder meta = new StringBuilder();
		for (int i = 0; i < num; i++) {
			meta.append(files.get(i).available() + " ");
		}
		data.println(meta);
		for (int i = 0; i < num; i++) {
			byte datafile[] = new byte[DataInfo.BUFFER_SIZE];
			while (files.get(i).available() >= DataInfo.BUFFER_SIZE) {
				files.get(i).read(datafile);
				data.write(datafile);
			}
			if (files.get(i).available() > 0) {
				datafile = new byte[files.get(i).available()];
				files.get(i).read(datafile);
				data.write(datafile);
			}
		}
		data.close();
	}
}
