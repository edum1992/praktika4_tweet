import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Instances;

public class Nagusia {

	public static void main(String[] args) throws Exception {
		Instances train = null;
		Instances dev = null;

		Sailkatu s = new Sailkatu();

		//dev eta train kargatu bi aldagaietan
		for (int i = 0; i < args.length; i++) {
			FileReader fi = new FileReader(args[i]);
			BufferedReader bw = new BufferedReader(fi);
			if (args[i].toLowerCase().contains("dev"))
				dev = new Instances(bw);
			else if (args[i].toLowerCase().contains("train"))
				train = new Instances(bw);

			fi.close();
		}

		train.setClassIndex(train.numAttributes() -1);
		dev.setClassIndex(dev.numAttributes() -1);

		//dev eta train batu:
		Instances data = new Instances(dev);
		data.addAll(train);

		System.out.println("Instantzia kopurua: " + data.numInstances());
		System.out.println("Atributu kopurua: " + data.numAttributes());

		data.setClassIndex(data.numAttributes()-1);

		//naiveBayes eta randomForest sailkatzaileak entrenatu, bakoitzean ebaluazio ez-zintzoa, trainVSdev eta crossValidation metodoekin.
		s.naiveBayes(data, dev, train, args[0]);
		s.randomForestEgin(data, dev, train, args[0]);

	}
}
