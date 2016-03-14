package util;

import java.util.Arrays;
import java.util.HashSet;

public class Common {
    public static HashSet<String> US_STATES = new HashSet<String>(Arrays.asList(new String[] { "AK", "AL", "AR", "AZ",
            "CA", "CO", "CT", "DC", "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME",
            "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "PR",
            "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY" }));

    public static String transIntToOperation(int op) {
        if (op == 1) {
            return "Sell Fund";
        } else if (op == 2) {
            return "Buy Fund";
        } else if (op == 3) {
            return "Deposit Check";
        } else if (op == 4) {
            return "Request Check";
        } else {
            return null;
        }

    }

    public static String convertDoublePriceToString(double price) {
        // DecimalFormat dfPrice = new DecimalFormat("######0.00");
        // return dfPrice.format(price);
        return String.format("%.2f", price);
    }

    public static String convertDoubleShareToString(double shares) {
        // DecimalFormat dfPrice = new DecimalFormat("######0.000");
        return String.format("%.3f", shares);
    }

    public static String convertLongPriceToString(long price) {
        // DecimalFormat dfPrice = new DecimalFormat("######0.00");
        // return dfPrice.format((double) price / 100);
        return String.format("%.2f", (double) price / 100);
    }

    public static String convertLongShareToString(long shares) {
        // DecimalFormat dfPrice = new DecimalFormat("######0.000");
        return String.format("%.3f", (double) shares / 1000);
    }

    public static void p() {
        System.out.println("I'm Here");
        return;
    }

    public static void p(String s) {
        System.out.println(s);
        return;
    }

}
