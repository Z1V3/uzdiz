package azivko_zadaca_1;

import java.util.ArrayList;
import java.util.List;

public class UpraviteljPodataka {
    private static UpraviteljPodataka instance;
    private List<String> errorLog = new ArrayList<>();

    private UpraviteljPodataka() {}

    public static UpraviteljPodataka getInstance() {
        if (instance == null) {
            instance = new UpraviteljPodataka();
        }
        return instance;
    }

    public void initializeSystem(String stationsFile, String compositionFile, String vehiclesFile) {
    	
        UcitavacPodatakaFactory stationFactory = new UcitavacPodatakaStanicaFactory();
        UcitavacPodataka stationLoader = stationFactory.izradiUcitavacPodataka();
        errorLog = stationLoader.ucitaj(stationsFile);
        if(!errorLog.isEmpty()) {
        	printErrorLog(stationsFile);        	
        }
        
        UcitavacPodatakaFactory vehicleFactory = new UcitavacPodatakaVozilaFactory();
        UcitavacPodataka vehicleLoader = vehicleFactory.izradiUcitavacPodataka();
        errorLog = vehicleLoader.ucitaj(vehiclesFile);
        if(!errorLog.isEmpty()) {
        	printErrorLog(vehiclesFile);        	
        }
        
        UcitavacPodatakaFactory compositionFactory = new UcitavacPodatakaKompozicijaFactory();
        UcitavacPodataka compositionLoader = compositionFactory.izradiUcitavacPodataka();
        errorLog = compositionLoader.ucitaj(compositionFile);
        if(!errorLog.isEmpty()) {
        	printErrorLog(compositionFile);
        }
        
    }

    private void printErrorLog(String fileName) {
        System.out.println("Error Log: " + "File - " + fileName);
        for (String error : errorLog) {
            System.out.println(error);
        }
    }
}
