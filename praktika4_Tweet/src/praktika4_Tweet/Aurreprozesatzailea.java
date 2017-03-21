package praktika4_Tweet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import weka.filters.unsupervised.attribute.*;

import weka.filters.Filter;

public class Aurreprozesatzailea {
	
	static StringToWordVector bektor = new StringToWordVector();
	
	public static void main(String[] args) throws Exception {
		for (int kont =0; kont<args.length; kont++){	
			FileReader fi = new FileReader(args[kont]);
			BufferedReader br = new BufferedReader(fi);
			
			FileWriter fw = new FileWriter(args[++kont]);
			BufferedWriter bw = new BufferedWriter(fw);
			Aurreprozesatzailea.arffIdatzi(bw, Aurreprozesatzailea.datuakIrakurri(br));
		}
	}

	
	private static ArrayList<String[]> datuakIrakurri(BufferedReader br) throws Exception {
		ArrayList<String[]> arraya = new ArrayList<>();
		String s;
		br.readLine();
		while ((s = br.readLine()) != null) {
			arraya.add(s.toLowerCase().trim().split("\",\""));
		}
		br.close();
		for (int i = 0; i < arraya.size(); i++) {
			String[] lista = arraya.get(i);
			for (int j = 0; j < lista.length; j++) {
				lista[j] = lista[j].replaceAll("[^A-Za-z]", "");
			}
			arraya.set(i, lista);
		}
		return arraya;
	}
		
	
	private static void arffIdatzi(BufferedWriter bw, ArrayList<String[]> array) throws IOException {
		bw.write("@relation tweet");
		bw.newLine();
		bw.write("@attribute 'Topic' String");
		bw.newLine();
		bw.write("@attribute 'Sentiment' { positive, neutral, irrelevant, negative}");
		bw.newLine();
		bw.write("@attribute 'TweetId' numeric");
		bw.newLine();
		bw.write("@attribute 'TweetDate' String");
		bw.newLine();
		bw.write("@attribute 'TweetText' String");
		bw.newLine();
		bw.write("@DATA");
		bw.newLine();
		Iterator<String[]> i = array.iterator();
		while (i.hasNext()) {
			String[] oraingoa = i.next();
			while (oraingoa.length < 5 && i.hasNext())
				oraingoa = i.next();
			bw.write("'" + oraingoa[0].replace("\"", "") + "',");
			bw.write(oraingoa[1] + ",");
			bw.write(oraingoa[2] + ",");
			bw.write("'" + oraingoa[3] + "',");
			bw.write("'" + oraingoa[4].trim().replace(",", "").replace("'", ""));
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
	
	
		
	


}
