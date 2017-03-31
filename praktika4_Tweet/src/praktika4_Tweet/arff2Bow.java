package praktika4_Tweet;

import java.io.BufferedReader;
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

	/*sparseToNonSparse metodoak, honakoa egiten du:
		datu batzuk emanda, SparseToNonSparse filtroa aplikatzen dio.
		Sartutako instantzia guztiak SparseToNonSparse formatoan bilakatzen dituen filtroa da.
	*/
	public static Instances sparseToNonSparseAplikatu(Instances data) throws Exception {
		SparseToNonSparse stns = new SparseToNonSparse();
		stns.setInputFormat(data);
		return Filter.useFilter(data, stns);
	}

	/*stringToWordVector:
		datuei filtro hau aplikatzerako orduan, Bag Of Words delakoa egiten da.
		bektore batetan bilakatzen ditugu, eta lerro horretan hitz bakoitza zenbat aldiz agertzen den erakusten du.
		boolean bat pasatzen diogu, TFT eta IDFT egitea esateko, hau da, boolean hori true bada, TFT eta IDFT aplikatuko du, bestela ez.
	*/
	public static Instances stringToWordVector(Instances data, int hitzKopurua, boolean erabilia) throws Exception {
		StringToWordVector stwv = new StringToWordVector(hitzKopurua);
		stwv.setIDFTransform(erabilia);
		stwv.setTFTransform(erabilia);
		stwv.setAttributeIndicesArray(new int[] { 1 });
		stwv.setLowerCaseTokens(true);
		stwv.setOutputWordCounts(true);
		stwv.setInputFormat(data);
		return Filter.useFilter(data, stwv);
	}

	// atributuakKendu metodoak, guretzako erabilerarik ez duten atributuak kentzen ditu, informazio baliagarririk ez dutelako ematen.
	public static Instances atributuakKendu(Instances test) throws Exception {
		Remove r = new Remove();
		r.setAttributeIndicesArray(new int[] { 0, 2, 3 }); // 0.2 eta 3 zutabeak, atributu horiek eraginik ez dutelako.
		r.setInputFormat(test);
		return Filter.useFilter(test, r);
	}

	/*infoGainAttributeEvalAplikatu:
		datuei filtro hau aplikatzerako orduan, atributuen balioa neurtzen du klasearekiko.
		0.0001 baino balio gutxiagoa duten hitzak datuetatik kentzen ditu.
		Hitz horiek informazio eskasa ematen dutelako klasea iragartzeko orduan.
	*/
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
		remove.setAttributeIndicesArray(gordetako_Atributuak); // atributu onak pasatzen diogu, gorde nahi ditugunak
		remove.setInvertSelection(true); // beraz, invert egitean gordetako atributuak ez ezik, besteak kentzen ditugu.
		remove.setInputFormat(data);
		return Filter.useFilter(data, remove);
	}
}
