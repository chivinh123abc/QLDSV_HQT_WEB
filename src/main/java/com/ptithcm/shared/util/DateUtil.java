package com.ptithcm.shared.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";

    private DateUtil() {
    }

    /**
     * Chuyển đổi một chuỗi ngày tháng thành đối tượng Date sử dụng định dạng mặc
     * định (yyyy-MM-dd).
     *
     * @param dateStr
     *            Chuỗi ngày tháng cần chuyển đổi
     * @return Đối tượng Date, hoặc null nếu chuỗi rỗng/lỗi định dạng
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, DEFAULT_PATTERN);
    }

    /**
     * Chuyển đổi một chuỗi ngày tháng thành đối tượng Date sử dụng định dạng được
     * chỉ định.
     *
     * @param dateStr
     *            Chuỗi ngày tháng cần chuyển đổi
     * @param pattern
     *            Định dạng ngày tháng (ví dụ: yyyy-MM-dd)
     * @return Đối tượng Date, hoặc null nếu chuỗi rỗng/lỗi định dạng
     */
    public static Date parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Định dạng đối tượng Date thành chuỗi sử dụng định dạng mặc định (yyyy-MM-dd).
     *
     * @param date
     *            Đối tượng Date cần định dạng
     * @return Chuỗi ngày tháng đã định dạng
     */
    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_PATTERN);
    }

    /**
     * Định dạng đối tượng Date thành chuỗi sử dụng định dạng được chỉ định.
     *
     * @param date
     *            Đối tượng Date cần định dạng
     * @param pattern
     *            Định dạng đầu ra mong muốn
     * @return Chuỗi ngày tháng đã định dạng, hoặc chuỗi rỗng nếu date null
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern).format(date);
    }
}
