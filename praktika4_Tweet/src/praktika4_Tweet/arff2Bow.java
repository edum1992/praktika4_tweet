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
	
	public Instances atributuHautaketa(Instances data) throws Exception {
		// Fitxategiak banatu lehenago!
		// EZIN DA SPARSE IZAN!!!!!!!!!
		AttributeSelection attSelection = new AttributeSelection();
		InfoGainAttributeEval igae = new InfoGainAttributeEval();
		Ranker r = new Ranker();
		data.setClassIndex(0);
		r.setNumToSelect(300);
		attSelection.setSearch(r);
		attSelection.setEvaluator(igae);
		attSelection.SelectAttributes(data);
		lista = attSelection.selectedAttributes();
		return this.atributuakKendu(data);
	}

	public Instances sparseToNonSparzeAplikatu(Instances data) throws Exception {
		SparseToNonSparse stns = new SparseToNonSparse();
		stns.setInputFormat(data);
		return Filter.useFilter(data, stns);
	}

	public Instances stringToWordVectorAplikatu(Instances data, int hitzKopurua, boolean bigarrena) throws Exception {
		StringToWordVector stwv = new StringToWordVector(hitzKopurua);
		stwv.setIDFTransform(bigarrena);
		stwv.setTFTransform(bigarrena);
		stwv.setAttributeIndicesArray(new int[] { 0 });
		stwv.setLowerCaseTokens(true);
		stwv.setOutputWordCounts(true);
		stwv.setInputFormat(data);
		return Filter.useFilter(data, stwv);
	}

	public Instances atributuakKendu(Instances test) throws Exception {
		Remove r = new Remove();
		r.setAttributeIndicesArray(lista);
		r.setInvertSelection(true);
		r.setInputFormat(test);
		return Filter.useFilter(test, r);
}
	
	
}
