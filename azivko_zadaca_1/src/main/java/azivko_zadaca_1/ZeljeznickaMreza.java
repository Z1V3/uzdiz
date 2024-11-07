package azivko_zadaca_1;

import java.util.HashMap;

public class ZeljeznickaMreza {

    private static ZeljeznickaMreza instance;
    private HashMap<String, ZeljeznickaPruga> zeljeznickePruge = new HashMap<>();
    private HashMap<String, PrijevoznoSredstvo> prijevoznaSredstva = new HashMap<>();
    private HashMap<String, ZeljeznickaKompozicija> zeljeznickeKompozicije = new HashMap<>();
    
    private ZeljeznickaMreza() {
    }

    public static ZeljeznickaMreza getInstance() {
        if (instance == null)
        	synchronized(ZeljeznickaMreza.class) {
        		if (instance == null)
        			instance = new ZeljeznickaMreza();        			
        	}
        	return instance;
    }
    
    public void dodajPrugu(String oznakaPruge, ZeljeznickaPruga pruga) {
        if (!this.zeljeznickePruge.containsKey(oznakaPruge)) {
            this.zeljeznickePruge.put(oznakaPruge, pruga);
        }
    }
    
    public ZeljeznickaPruga vratiPrugu(String oznakaPruge) {
        return this.zeljeznickePruge.get(oznakaPruge);
    }
    
    public HashMap<String, ZeljeznickaPruga> vratiListuPruga(){
    	return this.zeljeznickePruge;
    }
    
    public void dodajVozilo(String oznakaVozila, PrijevoznoSredstvo vozilo) {
    	if(!this.prijevoznaSredstva.containsKey(oznakaVozila)) {
    		this.prijevoznaSredstva.put(oznakaVozila, vozilo);
    	}
    }
    
    public void dodajKompoziciju(String oznakaKompozicija, ZeljeznickaKompozicija zeljeznickaKompozicija) {
    	if(!this.zeljeznickeKompozicije.containsKey(oznakaKompozicija)) {
    		this.zeljeznickeKompozicije.put(oznakaKompozicija, zeljeznickaKompozicija);
    	}
    }
    
    public PrijevoznoSredstvo vratiVozilo(String oznakaVozila) {
    	return this.prijevoznaSredstva.get(oznakaVozila);
    }
    
    public HashMap<String, PrijevoznoSredstvo> vratiListuVozila(){
    	return this.prijevoznaSredstva;
    }

	public HashMap<String, ZeljeznickaKompozicija> vratiListuKompozicija() {
		return zeljeznickeKompozicije;
	}
	
}