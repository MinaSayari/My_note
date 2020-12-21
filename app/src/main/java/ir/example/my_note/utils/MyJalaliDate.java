package ir.example.my_note.utils;

import java.util.Arrays;

public class MyJalaliDate {

    public boolean gLeapYear(int year) {
        if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)))
            return true;
        else
            return false;
    }

    public boolean sLeapYear(int year) {
        int[] ary = {1, 5, 9, 13, 17, 22, 26, 30};
        boolean result = false;
        int b = year % 33;
        if (Arrays.asList(ary).contains(b))
            result = true;
        return result;
    }

    public String shamsiDate(int gyear, int gmonth, int gday) {
        int[] _gl = {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};
        int[] _g = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};

        int deydiffjan = 10;
        int gd = 0;
        int sd = 0;
        int sm = 0;
        int gmod = 0;
        int sy = 0;

        if (gLeapYear(gyear - 1))
            deydiffjan = 11;
        if (gLeapYear(gyear))
            gd = _gl[gmonth - 1] + gday;
        else
            gd = _g[gmonth - 1] + gday;
        if (gd > 79) {
            sy = gyear - 621;
            gd = gd - 79;
            if (gd <= 186) {
                gmod = gd % 31;
                switch (gmod) {
                    case 0:
                        sd = 31;
                        sm = (int) (gd / 31);
                        break;
                    default:
                        sd = gmod;
                        sm = (int) (gd / 31) + 1;
                }
            } else {
                gd = gd - 186;
                gmod = gd % 30;
                switch (gmod) {
                    case 0:
                        sd = 30;
                        sm = (int) (gd / 30) + 6;
                        break;
                    default:
                        sd = gmod;
                        sm = (int) (gd / 30) + 7;
                }
            }
        } else {
            sy = gyear - 622;
            gd = gd + deydiffjan;
            gmod = gd % 30;
            switch (gmod) {
                case 0:
                    sd = 30;
                    sm = (int) (gd / 30) + 9;
                    break;
                default:
                    sd = gmod;
                    sm = (int) (gd / 30) + 10;
            }
        }

        return String.valueOf(sy) + '/' + String.valueOf(sm) + '/' + String.valueOf(sd);
    }
}
