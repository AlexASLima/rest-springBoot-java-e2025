package br.com.alex.request.converters;

import br.com.alex.exception.UnsupportedMathOperationException;

public class NumberConverter {
    public static boolean isNumeric(String strNumber) {
        if (strNumber == null) return false;
        String number = strNumber.replaceAll(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public static Double convertToDouble(String strNumber) {
        if (strNumber == null || strNumber.isEmpty()) throw new UnsupportedMathOperationException("Please set onl numeric!");
        String number = strNumber.replaceAll(",", ".");// Moeda Americana x Brasileira
        return Double.parseDouble(number);
    }
}
