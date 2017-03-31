import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class Nagusia {

	public static void main(String[] args) throws Exception {
		Instances train = null;
		Instances dev = null;
		Instances test = null;
		Operazioak o = new Operazioak();
		
		for (int i = 0; i < args.length; i++) {
			FileReader fi = new FileReader(args[i]);
			BufferedReader bw = new BufferedReader(fi);
			if (args[i].toLowerCase().contains("dev"))
				dev = new Instances(bw);
			else if (args[i].toLowerCase().contains("test"))
				test = new Instances(bw);
			else if (args[i].toLowerCase().contains("train"))
				train = new Instances(bw);

			fi.close();
		}
		
		train.setClassIndex(0);
		test.setClassIndex(0);
		dev.setClassIndex(0);
		
		Instances data = o.instantziakElkartu(train, dev);
		
		ArffSaver gorde = new ArffSaver();
		File f = new File(args[0]+"dev_train.arff");
		gorde.setInstances(dev);
		gorde.setFile(f);
		gorde.writeBatch();
	}
}
