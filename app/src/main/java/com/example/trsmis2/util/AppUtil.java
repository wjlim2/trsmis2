package com.example.trsmis2.util;

import com.example.trsmis2.model.Emp;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AppUtil {

    private static AppUtil appUtil = null;

    public static AppUtil getInstance() {
        if (appUtil == null) {
            appUtil = new AppUtil();
        }
        return appUtil;
    }

    public String getIpAddress() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            ip = "0.0.0.0";
            return ip;
        }
        return ip;
    }

    //요일 리턴
    public static String getDateDay(String date, String dateType) {
        try {
            String day = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateType, Locale.KOREA);
            Date nDate = dateFormat.parse(date);

            Calendar cal = Calendar.getInstance();
            if (nDate != null) {
                cal.setTime(nDate);
            }

            int dayNum = cal.get(Calendar.DAY_OF_WEEK);
            int weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);

            switch (dayNum) {
                case 1:
                    day = " 일";
                    break;
                case 2:
                    day = " 월";
                    break;
                case 3:
                    day = " 화";
                    break;
                case 4:
                    day = " 수";
                    break;
                case 5:
                    day = " 목";
                    break;
                case 6:
                    day = " 금";
                    break;
                case 7:
                    day = " 토";
                    break;

            }
            return day;
        } catch (ParseException e) {
            return "";
        }
    }

    public static String getDateToString(Date date) {
        String DateFormat = "yyyy-MM-dd";
        return new SimpleDateFormat(DateFormat, Locale.KOREA).format(date);
    }

    /**
     * amount (month) 만큼 과거 날짜를 반환한다.
     * @param date 기준날짜
     * @param amount 현재 날짜에서 차감할 개월 수
     * @return 계산된 날짜를 yyyy-MM-dd 포맷으로 반환
     */
    public static String monthCalculator(String date, int amount) {
        Date dDate = null;
        try {
            dDate = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        if (dDate != null) {
            cal.setTime(dDate);
        }

        cal.add(Calendar.MONTH, amount);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(cal.getTime());
    }

    public static String dayCalculator(String day, int amount) {
        Date dDate = null;
        try {
            dDate = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        if (dDate != null) {
            cal.setTime(dDate);
        }

        cal.add(Calendar.DAY_OF_MONTH, amount);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(cal.getTime());
    }

    //오늘 날짜와 요일
    public static String getDayAndDate(String date) {
        return date + getDateDay(date, "yyyy-MM-dd");
    }

    public static Emp testEmp() {
        Emp emp = new Emp();

        emp.setJobCd("8");
        emp.setJobCdNm("개발");
        emp.setTeamNm("개발팀");

        return emp;
    }

    public static String todayDateString() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(cal.getTime());
    }

    public static Date getStringToDate(String StringDate) {

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Date to = null;
        try {
            to = transFormat.parse(StringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return to;
    }

    public static String changeDateBarFormat(String dateString) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(getStringToDate(dateString));
    }

    public static String changeDateSlashFormat(String dateString) {
        return new SimpleDateFormat("yyyy / MM / dd", Locale.KOREA).format(getStringToDate(dateString));
    }

    public static String jobCdToNm(String jobCd) {
        String jobCdNm;
        switch (jobCd) {
            case "6":
                jobCdNm = "경영지원팀";
                break;
            case "7":
                jobCdNm = "공고팀";
                break;
            default:
                jobCdNm = "개발팀";
                break;
        }
        return jobCdNm;
    }

    public static String jobNmToCd(String jobCdNm) {
        String jobCd;
        switch (jobCdNm) {
            case "경영지원팀":
                jobCd = "6";
                break;
            case "공고팀":
                jobCd = "7";
                break;
            default:
                jobCd = "8";
                break;
        }
        return jobCd;
    }
}
