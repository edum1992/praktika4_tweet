import weka.core.Instances;

public class Operazioak {
	
	//Instantzia operazioak:
	public Instances instantziakElkartu(Instances dev, Instances train) {
		dev.addAll(train);
		return dev;
}
}
