package praktika4_Tweet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import weka.core.Instances;

//KLASE HONEN ZERGATIA:
//Klase hau instantziak batzeko egin dugu, Wekak ematen zuen errorea konpontzeko, datuak batzerako orduan errore arraro bat ematen zuelako.

public class NireInstances extends Instances {

	private static final long serialVersionUID = 1L;
	private int[] instantziaKop;
	private ArrayList<String>[] fitxategiak;

	public NireInstances(Reader r) throws IOException {
		super(r);
	}

	public NireInstances(Instances data) {
		super(data);
		this.clear();
	}
	
	//datuak batzeko metodoa
	public NireInstances addAll(@SuppressWarnings("unchecked") ArrayList<String>... fitxategiak) throws IOException {
		this.fitxategiak = fitxategiak;
		this.instantziaKop = new int[this.fitxategiak.length];
		for (int i = 1; i < this.fitxategiak.length; i++) {
			for (Iterator<String> iterator = this.fitxategiak[i].iterator(); iterator.hasNext();) {
				if (iterator.next().startsWith("@"))
					iterator.remove();
			}
		}
		int kop = 0;
		for (int i = 0; i < fitxategiak[0].size(); i++) {
			if (fitxategiak[0].get(i).startsWith("@"))
				kop++;
		}
		ArrayList<String> osoa = new ArrayList<>();
		for (int i = 0; i < this.fitxategiak.length; i++) {
			for (int j = 0; j < this.fitxategiak[i].size(); j++) {
				osoa.add(this.fitxategiak[i].get(j));
			}
			this.instantziaKop[i] = this.fitxategiak[i].size();
			if (i == 0)
				this.instantziaKop[0] -= kop;
		}
		String path = System.getProperty("user.home") + "/temp.arff";
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
		for (String string : osoa) {
			bw.write(string + "\n");
		}
		bw.flush();
		bw.close();
		return new NireInstances(new BufferedReader(new FileReader(new File(path))));
	}

	//datuen instantzia kopura itzultzen du, gero datu horiek banatzeko erabiliko duguna
	public int[] banatzekoDatuak() {
		return this.instantziaKop;
	}
}
