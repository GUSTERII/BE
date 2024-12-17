package com.usv.examtimetabling.exam.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
  public static Date convertToDate(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  public static LocalDate convertToLocalDate(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static String formatDate(Date date) {
    return date.toString();
  }
}
