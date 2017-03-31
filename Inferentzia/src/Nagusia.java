import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Instances;

public class Nagusia {

	public static void main(String[] args) throws Exception {
		Instances train = null;
		Instances dev = null;
		Instances test = null;
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
	}
}
