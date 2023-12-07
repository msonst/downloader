package com.cs.download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ResourceExtractor {
	public static void printFileContent(InputStream is) throws IOException {
		try (InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr);) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			is.close();
		}
	}

	public static List<String> fileContentAsList(final String fileName) throws IOException {
		
		List<String> retVal = new  ArrayList<String>();
		
		InputStream is = getFileAsIOStream(fileName);
		
		try (InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr);) {
			String line;
			while ((line = br.readLine()) != null) {
				retVal.add(line);
			}
			is.close();
		}
		
		return retVal;
	}

	public static InputStream getFileAsIOStream(final String fileName) {
		InputStream ioStream = ResourceExtractor.class.getClassLoader().getResourceAsStream(fileName);

		if (ioStream == null) {
			throw new IllegalArgumentException(fileName + " is not found");
		}
		return ioStream;
	}

	private static void copy(InputStream source, OutputStream target) throws IOException {
		byte[] buf = new byte[8192];
		int length;
		while ((length = source.read(buf)) != -1) {
			target.write(buf, 0, length);
		}
	}

	public static void extract(String ressourcePath, String outPath) {

		ResourceList.getResources(Pattern.compile(".*")).forEach(s -> {
			System.out.println(s);
			InputStream in = null;
			try {
				Path sp = Path.of(s);
				Path fileName = sp.getFileName();

				Path rsp = Path.of("./").toAbsolutePath().relativize(sp);
				Path rtp = Path.of(outPath, rsp.toString());

				System.out.println(sp);
				System.out.println(rsp);
				System.out.println(rtp);

				rtp.getParent().toFile().mkdirs();

//				in = RessourceExtractor.class.getClassLoader().getResourceAsStream(rsp.toString());
//				copy(in, new FileOutputStream(rtp.toFile()));

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	public static void main(final String[] args) {
		ResourceExtractor.extract("src/main/ressources", "./test");
	}
}
