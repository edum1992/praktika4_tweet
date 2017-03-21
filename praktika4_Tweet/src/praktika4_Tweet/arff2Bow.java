package praktika4_Tweet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class arff2Bow {
	
	public static void main(String[] args) throws Exception {
		FileReader fi = new FileReader(args[0]);
		Instances data = new Instances(fi);
		fi.close();
		
		StringToWordVector bektor = new StringToWordVector();
		bektor.setInputFormat(data);
		
		//filtroa aplikatu
		Instances datuak_filtratuta = Filter.useFilter(data, bektor);
		
		ArffSaver gorde = new ArffSaver();
		File f = new File(args[1]);
		
		gorde.setInstances(datuak_filtratuta);
		gorde.setFile(f);
		gorde.writeBatch();
		
	}
}
