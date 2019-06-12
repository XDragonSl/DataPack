package com.dp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DataInfo {
	
	protected static final String VERSION = "003";
	protected static final int BUFFER_SIZE = 4096;
	protected static final String NEW_LINE = "\r\n";
	protected static final String SPACE = " ";
	
	public static int getVersion(String filename) throws FileNotFoundException, IOException {
		InputStream data = new FileInputStream(filename);
		byte vers[] = new byte[VERSION.getBytes().length];
		data.read(vers);
		data.close();
		return Integer.parseInt(new String(vers));
	}
	
	public static int getNumber(String filename) throws FileNotFoundException, IOException {
		InputStream data = new FileInputStream(filename);
		data.skip(VERSION.getBytes().length + NEW_LINE.getBytes().length);
		ArrayList<Byte> datnum = new ArrayList<>();
		datnum.add((byte)data.read());
		while (datnum.get(datnum.size() - 1) != NEW_LINE.getBytes()[0]) {
			datnum.add((byte)data.read());
		}
		data.close();
		byte number[] = new byte[datnum.size() - 1];
		for (int i = 0; i < number.length; i++) {
			number[i] = datnum.get(i);
		}
		return Integer.parseInt(new String(number));
	}
	
	public static String[] getList(String filename) throws FileNotFoundException, IOException  {
		int number = getNumber(filename);
		InputStream data = new FileInputStream(filename);
		data.skip(VERSION.getBytes().length + 2 * NEW_LINE.getBytes().length + new String("" + number).getBytes().length);
		String list[] = new String[number];
		ArrayList<Byte> datlist = new ArrayList<>();
		for (int i = 0; i < list.length; i++) {
			datlist.clear();
			datlist.add((byte)data.read());
			while (datlist.get(datlist.size() - 1) != NEW_LINE.getBytes()[0]) {
				datlist.add((byte)data.read());
			}
			byte blist[] = new byte[datlist.size() - 1];
			for (int j = 0; j < blist.length; j++) {
				blist[j] = datlist.get(j);
			}
			list[i] = new String(blist);
			data.skip(NEW_LINE.getBytes().length - 1);
		}
		data.close();
		return list;
	}
	
	public static long[] getSizes(String filename) throws FileNotFoundException, IOException {
		int number = getNumber(filename);
		String list[] = getList(filename);
		int length = 0;
		for (int i = 0; i < list.length; i++) {
			length += list[i].getBytes().length + 2;
		}
		InputStream data = new FileInputStream(filename);
		data.skip(VERSION.getBytes().length + 2 * NEW_LINE.getBytes().length + new String("" + number).getBytes().length + length);
		long sizes[] = new long[number];
		ArrayList<Byte> datsizes = new ArrayList<>();
		for (int i = 0; i < sizes.length; i++) {
			datsizes.clear();
			datsizes.add((byte)data.read());
			while (datsizes.get(datsizes.size() - 1) != SPACE.getBytes()[0]) {
				datsizes.add((byte)data.read());
			}
			byte bsizes[] = new byte[datsizes.size() - 1];
			for (int j = 0; j < bsizes.length; j++) {
				bsizes[j] = datsizes.get(j);
			}
			sizes[i] = Long.parseLong(new String(bsizes));
		}
		data.close();
		return sizes;
	}
}
