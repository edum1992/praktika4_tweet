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

	/*datuakIrakurri metodoak, honakoa egiten du:
		sartutako datuak irakurri.
		Ondoren, lerroz lerro ArrayList batera gehitu.
		Azkenik, lista zeharkatzen du eta karaktere arraroak kentzen ditu, hau da, letra normalak edo zenbakiak ez direnak kentzen ditu.
		Array hori da itzultzen duena metodo honek
	*/
	public static ArrayList<String[]> datuakIrakurri(BufferedReader br) throws Exception {
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
				lista[j] = lista[j].replaceAll("[^A-Za-z0-9 ]", " ");
			}
			arraya.set(i, lista);
		}
		return arraya;
	}
		
	/*arffIdatzi metodoak:
		textua lerroz lerro ArrayList batean emanda, arff bat idazten du.
		Lehenik, beti berdinak diren atalak (@relation eta atributuen izenak eta motak) idazten ditu edozein arff izanda.
		Ondoren, lista zeharkatzen du eta arff-a idazten du, arff-ak duen formatoa mantenduz eta listako datuak sartuz.
	*/
	public static void arffIdatzi(BufferedWriter bw, ArrayList<String[]> array) throws IOException {
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
			bw.write(oraingoa[1].replace("unknown", "?") + ",");
			bw.write(oraingoa[2] + ",");
			bw.write("'" + oraingoa[3] + "',");
			bw.write("'" + oraingoa[4].trim().replace(",", "").replace("'", ""));
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
	
	
		
	


}
