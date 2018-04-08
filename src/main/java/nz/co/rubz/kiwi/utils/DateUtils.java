package nz.co.rubz.kiwi.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class DateUtils {


	/**
     * 常用的时间格式：yyyy-MM-dd HH:mm:ss
     */
//    public static final String REGULAR_DATE_REGX = "yyyy-MM-dd HH:mm:ss";
    private static final Pattern UTC_DATE_PATTERN=Pattern.compile("^2[0-9]{3}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$");
    private static final Pattern lOCALE_DATE_PATTERN=Pattern.compile("^2[0-9]{3}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}[+,-][0-9]{2}:[0-9]{2}$");

	/**
     *
	 * 返回统计开通用户数的缺省起始时间,上一月的开始
	 *
	 * @return
	 */
	public static String getDefaultStatBeginDate() {
		Calendar cl = Calendar.getInstance();
		cl.roll(Calendar.MONTH, -1);
		int year = cl.get(Calendar.YEAR);
		int month = cl.get(Calendar.MONTH);

		cl.clear();
		if (month == 11) {
			cl.set(year - 1, month, 1);
		} else {
			cl.set(year, month, 1);
		}
		Date date = cl.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	/**
	 * 返回统计开通用户数的缺省结束时间,上一月的结束
	 *
	 * @return
	 */
	public static String getDefaultStatEndDate() {
		Calendar cl = Calendar.getInstance();
		cl.roll(Calendar.MONTH, -1);
		int year = cl.get(Calendar.YEAR);
		int month = cl.get(Calendar.MONTH);
		int eday = cl.getActualMaximum(Calendar.DAY_OF_MONTH);
		cl.clear();
		if (month == 11) {
			cl.set(year - 1, month, eday);
		} else {
			cl.set(year, month, eday);
		}
		Date date = cl.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	public static String getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		return df.format(cl.getTime());
	}

	/**
	 * 按指定的格式返回时间<br>
	 * 注：此处未做时间格式参数的校验
	 *
	 * @param format
	 *            ：时间格式
	 * @return ：需要的时间格式字符串
	 *
	 */
	public static String getCurrentDate(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Calendar cl = Calendar.getInstance();
		return df.format(cl.getTime());
	}
	
	public static String getCurrentDate(Calendar c1, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(c1.getTime());
	}

	public static String getSiRespTimeStamp() { // 获取格式为 YYYYMMDDHHmmssnnnn
												// 的日期数据
		Date date = Calendar.getInstance().getTime();
		// SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssnnnn");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(date) + "0000";

	}

	public static String getGdadcTimeStamp() {
		Date date = Calendar.getInstance().getTime();
		// SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssnnnn");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(date);
	}

	public static String getGdadcTimeStampYMD() {
		Date date = Calendar.getInstance().getTime();
		// SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssnnnn");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}

	public static Date getDateByPatternString(String dateStr, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date reqDate = null;
		try {
			reqDate = sdf.parse(dateStr);
		} catch (Exception e) {
			reqDate = null;
		}
		return reqDate;
	}

    /**
	 * 订购中注册接口上传终端发送开通短信时间，使用标准时间格式
	 *
	 * @return
	 */
    public static Date getLocalDate(String src){
        java.util.Calendar cal = java.util.Calendar.getInstance();
        if(UTC_DATE_PATTERN.matcher(src).matches()){
            //UTC标准格式，需要转换成本地时间
            Date utc_date = getDateByPatternString(src,"yyyy-MM-dd'T'HH:mm:ss'Z'");
            if(utc_date != null){
                cal.setTime(utc_date);
                int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
                int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
                cal.add(java.util.Calendar.MILLISECOND,zoneOffset + dstOffset);
                return new Date(cal.getTimeInMillis());
            }
        }else if(lOCALE_DATE_PATTERN.matcher(src).matches()){
            //本地时间格式，将后面的时区信息去掉
            int pos = src.indexOf("+");
            if(pos == -1) pos = src.indexOf("-");
            if(pos == -1) return null;
            String stemp = src.substring(0,pos);
            return getDateByPatternString(stemp,"yyyy-MM-dd'T'HH:mm:ss");
        }
        return null;
    }

    public static String convertDate2Str(Date date,String format){
        if(date==null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


	/**
	 * 按照表达式格式化日期为字符串
	 *
	 * @param regx
	 * @param date
	 * @return
	 */
	public static synchronized String format(String regx, Date date) {
		// 实例化SimpleDateFormat对象，并使用regx做为格式
		SimpleDateFormat dateformat = new SimpleDateFormat(regx);

		// 格式化给定的日期，并返回结果给formatedDate
		String formatedDate = dateformat.format(date);

		// 返回结果
		return formatedDate;
	}

    public static boolean checkBeforeDate(Date beforeDate,Date afterDate){
        return beforeDate.before(afterDate);
    }

	public static void main(String [] args){
//	    "2011-04-01 12:12:12"
//	    System.out.println(DateUtils.format("yyyyMMddHHmmss", null));
        System.out.println(DateUtils.convertDate2Str(new Date(),"yyyyMMdd"));
	}
}
