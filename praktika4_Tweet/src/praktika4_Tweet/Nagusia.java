package praktika4_Tweet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class Nagusia {
	public static void main(String[] args) throws Exception {
		for (int kont = 0; kont < args.length; kont++) {
			FileReader fi = new FileReader(args[kont]);
			BufferedReader br = new BufferedReader(fi);

			FileWriter fw = new FileWriter(args[kont] + ".arff");
			BufferedWriter bw = new BufferedWriter(fw);
			Aurreprozesatzailea.arffIdatzi(bw, Aurreprozesatzailea.datuakIrakurri(br));

		}
		Instances train = null;
		Instances dev = null;
		Instances test = null;
		for (int i = 0; i < args.length; i++) {
			FileReader fi = new FileReader(args[i] + ".arff");
			BufferedReader bw = new BufferedReader(fi);
			if (args[i].toLowerCase().contains("dev"))
				dev = new Instances(bw);
			else if (args[i].toLowerCase().contains("train"))
				train = new Instances(bw);
			else if (args[i].toLowerCase().contains("test"))
				test = new Instances(bw);

			fi.close();
		}

		arff2Bow.atributuakKendu(dev);
		arff2Bow.atributuakKendu(train);
		arff2Bow.atributuakKendu(test);
		Instances datuak_kenduta = new Instances(dev);
		
		NireInstances berriak = new NireInstances(dev);
		
		//Textuko lerro bat kargatu ArrayList lerro bakoitzeko eta hau hiru fitxategiekin:
		ArrayList<String> devr = new ArrayList<>();
		BufferedReader brr = new BufferedReader(new FileReader(new File(args[0] + ".arff")));
		String lerroa = "";
		while ((lerroa = brr.readLine()) != null){
			devr.add(lerroa);
		}
		brr.close();

		brr = new BufferedReader(new FileReader(new File(args[1] + ".arff")));
		ArrayList<String> trainr = new ArrayList<>();
		lerroa = "";
		while ((lerroa = brr.readLine()) != null){
			trainr.add(lerroa);
		}		
		brr.close();
		
		brr = new BufferedReader(new FileReader(new File(args[2] + ".arff")));
		ArrayList<String> testr = new ArrayList<>();
		lerroa = "";
		while ((lerroa = brr.readLine()) != null){
			testr.add(lerroa);
		}
		
		NireInstances datuak_batuta = berriak.addAll(devr, trainr, testr);
		
	
		datuak_kenduta = arff2Bow.atributuakKendu(datuak_batuta);

		datuak_kenduta.setClassIndex(0);

		
		Instances datuak_BOW = arff2Bow.stringToWordVector(datuak_kenduta, Integer.MAX_VALUE, false);
		datuak_BOW = arff2Bow.sparseToNonSparseAplikatu(datuak_BOW);
		
		int g1 = berriak.banatzekoDatuak()[0] + berriak.banatzekoDatuak()[1];
		int g11 = berriak.banatzekoDatuak()[0] +1;
		int g2 =  berriak.banatzekoDatuak()[1] - 1;
		int g22 = g1 +1;
		int g23 = berriak.banatzekoDatuak()[2] -1;
		System.out.println(0 + ", " + berriak.banatzekoDatuak()[0]);
		System.out.println(g11 + ", " + berriak.banatzekoDatuak()[1]);
		System.out.println(g22 + ", " + berriak.banatzekoDatuak()[2]);
		
		
		
		dev = new Instances(datuak_BOW, 0, berriak.banatzekoDatuak()[0]);	//lehenengo datuak dev direnez, lehenengo dev atera behar da
		train = new Instances(datuak_BOW, g11,g2);
		test = new Instances(datuak_BOW, g22 , g23);
		
		dev.setClassIndex(0);
		train.setClassIndex(0);
		test.setClassIndex(0);
//		for (int i = 0; i < train.numInstances(); i++) {
//			System.out.println(train.get(i));
//		}

		train = arff2Bow.infoGainAttributeEvalAplikatu(train);
		
		//INFOGAIN APLIKATZEA FALTA DAAA!!! ERROREA EMATEN DU, BERAZ HOBETO AURRERA JARRAITU
		//ORAINDIK ASKO FALTA DELAKO, HOBETO BESTEAREKIN LEHENENGO AMAITU, BESTELA DENBORA BARIK LOTUKO GARELAKO!!!
		
		dev = arff2Bow.stringToWordVector(dev, Integer.MAX_VALUE, true);
		train = arff2Bow.stringToWordVector(train, Integer.MAX_VALUE, true);
		test = arff2Bow.stringToWordVector(test, Integer.MAX_VALUE, true);
			
		
		
		ArffSaver gorde = new ArffSaver();
		File f = new File(args[0]+"_TFIDF_dev.arff");
		gorde.setInstances(dev);
		gorde.setFile(f);
		gorde.writeBatch();
		
		gorde = new ArffSaver();
		f = new File(args[0]+"_TFIDF_test.arff");
		gorde.setInstances(test);
		gorde.setFile(f);
		gorde.writeBatch();
		
		gorde = new ArffSaver();
		f = new File(args[0]+"_TFIDF_train.arff");
		gorde.setInstances(train);
		gorde.setFile(f);
		gorde.writeBatch();
	}
}
