package azivko_zadaca_1;

import java.util.ArrayList;

enum NamjenaVozila{
	PSVPVK,
	PSVP,
	PSBP
}

enum VrstaPrijevoza{
	N,
	P,
	TA,
	TK,
	TRS,
	TTS,
	TPS
}

enum VrstaPogona{
	N,
	D,
	B,
	E,
	S
}

enum StatusVozila{
	I,
	K
}

public class PrijevoznoSredstvo {
	private String oznaka;
	private NamjenaVozila namjenaVozila;
	private VrstaPrijevoza vrstaPrijevoza;
	private VrstaPogona vrstaPogona;
	private double maxSnaga;
	private double maxBrzina;
	private int godinaProizvodnje;
	private String proizvodac;
	private ArrayList<Integer> brojMjesta;
	private double nosivost;
	private int zapremina;
	private StatusVozila status;
	public PrijevoznoSredstvo(String oznaka, NamjenaVozila namjenaVozila, VrstaPrijevoza vrstaPrijevoza, VrstaPogona vrstaPogona,
			double maxSnaga, double maxBrzina, int godinaProizvodnje, String proizvodac, ArrayList<Integer> brojMjesta,
			double nosivost, int zapremina, StatusVozila status) {
		super();
		this.oznaka = oznaka;
		this.namjenaVozila = namjenaVozila;
		this.vrstaPrijevoza = vrstaPrijevoza;
		this.vrstaPogona = vrstaPogona;
		this.maxSnaga = maxSnaga;
		this.maxBrzina = maxBrzina;
		this.godinaProizvodnje = godinaProizvodnje;
		this.proizvodac = proizvodac;
		this.brojMjesta = brojMjesta;
		this.nosivost = nosivost;
		this.zapremina = zapremina;
		this.status = status;
	}
	
	public String getOznaka() {
		return this.oznaka;
	}
	
	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}
	
	public NamjenaVozila getNamjenaVozila() {
		return this.namjenaVozila;
	}
	public void setNamjenaVozila(NamjenaVozila namjenaVozila) {
		this.namjenaVozila = namjenaVozila;
	}
	public VrstaPrijevoza getVrstaPrijevoza() {
		return vrstaPrijevoza;
	}
	public void setVrstaPrijevoza(VrstaPrijevoza vrstaPrijevoza) {
		this.vrstaPrijevoza = vrstaPrijevoza;
	}
	public VrstaPogona getVrstaPogona() {
		return vrstaPogona;
	}
	public void setVrstaPogona(VrstaPogona vrstaPogona) {
		this.vrstaPogona = vrstaPogona;
	}
	public double getMaxSnaga() {
		return maxSnaga;
	}
	public void setMaxSnaga(double maxSnaga) {
		this.maxSnaga = maxSnaga;
	}
	public double getMaxBrzina() {
		return maxBrzina;
	}
	public void setMaxBrzina(double maxBrzina) {
		this.maxBrzina = maxBrzina;
	}
	public int getGodinaProizvodnje() {
		return godinaProizvodnje;
	}
	public void setGodinaProizvodnje(int godinaProizvodnje) {
		this.godinaProizvodnje = godinaProizvodnje;
	}
	public String getProizvodac() {
		return proizvodac;
	}
	public void setProizvodac(String proizvodac) {
		this.proizvodac = proizvodac;
	}
	public ArrayList<Integer> getBrojMjesta() {
		return brojMjesta;
	}
	public void setBrojMjesta(ArrayList<Integer> brojMjesta) {
		this.brojMjesta = brojMjesta;
	}
	public double getNosivost() {
		return nosivost;
	}
	public void setNosivost(double nosivost) {
		this.nosivost = nosivost;
	}
	public int getZapremina() {
		return zapremina;
	}
	public void setZapremina(int zapremina) {
		this.zapremina = zapremina;
	}
	public StatusVozila getStatus() {
		return status;
	}
	public void setStatus(StatusVozila status) {
		this.status = status;
	}

	public String toString() {
		return "" + this.oznaka + " " + this.godinaProizvodnje + " " + this.maxBrzina + " " + this.maxSnaga + " " + this.nosivost + " " + this.proizvodac + " " + this.vrstaPogona + " " + this.vrstaPrijevoza;
	}
	
	
}
