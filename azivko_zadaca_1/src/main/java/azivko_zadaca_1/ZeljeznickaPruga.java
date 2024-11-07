package azivko_zadaca_1;

import java.util.ArrayList;

enum KategorijaPruge{
	LOKALNA,
	REGIONALNA,
	MEDUANRODNA
}

enum NacinPrijevoza{
	K,
	E
}

enum StatusPruge {
	ISPRAVNA,
	KVAR,
	ZATVORENA
}

public class ZeljeznickaPruga {
	private String oznaka;
	private KategorijaPruge kategorija;
	private NacinPrijevoza nacinPrijevoza; 
	private int brojKolosijeka;
	private int duzina;
	private double doPoOsovini;
	private double doPoDuznomMetru;
	private StatusPruge statusStanice;
	private ArrayList<ZeljeznickaStanica> popisStanica = new ArrayList<ZeljeznickaStanica>();
	public ZeljeznickaPruga(String oznaka, KategorijaPruge kategorija, NacinPrijevoza nacinPrijevoza,
			int brojKolosijeka, double doPoOsovini, double doPoDuznomMetru, StatusPruge statusStanice) {
		super();
		this.oznaka = oznaka;
		this.kategorija = kategorija;
		this.nacinPrijevoza = nacinPrijevoza;
		this.brojKolosijeka = brojKolosijeka;
		this.doPoOsovini = doPoOsovini;
		this.doPoDuznomMetru = doPoDuznomMetru;
		this.statusStanice = statusStanice;
	}

	public void dodajStanicu(ZeljeznickaStanica stanica) {
		popisStanica.add(stanica);
	}

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public KategorijaPruge getKategorija() {
		return kategorija;
	}

	public void setKategorija(KategorijaPruge kategorija) {
		this.kategorija = kategorija;
	}

	public NacinPrijevoza getNacinPrijevoza() {
		return nacinPrijevoza;
	}

	public void setNacinPrijevoza(NacinPrijevoza nacinPrijevoza) {
		this.nacinPrijevoza = nacinPrijevoza;
	}

	public int getBrojKolosijeka() {
		return brojKolosijeka;
	}

	public void setBrojKolosijeka(int brojKolosijeka) {
		this.brojKolosijeka = brojKolosijeka;
	}

	public int getDuzina() {
		return duzina;
	}

	public void setDuzina(int duzina) {
		this.duzina = duzina;
	}

	public double getDoPoOsovini() {
		return doPoOsovini;
	}

	public void setDoPoOsovini(double doPoOsovini) {
		this.doPoOsovini = doPoOsovini;
	}

	public double getDoPoDuznomMetru() {
		return doPoDuznomMetru;
	}

	public void setDoPoDuznomMetru(double doPoDuznomMetru) {
		this.doPoDuznomMetru = doPoDuznomMetru;
	}

	public StatusPruge getStatusStanice() {
		return statusStanice;
	}

	public void setStatusStanice(StatusPruge statusStanice) {
		this.statusStanice = statusStanice;
	}

	public ArrayList<ZeljeznickaStanica> getPopisStanica() {
		return popisStanica;
	}

	public void setPopisStanica(ArrayList<ZeljeznickaStanica> popisStanica) {
		this.popisStanica = popisStanica;
	}
	
	
	
}
