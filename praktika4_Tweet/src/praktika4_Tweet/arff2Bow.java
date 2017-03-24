package praktika4_Tweet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

public class arff2Bow {
	
	public static void main(String[] args) throws Exception {
		FileReader fi = new FileReader(args[0]);
		Instances data = new Instances(fi);
		data.setClassIndex(1);
		fi.close();
		
		ArffSaver gorde = new ArffSaver();
		File f = new File(args[1]);
		
		Instances datuak_filtratuta = stringToWordVector(data, Integer.MAX_VALUE, false);
		datuak_filtratuta = sparseToNonSparseAplikatu(datuak_filtratuta);
		//datuak_filtratuta = 
		
		gorde.setInstances(datuak_filtratuta);
		gorde.setFile(f);
		gorde.writeBatch();
	}
	
	
	public static Instances sparseToNonSparseAplikatu(Instances data) throws Exception {
		SparseToNonSparse stns = new SparseToNonSparse();
		stns.setInputFormat(data);
		return Filter.useFilter(data, stns);
	}

	//BagOfWords
	public static Instances stringToWordVector(Instances data, int hitzKopurua, boolean erabilia) throws Exception {
		StringToWordVector stwv = new StringToWordVector(hitzKopurua);
		stwv.setIDFTransform(erabilia);
		stwv.setTFTransform(erabilia);
		stwv.setAttributeIndicesArray(new int[] { 0 });
		stwv.setLowerCaseTokens(true);
		stwv.setOutputWordCounts(true);
		stwv.setInputFormat(data);
		return Filter.useFilter(data, stwv);
	}

	//remove
	public Instances atributuakKendu(Instances test) throws Exception {
		Remove r = new Remove();
		r.setAttributeIndicesArray(new int[]{ 0,2,3});		//0.2 eta 3 zutabeak, atributu horiek eraginik ez dutelako.
		r.setInvertSelection(true);
		r.setInputFormat(test);
		return Filter.useFilter(test, r);
}
	
	public static Instances infoGainAttributeEvalAplikatu(Instances data) throws Exception {
		Ranker r = new Ranker();
		InfoGainAttributeEval i = new InfoGainAttributeEval();
		AttributeSelection aS = new AttributeSelection();
		r.setNumToSelect(-1);
		r.setThreshold(0.0001);
		aS.setSearch(r);
		aS.setEvaluator(i);
		aS.SelectAttributes(data);
		int[] gordetako_Atributuak = aS.selectedAttributes();
		Remove remove = new Remove(); 
		remove.setAttributeIndicesArray(gordetako_Atributuak); //atributu onak pasatzen diogu, los que nos queremos quedar
		remove.setInvertSelection(true); //en vez de borrar los que le pasas, que borre los demas.
		remove.setInputFormat(data); 
		return Filter.useFilter(data, remove);
		
	}
	
	
}
