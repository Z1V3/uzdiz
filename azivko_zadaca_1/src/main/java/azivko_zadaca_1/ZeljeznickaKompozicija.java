package azivko_zadaca_1;

import java.util.ArrayList;

public class ZeljeznickaKompozicija {
	private String oznaka;
	private ArrayList<VoziloKompozicija> listaVozila;
	
	public ZeljeznickaKompozicija(String oznaka, ArrayList<VoziloKompozicija> listaVozila) {
		super();
		this.oznaka = oznaka;
		this.listaVozila = listaVozila;
	}
	
	public String getOznaka() {
		return oznaka;
	}
	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}
	public ArrayList<VoziloKompozicija> getListaVozila() {
		return listaVozila;
	}

	public void setListaVozila(ArrayList<VoziloKompozicija> listaVozila) {
		this.listaVozila = listaVozila;
	}
	
}
