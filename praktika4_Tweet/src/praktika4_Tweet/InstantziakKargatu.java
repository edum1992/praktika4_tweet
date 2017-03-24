package praktika4_Tweet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class InstantziakKargatu {
	public String fitxategiOsoaGorde(ArrayList<String> osoa, String path) throws IOException {
		String pathBerria = this.pathEraldatu(path, "osoa");
		System.out.println("Fitxategi osoa gordeko den path:\n" + pathBerria);
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(pathBerria)));
		for (Iterator<String> iterator = osoa.iterator(); iterator.hasNext();) {
			bw.write(iterator.next());
			bw.newLine();
		}
		bw.flush();
		bw.close();
		return pathBerria;
	}

	public ArrayList<String> lerroakIrakurri(String[] pathLista) throws IOException {
		ArrayList<String> osoa = new ArrayList<>();
		BufferedReader br = null;
		String lerroa = "";
		for (int i = 0; i < pathLista.length; i++) {
			br = new BufferedReader(new FileReader(new File(pathLista[i])));
			while ((lerroa = br.readLine()) != null)
				osoa.add(lerroa);
		}
		br.close();
		return this.burukoakKendu(osoa, pathLista);
}
	private String pathEraldatu(String path, String ipintzeko) {
		if (path.contains("test"))
			path = path.replace("test_blind", ipintzeko);
		else if (path.contains("dev"))
			path = path.replace("dev", ipintzeko);
		else
			path = path.replace("train", ipintzeko);
		return path;

}
	private ArrayList<String> burukoakKendu(ArrayList<String> osoa, String[] pathLista) {
		for (int i = 6; i < osoa.size(); i++) {
			if (osoa.get(i).startsWith("@")) {
				osoa.set(i, "%BESTE FITXATEGI BAT");
			}
		}
		for (int i = 0; i < pathLista.length; i++) {
			osoa.add("%" + pathLista[i]);
		}
		return osoa;
}
}
