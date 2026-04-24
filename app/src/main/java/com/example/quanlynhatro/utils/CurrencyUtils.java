package com.example.quanlynhatro.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    public static String formatCurrency(double amount) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        return currencyVN.format(amount);
    }
}
