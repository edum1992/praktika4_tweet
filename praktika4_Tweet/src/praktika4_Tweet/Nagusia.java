package praktika4_Tweet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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
			else if (args[i].toLowerCase().contains("test"))
				test = new Instances(bw);
			else if (args[i].toLowerCase().contains("train"))
				train = new Instances(bw);

			fi.close();
		}

		arff2Bow.atributuakKendu(dev);
		arff2Bow.atributuakKendu(train);
		arff2Bow.atributuakKendu(test);
		Instances datuak_kenduta = new Instances(dev);
		datuak_kenduta.addAll(train);
		datuak_kenduta.addAll(test);
	
		//Instances datuak_kenduta = arff2Bow.atributuakKendu(data);

		datuak_kenduta.setClassIndex(0);

		System.out.println(datuak_kenduta.numAttributes());
		Instances datuak_BOW = arff2Bow.stringToWordVector(datuak_kenduta, Integer.MAX_VALUE, false);
		datuak_BOW = arff2Bow.sparseToNonSparseAplikatu(datuak_BOW);

		Instances datuak_InfoGain = arff2Bow.infoGainAttributeEvalAplikatu(datuak_BOW);
		Instances datuakTFIDF = arff2Bow.stringToWordVector(datuak_InfoGain, Integer.MAX_VALUE, true);
		
		dev = new Instances(datuakTFIDF, 0, dev.numInstances());	//lehenengo datuak dev direnez, lehenengo dev atera behar da
		train = new Instances(datuakTFIDF, dev.numInstances(), train.numInstances());
		test = new Instances(datuakTFIDF, dev.numInstances() + train.numInstances(), test.numInstances());
		
		ArffSaver gorde = new ArffSaver();
		File f = new File(args[0]+"_TFIDF_dev.arff");
		gorde.setInstances(dev);
		gorde.setFile(f);
		gorde.writeBatch();
		
		gorde = new ArffSaver();
		f = new File(args[0]+"_TFIDF_train.arff");
		gorde.setInstances(train);
		gorde.setFile(f);
		gorde.writeBatch();
		
		gorde = new ArffSaver();
		f = new File(args[0]+"_TFIDF_test.arff");
		gorde.setInstances(test);
		gorde.setFile(f);
		gorde.writeBatch();
	}
}
