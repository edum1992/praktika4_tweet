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

	/* naiveBayes metodoari datu sorta osoa, dev, train eta path-a pasatzen diogu:
		Path-a, sortzen dugun modeloa non gordetzen dugun esateko erabiltzen dugu, karpeta berean sor dezan geroago ebaluatuko dugun modeloa.
		metodo honek, ondoren agertzen diren hiru metodoei deitzen dio, behealdean hobeto azaltzen ditugunak, hain zuzen.
	*/
	public void naiveBayes(Instances data, Instances dev, Instances train, String path) throws Exception {
		System.out.println("\nNAIVE BAIYES: ");
		this.ebaluazioEzZintzoa(data, new NaiveBayes(), path);
		this.trainVSdev(dev, train, new NaiveBayes());
		this.tenFoldCrossValidation(data, new NaiveBayes());
		System.out.println("\nNAIVE BAIYES BUKATUTA!!");
	}

	/* ebaluazioEzZintzoa metodoari datu sorta osoa, klasifikatzaile hutsa eta path-a pasatzen diogu:
		Path-a, lehen esan dugun moduan, modeloa non gorde dezan esateko da.
		entrenatzeko eta probatzeko datuak berdinak dira metodo honetan.
	*/
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
	
	/* trainVSdev metodoari dev eta train datu multzoak eta sailkatzaile huts bat pasatzen diogu:
		Path-a, lehen esan dugun moduan, modeloa non gorde dezan esateko da.
		dev datuekin ebaluatu eta train datuak entrenatzeko erabiltzen ditugu.
	*/
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
	
	/* tenFoldCrossValidation metodoari data multzoa eta sailkatzaile huts bat pasatzen diogu:
		10 itzuli egiten ditu eskema honek
	*/
	private void tenFoldCrossValidation(Instances data, Classifier sailkatzailea) throws Exception {
		int itzuliak = 10;
		System.out.println("\n" + itzuliak + " itzuliko fold cross validation egingo dugu:");
		Evaluation ebaluatzailea = new Evaluation(data);
		ebaluatzailea.crossValidateModel(sailkatzailea, data, itzuliak, new Random());
		
		System.out.println("Datuak: ------------------------------------------");
		System.out.println(ebaluatzailea.toSummaryString());
		System.out.println(ebaluatzailea.toClassDetailsString());
		System.out.println(ebaluatzailea.toMatrixString());
	}

	/* pathAldatu metodoak, bere izenak esaten duen moduan, path-a (String) aldatzeko erabiltzen dugu:
		path-a zeharkatu egiten dugu, bukaeratik hasi eta '/' bilatu arte.
		Hau da, artxiboaren izena aldatu egiten dugu, klasifikatzailearen izena eta ".model" gehituz.
		Horrela, leku berean utziko dugu modeloa, gainera, klasifikatzailearen izenarekin.
	*/
	private String pathAldatu(String pathZaharra, Classifier sailkatzailea) {
		for (int i = pathZaharra.length() - 1; i > 0; --i) {
			if (pathZaharra.charAt(i) == '/' || pathZaharra.charAt(i) == '\\') {
				return pathZaharra.substring(0, i) + "/" + sailkatzailea.getClass().getSimpleName() + ".model";
			}
		}
		return new String();
	}

	/* randomForestEgin metodoari, datu sorta osoa, dev, train eta path-a pasatzen diogu:
		klasifikatzailea.setNumExecutionSlots(4) metodoa, programa abiarazteko orduan ordenagailuak programari memoria handiagoa esleitzeko erabiltzen dugu, hau da, exekuzioa arinago egiteko.
		RandomForest egiterakoan, CVParameterSelection erabiltzen dugu.
	*/
	public void randomForestEgin(Instances data, Instances dev, Instances train, String path) throws Exception {
		
		RandomForest klasifikatzailea = new RandomForest();
		klasifikatzailea.setNumExecutionSlots(4); //arinago probatzeko
		CVParameterSelection cv = new CVParameterSelection();
		cv.setClassifier(klasifikatzailea);
		cv.addCVParameter("K 1 10 5");	
		cv.addCVParameter("M 1 10 5"); 
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
		this.tenFoldCrossValidation(data, klasifikatzailea);
		
		
	}
}
