package azivko_zadaca_1;

enum VrstaStanice {
	KOL,
	STAJ
}

enum AktivnostiStanice{
	UIP,
	UIR,
	OBA
}

public class ZeljeznickaStanica {
	private String stanicaNaziv;
	private VrstaStanice vrstaStanice;
	private AktivnostiStanice aktivnosti;
	private int brojPerona;
	private boolean statusStanice;
	private int udaljenostOdPocetne;
	
	public ZeljeznickaStanica(String stanicaNaziv, VrstaStanice vrstaStanice, AktivnostiStanice aktivnosti, int brojPerona,
			boolean statusStanice, int udaljenostOdPocetne) {
		super();
		this.stanicaNaziv = stanicaNaziv;
		this.vrstaStanice = vrstaStanice;
		this.aktivnosti = aktivnosti;
		this.brojPerona = brojPerona;
		this.statusStanice = statusStanice;
		this.udaljenostOdPocetne = udaljenostOdPocetne;
	}

	public String getStanicaNaziv() {
		return stanicaNaziv;
	}

	public void setStanicaNaziv(String stanicaNaziv) {
		this.stanicaNaziv = stanicaNaziv;
	}

	public VrstaStanice getVrstaStanice() {
		return vrstaStanice;
	}

	public void setVrstaStanice(VrstaStanice vrstaStanice) {
		this.vrstaStanice = vrstaStanice;
	}

	public AktivnostiStanice getAktivnosti() {
		return aktivnosti;
	}

	public void setAktivnosti(AktivnostiStanice aktivnosti) {
		this.aktivnosti = aktivnosti;
	}

	public int getBrojPerona() {
		return brojPerona;
	}

	public void setBrojPerona(int brojPerona) {
		this.brojPerona = brojPerona;
	}

	public boolean isStatusStanice() {
		return statusStanice;
	}

	public void setStatusStanice(boolean statusStanice) {
		this.statusStanice = statusStanice;
	}
	
	public int getUdaljenostOdPocetne() {
		return this.udaljenostOdPocetne;
	}
	
	public void setUdaljenostOdPocetne(int udaljenost) {
		this.udaljenostOdPocetne = udaljenost;
	}
}
