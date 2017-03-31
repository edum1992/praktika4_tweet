import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.CVParameterSelection;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;

public class Sailkatu {

	public void naiveBayes(Instances data, Instances dev, Instances train, String path) throws Exception {
		System.out.println("\nNaive Bayes:");
		this.ebaluazioEzZintzoa(data, new NaiveBayes(), path);
		this.trainVSdev(dev, train, new NaiveBayes());
		this.crossValidation(data, new NaiveBayes());
	}

	
	private void ebaluazioEzZintzoa(Instances data, Classifier sailkatzailea, String path) throws Exception {
		System.out.println("\nEbaluazio ez zintzoa:");
		sailkatzailea.buildClassifier(data);
		Evaluation ebaluatzailea = new Evaluation(data);
		ebaluatzailea.evaluateModel(sailkatzailea, data);
		
		System.out.println("Datuak: ------------------------------------------");
		System.out.println(ebaluatzailea.toSummaryString());
		System.out.println(ebaluatzailea.toClassDetailsString());
		System.out.println(ebaluatzailea.toMatrixString());
		
		SerializationHelper.write(this.pathAldatu(path, sailkatzailea), sailkatzailea);  //modeloa pasatutako path-ean gordetzen du
	}
	
	private void trainVSdev(Instances dev, Instances train, Classifier sailkatzailea) throws Exception {
		System.out.println("\nTrain vs dev:");
		Evaluation ebaluatzailea = new Evaluation(dev);
		sailkatzailea.buildClassifier(train);
		ebaluatzailea.evaluateModel(sailkatzailea, dev);
		
		System.out.println("Datuak: ------------------------------------------");
		System.out.println(ebaluatzailea.toSummaryString());
		System.out.println(ebaluatzailea.toClassDetailsString());
		System.out.println(ebaluatzailea.toMatrixString());
}
	
	private void crossValidation(Instances data, Classifier sailkatzailea) throws Exception {
		int itzuliak = 10;
		System.out.println("\n" + itzuliak + " fold cross validation:");
		Evaluation ebaluatzailea = new Evaluation(data);
		ebaluatzailea.crossValidateModel(sailkatzailea, data, itzuliak, new Random());
		
		System.out.println("Datuak: ------------------------------------------");
		System.out.println(ebaluatzailea.toSummaryString());
		System.out.println(ebaluatzailea.toClassDetailsString());
		System.out.println(ebaluatzailea.toMatrixString());
	}

	private String pathAldatu(String pathZaharra, Classifier sailkatzailea) {
		for (int i = pathZaharra.length() - 1; i > 0; --i) {
			if (pathZaharra.charAt(i) == '/' || pathZaharra.charAt(i) == '\\') {
				return pathZaharra.substring(0, i) + "/" + sailkatzailea.getClass().getSimpleName() + ".model";
			}
		}
		return new String();
	}

	

	public void randomForestEgin(Instances data, Instances dev, Instances train, String path) throws Exception {
		
		RandomForest klasifikatzailea = new RandomForest();
		klasifikatzailea.setNumExecutionSlots(4); //arinago probatzeko
		CVParameterSelection cv = new CVParameterSelection();
		cv.setClassifier(klasifikatzailea);
		cv.addCVParameter("K 1 10 2");			//Probatzeko 2 jarri dut, 5 edo 10ekin gero nahikoa zen
		cv.addCVParameter("M 1 10 2"); 
		cv.setNumFolds(2);
		cv.setSeed(1);
		cv.buildClassifier(data);
		System.out.println(Utils.joinOptions(cv.getBestClassifierOptions()));
		klasifikatzailea = new RandomForest();
		klasifikatzailea.setOptions(cv.getBestClassifierOptions());
		klasifikatzailea.setNumExecutionSlots(4); //arinago probatzeko
		
		
		System.out.println("\nRandom Forest aplikatu:");
		
		this.ebaluazioEzZintzoa(data, klasifikatzailea, path);
		
		klasifikatzailea = new RandomForest();
		klasifikatzailea.setOptions(cv.getBestClassifierOptions());
		klasifikatzailea.setNumExecutionSlots(4); //arinago probatzeko
		this.trainVSdev(dev, train, klasifikatzailea);
		
		
		klasifikatzailea = new RandomForest();
		klasifikatzailea.setOptions(cv.getBestClassifierOptions());
		klasifikatzailea.setNumExecutionSlots(4); //arinago probatzeko
		this.crossValidation(data, klasifikatzailea);
		
		
	}
}
