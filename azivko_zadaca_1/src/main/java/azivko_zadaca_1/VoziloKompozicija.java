package azivko_zadaca_1;

enum Uloga{
	P,
	V
}

public class VoziloKompozicija {
	private Uloga uloga;
	private PrijevoznoSredstvo vozilo;
	
	public VoziloKompozicija(Uloga uloga, PrijevoznoSredstvo vozilo) {
		super();
		this.uloga = uloga;
		this.vozilo = vozilo;
	}
	
	public Uloga getUloga() {
		return uloga;
	}
	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}
	public PrijevoznoSredstvo getVozilo() {
		return vozilo;
	}
	public void setVozilo(PrijevoznoSredstvo vozilo) {
		this.vozilo = vozilo;
	}
	
}
