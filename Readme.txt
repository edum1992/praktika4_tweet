4Praktika:

Programa hau, Eclipse Mars.2, Eclipse Mars.3 eta JDK 8 dependentziekin probatu da.
Gure programa instalatzeko pausuak honakoak dira:
	- Eclipsen inportatu proiektuak.
	- Programak exekutatu, proiektu guztietan klase nagusia Nagusia.java da.

Aurreprozesamendua

	Programa honek, sartutako csv-ak .arff bilakatuko ditu, datuak prosezatu ahal izateko eta lanean hasteko.
	Horretarako, filtro batzuk ere aplikatuko ditugu:
		- Remove: Informaziorik ematen ez duten atributuak kentzeko.
		- BagOfWords: bektore batetan bilakatzen ditugu, eta lerro horretan hitz bakoitza zenbat aldiz agertzen den erakusten du. 
		- SparseToNonSparse: Formatoa aldatzeko
		- InfoGain: atributuen balioa neurtzen du klasearekiko.
		- TFIDF: TFIDF aplikatuko du.

	Aurre-baldintza: Hiru csv fixategi sartu, bat train bestea dev eta azkenekoa test, edozein ordenetan.
	Post-baldintza: Hiru .arff fitxategi itzuliko ditu, filtroak aplikatuta.
	
	Erabileraren adibidea:
	Kontsolan:
		java -jar Preprocess.jar HEMEN 3 CSV-EN HELBIDEA!!!
	Irteera:
		.csv-ak dauden karpetan gordeko ditu hiru .arff-ak

		
Inferentzia

	Programa honi, aurretik lortutako, train eta dev .arff fitxategiak pasatuko dizkiogu.
	Programa honek, datu hauek klasifikatuko ditu metodo ezberdinen bidez eta bi modelo itzuliko ditu:
		- RandomForest erabiliz lortuko dugun modeloa.
		- NaiveBayes erabiliz lortuko dugun modeloa.
	
	Aurre-baldintza: Bi .arff fixategi sartu, bat train eta bestea dev, edozein ordenetan.
	Post-baldintza: Bi .model itzuliko ditu.
	
	Erabileraren adibidea:
		Kontsolan:
			java -jar GetModel.jar HEMEN 2 ARFF-EN HELBIDEA!!!
		Irteera:
			sartutako lehen fitxategiaren karpetan gordeko ditu modeloak.

Sailkapena

	Programa honen helburua, klasea iragartzea da, beraz modelo bat eta test .arff-a pasatuko diogu edozein ordenetan.
	Haiekin, instantzia bakoitzaren klasea iragarriko du eta predictions.arff baten sartuko ditu.

	Aurre-baldintza: .arff fitxategi bat (test) eta .model bat, edozein ordenetan.
	Post-baldintza: iragarritako klasez betetako .arff-a.
	
	Erabileraren adibidea:
		Kontsolan:
			java -jar Classify.jar HEMEN ARFF ETA MODEL-AREN HELBIDEA!!!
		Irteera:
			.arff-a, hau da, testa dagoen karpetan gordeko du iragarpenen .arff-a.
