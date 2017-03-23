package praktika4_Tweet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
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
		fi.close();
		
		ArffSaver gorde = new ArffSaver();
		File f = new File(args[1]);
		
		gorde.setInstances(datuak_filtratuta);
		gorde.setFile(f);
		gorde.writeBatch();
	}
	
	
	public Instances sparseToNonSparzeAplikatu(Instances data) throws Exception {
		SparseToNonSparse stns = new SparseToNonSparse();
		stns.setInputFormat(data);
		return Filter.useFilter(data, stns);
	}

	public Instances stringToWordVector(Instances data, int hitzKopurua, boolean erabilia) throws Exception {
		StringToWordVector stwv = new StringToWordVector(hitzKopurua);
		stwv.setIDFTransform(erabilia);
		stwv.setTFTransform(erabilia);
		stwv.setAttributeIndicesArray(new int[] { 0 });
		stwv.setLowerCaseTokens(true);
		stwv.setOutputWordCounts(true);
		stwv.setInputFormat(data);
		return Filter.useFilter(data, stwv);
	}

	public Instances atributuakKendu(Instances test) throws Exception {
		Remove r = new Remove();
		r.setAttributeIndicesArray(new int[]{ 0,2,3});		//0.2 eta 3 zutabeak, atributu horiek eraginik ez dutelako.
		r.setInvertSelection(true);
		r.setInputFormat(test);
		return Filter.useFilter(test, r);
}
	
	
}
