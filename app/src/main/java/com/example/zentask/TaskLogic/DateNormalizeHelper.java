package com.example.zentask.TaskLogic;

public class DateNormalizeHelper {
    public static String normalizeDate(String input){
        input = input.trim();

        //patterns that are allowed M/D/Y
        // 1/2/2025 OR 01/02/25 OR 1-2-25 ETC
        input = input.replace("-", "/").replace(".","/").replace(" ", "/");
        String[] parts = input.split("/");

        if(parts.length != 3){
            return ""; //invalid
        }

       try {
           int month = Integer.parseInt(parts[0]);
           int day = Integer.parseInt(parts[1]);
           int year = Integer.parseInt(parts[2]);

           //fix 2 digit years like 25
           if (year < 100) {
               year += 2000;
           }
           if (month < 1 || month > 12) {
               return "";
           }
           if (day < 1 || day > 31) {
               return "";
           }
           //apr jun sep nov have only 30 days
           if((month == 4 || month == 6 || month == 9 || month == 11) && day > 30){
                return "";
           }

           if(month==2){
               boolean leap = (year % 4 == 0);
               int max = leap ? 29 : 28;
               if(day > max){
                   return "";
               }
           }

           return month + "/" + day + "/" + year;
       }catch (NumberFormatException E){
           return "";
       }

    }
}
