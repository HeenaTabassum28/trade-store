package com.deutschebank.trade.store;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@UtilityClass
public class DateUtil {

    public static Date todayDate() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant());
    }

    public static Date getDateFromString(String date) {
        return Date.from(LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC).toInstant());
    }
}
