package azivko_zadaca_1;

public abstract class RegExUtils {
	// Procesor Komanda
	public static final String ISP_REGEX = "^ISP [LRM]\\d{3,4} [NO]$";
	public static final String LRM_REGEX = "^[LRM]\\d{3,4}";
	
	// 
	public static final String isNumberRegex = "^[+]?\\d+(\\d+)?$";
	public static final String isNumberWithNegRegex = "^[-+]?\\d+(\\d+)?$";
	public static final String isDoubleRegex = "^[+]?\\d+(\\.\\d+)?([eE][+]?\\d+)?$";
	public static final String isDoubleWithNegRegex = "^[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?$";
}
