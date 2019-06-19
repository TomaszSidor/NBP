
import java.util.ArrayList;

public class Exchange {
    public String currency;
    public String effectiveDate;
    public String code;
    public ArrayList<Rate> rates;

    public Exchange(String currency, String effectiveDate, String code, ArrayList<Rate> rates) {
        this.currency = currency;
        this.effectiveDate = effectiveDate;
        this.code = code;
        this.rates = rates;
    }
}
