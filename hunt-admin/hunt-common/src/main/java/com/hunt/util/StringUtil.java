package com.hunt.util;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: ouyangan
 * @Date : 2016/10/7 
 */
public class StringUtil {
    private static Logger log = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 
     * @param str
     * @return -1, 输入为 null 1,匹配正确 0 匹配错误
     */
    public static Integer isNumber(String str) {
    	if(!StringUtils.hasLength(str))return -1;
   		String strRegex="\\d+$";
   		Pattern compile = Pattern.compile(strRegex);
   		Matcher matcher = compile.matcher(str);
   		return matcher.matches()==true?1:0; 
    }

    /**
     * 生成密码
     *
     * @param password 密码
     * @param salt     密码盐
     * @return
     */
    public static String createPassword(String password, String salt, int hashIterations) {
        Md5Hash md5Hash = new Md5Hash(password.trim(), salt, hashIterations);
        return md5Hash.toString();
    }
    
    public static void main(String[] args) {  //  E10ADC3949BA59ABBE56E057F20F883E
		String createPassword = createPassword("111111", "109e0d4a996341439bb74898892a1ece", 2);
		System.out.println("cratePassword--->"+createPassword.length()+"-->"+createPassword); 
	}

    public static String exceptionDetail(Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        return "\n" + writer.toString();
    }

    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    public static String camelToUnderline(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toUpperCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s);
            }
        }
        return result.toString().toLowerCase();
    }
    
    /**
     * 获得查询范围
     * @param sTimeType 查询类型
     * @return	1, 查询3天内	2, 查询7天内	3,查询15天内	4,	1个月内  5, 3个月内   6, 6个月 
     */
    public static String getSRange(String sTimeType) {
    	String dayMonth="";
		switch (sTimeType) {
			case "1":		//  三天
				dayMonth="3";
				break;
			case "2":		//  七天
				dayMonth="7";
				break;
			case "3":		//	半月
				dayMonth="15";
				break;
			case "4":		// 一个月
				dayMonth="1";
				break;
			case "5":		// 近三月
				dayMonth="3";
				break;
			case "6":		// 近半年
				dayMonth="6";
				break;
		}
		return dayMonth;
    }
    
    /**
     * 判断是否是中文，大小写，数字，return true 乱码其他字符 return false
     * @param string
     * @return
     */
  public static boolean isStrCorrect(String string) {
		String machex="^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";
//		String string="ʤ�����͹���";
//		String string="safd432s找那个243�";
		return string.matches(machex);
  }
  
  /**
   * 判断是否是大小写，数字，return true 乱码其他字符 return false
   * @param string
   * @return
   */
public static boolean isNoChCorrect(String string) {
		String machex="^[A-Za-z0-9_]+$";
//		String string="ʤ�����͹���";
//		String string="safd432s找那个243�";
		return string.matches(machex);
}
  
  public static boolean isnull(String str) {
	  return str==null||str.isEmpty()||str.length()==0||str.equals("")||str.equals("null");
  }
  
  @Test
  public void testStr() {
	  boolean matcher = isMatcher("张m12p34a", "张m12p34a");
	  System.out.println("matcher"+matcher);
	  
  }
  
  /**
   * 匹配满足条件  大小写，数字，下划线
   * @param str1
   * @param str2
   * @return
   */
  public static boolean isMatcher(String str1,String str2) {
	  if(isnull(str1)||isnull(str2)||str1.length()<4) {
		  return false;
	  }
	  if(isNoChCorrect(str1)) {
		  return str1.equals(str2);
	  }
	  return false;
  }
  
  /**
   * 获得这个月的第一天毫秒数
   * @return
   */
  public static Long getDayFirstMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis(); 
  }
   
  /**
   * 获取这一周的第一天
   * @return
   */
  public static Long getDayFirstWeek() {
//		Calendar calendar = Calendar.getInstance();
		Calendar calendar=new GregorianCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
//		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		
//		int firstDayOfWeek = calendar.getFirstDayOfWeek();  
//		calendar.set(Calendar.DAY_OF_WEEK, -1); 
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
//		calendar.add(Calendar.DAY_OF_WEEK, firstDayOfWeek);
		System.out.println("getDayFirstWeek-->"+calendar.getTimeInMillis()); 
		return calendar.getTimeInMillis(); 
  }
  
  
  @org.junit.Test
  public void test() {
	  Long dayFirstWeek = getDayFirstWeek();
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  System.out.println(sdf.format(new Date(dayFirstWeek)));
//	 String createPassword = createPassword("123", "", 0);
//	 System.out.println("create-->"+createPassword);
  }  
}
