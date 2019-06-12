package com.dp;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class DataReader {
	public static InputStream readFile(String filename, String datafile) throws FileNotFoundException, IOException {
		int number = DataInfo.getNumber(datafile);
		String list[] = DataInfo.getList(datafile);
		long sizes[] = DataInfo.getSizes(datafile);
		int length = 0;
		for (int i = 0; i < number; i++) {
			length += list[i].getBytes().length + DataInfo.NEW_LINE.getBytes().length;
			length += new String("" + sizes[i]).getBytes().length + DataInfo.SPACE.getBytes().length;
		}
		length += DataInfo.NEW_LINE.getBytes().length;
		int numoffile = -1;
		for (int i = 0; i < list.length; i++) {
			if (filename.equals(list[i])) {
				numoffile = i;
				break;
			}
		}
		int num = 0;
		while (num < numoffile) {
			length += sizes[num];
			++num;
		}
		InputStream data = new FileInputStream(datafile);
		data.skip(DataInfo.VERSION.getBytes().length + 2 * DataInfo.NEW_LINE.getBytes().length + new String("" + number).getBytes().length + length);
		long size = sizes[numoffile];
		long maxsize = 1;
		Runtime runtime = Runtime.getRuntime();
		while (maxsize * 2 < runtime.maxMemory() - (runtime.totalMemory() - runtime.freeMemory())) {
			maxsize *= 2;
		}
		if (size > maxsize) {
			byte file[] = new byte[DataInfo.BUFFER_SIZE];
			PrintStream tmp = new PrintStream(filename + ".dpacktmp");
			while (size >= DataInfo.BUFFER_SIZE) {
				data.read(file);
				tmp.write(file);
				size -= DataInfo.BUFFER_SIZE;
			}
			if (size > 0) {
				file = new byte[(int)size];
				data.read(file);
				tmp.write(file);
			}
			data.close();
			tmp.close();
			return new FileInputStream(filename + ".dpacktmp");
		}
		byte file[] = new byte[(int)size];
		data.read(file);
		data.close();
		return new ByteArrayInputStream(file);
	}
}
