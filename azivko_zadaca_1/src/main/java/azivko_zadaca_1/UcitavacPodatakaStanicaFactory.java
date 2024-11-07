package azivko_zadaca_1;

public class UcitavacPodatakaStanicaFactory implements UcitavacPodatakaFactory{
    @Override
    public UcitavacPodataka izradiUcitavacPodataka() {
        return new UcitavacPodatakaStanica();
    }
}
