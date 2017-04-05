import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;

public class Nagusia {
	public static void main(String[] args) throws Exception {
		hasieratu(args);
	}

	private static Instances test;
	private static Classifier modeloa;

	private static void modelEdoArff(String[] args) throws Exception {
		for (int i = 0; i < args.length; i++) {
			if (args[i].contains(".model")) {
				modeloa = (Classifier) SerializationHelper.read(args[i]);
				System.out.println("Kargatutako sailkatzailearen modeloa " + args[i] + " da");
			} else {
				BufferedReader br = new BufferedReader(new FileReader(new File(args[i])));
				test = new Instances(br);
				System.out.println("Kargatutako arff fitxategia " + args[i] + " da");
			}
		}
	}

	private static void hasieratu(String[] args) throws Exception {
		modelEdoArff(args);
		test.setClassIndex(0);
		Instances sailkatuak = instantziakSailkatu(modeloa, test);
		
		ArffSaver gorde = new ArffSaver();
		File f = new File(pathEraldatu(args));
		gorde.setInstances(sailkatuak);
		gorde.setFile(f);
		gorde.writeBatch();
		
		String path = pathEraldatu(args);
		System.out.println("Fitxategi sailkatua " + path + " karpetan gorde da");
	}

	private static String pathEraldatu(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].contains(".arff"))
				return args[i].replace("test_blind", "predictions");
		}
		return new String();
	}
	
	public static void fitxategiaGorde(Instances data, String path) throws IOException {
		ArffSaver s = new ArffSaver();
		s.setFile(new File(path));
		s.setInstances(data);
		s.writeBatch();
		
	}
	
	public static Instances instantziakSailkatu(Classifier sailkatzailea, Instances data) throws Exception {
		Instances sailkatuak = new Instances(data);
		for (int i = 0; i < data.numInstances(); i++) {
			double clsLabel = sailkatzailea.classifyInstance(data.instance(i));
			sailkatuak.instance(i).setClassValue(clsLabel);
		}
		return sailkatuak;
}
}