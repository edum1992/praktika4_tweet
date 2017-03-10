package praktika4_Tweet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class Nagusia {
	
	public static void main(String[] args) throws IOException {
		FileReader fi = new FileReader(args[0]);
		BufferedReader bf = new BufferedReader(fi);
		
		FileWriter fw = new FileWriter(args[1]);
		BufferedWriter bw = new BufferedWriter(fw);
		
		String[] izena = args[0].split("/");
		String relation = izena[izena.length-1];
		String[] relation2 = relation.split(".");
		System.out.println(relation2);
		String relation3 = relation2[0];
		
		bw.write("@relation " + relation3 );
		
		
		
		/*
		//Load Csv
		//CSVLoader loader = new CSVLoader();
	    bf.setSource(new File(args[0]));
	    Instances data = loader.getDataSet();
	    
	    // save ARFF
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(new File(args[1]));
	    saver.setDestination(new File(args[1]));
	    saver.writeBatch();
	    */
	}

}
