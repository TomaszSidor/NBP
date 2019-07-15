import com.google.gson.Gson;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.Scanner;

public class NBP {

    public static void main(String[] args) throws IOException {

        LocalDate now = LocalDate.now();
        LocalDate before = now.minusMonths(1);

        String[] curencies = {"usd", "eur", "gbp", "chf"};
        String[] midLinkArray = new String[curencies.length];
        String[] bidLinkArray = new String[curencies.length];

//        Wyświetl kursy średnie 4 walut dzisiaj
        showAverageCourses(now, curencies, midLinkArray);
        //        Wyświetl kursy średnie 4 walut miesiąc temu
        showAverageCourses(before, curencies, midLinkArray);
//        System.out.println("Kurs Kupna");
//        for (int i = 0; i <bidLinkArray.length; i++) {
//            Exchange nbpBidApiJson = getNbpApiJson(bidLinkArray[i]);
//            System.out.println(getCurrencyCode(nbpBidApiJson)
//                    + " " + getExchangeBidRate(nbpBidApiJson) + "PLN");
//        }
//        showAskRate(bidLinkArray);
    }

    private static void showAskRate(String[] bidLinkArray) throws IOException {
        System.out.println("Kurs Sprzedaży");
        for (int i = 0; i <bidLinkArray.length; i++) {
            Exchange nbpAskApiJson = getNbpApiJson(bidLinkArray[i]);
            System.out.println(getCurrencyCode(nbpAskApiJson)
                    + " " + getExchangeAskRate(nbpAskApiJson) + "PLN");
        }
    }

    private static void showAverageCourses(LocalDate now, String[] curencies, String[] midLinkArray) {
        System.out.println("Kurs Sredni:");
        for (int i = 0; i < midLinkArray.length; i++) {
            try {
                midLinkArray[i] = "http://api.nbp.pl/api/exchangerates/rates/a/" + curencies[i] + "/" + now;
                Exchange nbpApiJson = getNbpApiJson(midLinkArray[i]);
                System.out.println(getCurrencyCode(nbpApiJson)
                        + " - " + getExchangeMidRate(nbpApiJson) + "PLN");

            } catch (IOException exception) { //jeśli nie ma raportu z dzisiaj, szukamy w tył aż się pojawi
                now = now.minusDays(1);
                i = -1;
            }
        }
        System.out.println("Dane z dnia " + now);
    }
////        Ile kupimy waluty za 100 PLN?
//        for (int i = 0; i <bidLinkArray.length; i++) {
//            System.out.println("za 100PLN mozesz kupić "
//                    + (100 / Double.parseDouble(getExchangeAskRate(getNbpApiJson(bidLinkArray[i]))))
//                    + " " + getCurrencyCode(getNbpApiJson(bidLinkArray[i])));
//        }

//        Ile zarobimy/stracimy przez miesiac?
//        Double BidRate = Double.parseDouble(getExchangeAskRate(getNbpApiJson("http://api.nbp.pl/api/exchangerates/rates/c/usd/" + now)));
//        System.out.println("Kurs sprzedaży dzisiaj " + BidRate);
//
//        Double AskRate = Double.parseDouble(getExchangeAskRate(getNbpApiJson("http://api.nbp.pl/api/exchangerates/rates/c/usd/" + before)));
//        System.out.println("kurs Kupna miesiac temu " + AskRate);
//        System.out.println("Kupując za 100PLN dnia " + before + " " + 100/AskRate + " USD, zarobiliśmy "
//                + (100/AskRate*BidRate-100) + " PLN.");
//   }

    private static Exchange getNbpApiJson(String link) throws IOException {

            URL page = new URL(link);
            URLConnection connection = page.openConnection();

            try (InputStream inputStream = connection.getInputStream();
                 Scanner scanner = new Scanner(inputStream);
            ) {

                String nbpApiJson = scanner.nextLine();
                Gson gson = new Gson();
                return gson.fromJson(nbpApiJson, Exchange.class);
            }
    }

    public static String getExchangeMidRate(Exchange exchange){

        /*exchange.rates.get(0).mid                     klasa w ktorej jest tablica(kurs).nazwa tablicy(rates)
                                                     .pobieramy element tablicy{tablica ma tylko jeden element wiec(get(0))
                                                     .i z elementu tablicy bierzemy mida, albo co kolwiek chcemy(mid)
                                                     */
        return exchange.rates.get(0).mid;
    }
    public static String getCurrencyCode(Exchange exchange){

        return exchange.code;
    }
    public static String getExchangeBidRate(Exchange exchange){

        return exchange.rates.get(0).bid;
    }
    public static String getExchangeAskRate(Exchange exchange){

        return exchange.rates.get(0).ask;
    }

}



