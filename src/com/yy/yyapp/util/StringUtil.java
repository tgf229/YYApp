package com.yy.yyapp.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil
{
    
    private static final byte[] CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
        'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
        'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9'};
    
    public static boolean isEmpty(String str)
    {
        return str == null || str.trim().length() == 0;
    }
    
    public static boolean isNotEmpty(String str)
    {
        return !StringUtil.isEmpty(str);
    }
    
    public static boolean isBlank(String str)
    {
        int strLen;
        if (str == null || (strLen = str.length()) == 0)
        {
            return true;
        }
        for (int i = 0; i < strLen; i++)
        {
            if ((Character.isWhitespace(str.charAt(i)) == false))
            {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNotBlank(String str)
    {
        return !StringUtil.isBlank(str);
    }
    
    public static boolean isEquals(String str1, String str2)
    {
        if (str1 == str2)
        {
            return true;
        }
        if (str1 != null)
        {
            return str1.equals(str2);
        }
        else
        {
            return false;
        }
    }
    
    public static boolean isNumeric(String str)
    {
        if (str == null)
        {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches())
        {
            return false;
        }
        return true;
    }
    
    public static int getLengthByByte(String str)
    {
        int length = 0;
        if (str == null || str.length() == 0)
        {
            return length;
        }
        
        for (int i = 0; i < str.length(); i++)
        {
            int ascii = Character.codePointAt(str, i);
            if (ascii >= 0 && ascii <= 255)
            {
                length++;
            }
            else
            {
                length += 2;
            }
        }
        return length;
    }
    
    public static String subStringByByte(String str, int startPos, int length)
    {
        if (str == null || str.length() == 0)
        {
            return "";
        }
        
        StringBuffer sb = new StringBuffer();
        int byteLen = 0;
        for (int i = 0; i < str.length(); i++)
        {
            int ascii = Character.codePointAt(str, i);
            if (ascii >= 0 && ascii <= 255)
            {
                byteLen++;
            }
            else
            {
                byteLen += 2;
            }
            if (byteLen >= startPos + 1 && byteLen <= length)
            {
                sb.append(str.charAt(i));
            }
            else
            {
                break;
            }
        }
        return sb.toString();
    }
    
    public static String getRandomString(int length)
    {
        Random random = new Random(System.currentTimeMillis());
        byte[] randomBytes = new byte[length];
        for (int i = 0; i < length; i++)
        {
            randomBytes[i] = CHARS[random.nextInt(CHARS.length)];
        }
        return new String(randomBytes);
    }
    
    public static String join(Object[] array, String separator)
    {
        if (array == null)
        {
            return null;
        }
        return join(array, separator, 0, array.length);
    }
    
    public static String join(Object[] array, String separator, int startIndex, int endIndex)
    {
        if (array == null)
        {
            return null;
        }
        if (separator == null)
        {
            separator = "";
        }
        
        int noOfItems = (endIndex - startIndex);
        if (noOfItems <= 0)
        {
            return "";
        }
        
        StringBuilder buf = new StringBuilder(noOfItems * 16);
        
        for (int i = startIndex; i < endIndex; i++)
        {
            if (i > startIndex)
            {
                buf.append(separator);
            }
            if (array[i] != null)
            {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
    
    /**
     * 截取价钱.00情况
     * 如20.00 --> 20
     * @param str
     * @return
     */
    public static String substringPrice(String str)
    {
        int count = 0;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (c == '.')
            {
                count = i;
            }
        }
        if (str.substring(count).equals(".00"))
        {
            return str.substring(0, count);
        }
        else
        {
            return str;
        }
    }
    
    /**
     * 卡类型限制说明
     * 
     * @param str
     * @return
     */
    public static String cardLimitExplain(String s)
    {
        
        StringBuffer sbDZ = new StringBuffer();
        StringBuffer sbST = new StringBuffer();
        boolean flagDZ = false;
        boolean flagST = false;
        String strDZ = "";
        String strST = "";
        String str = null;

        if(null == s || "".equals(s))
        {
            //结果集为空 不拼装
            return "";
        }
        else
        {
            if(s.contains("0"))
            {
                sbDZ.append("、电子普卡");
                flagDZ= true;
            }
            if(s.contains("2"))
            {
                sbDZ.append("、电子金卡");
                flagDZ= true;
            }
            if(s.contains("4"))
            {
                sbDZ.append("、电子钻石卡");
                flagDZ= true;
            }
            if(s.contains("1"))
            {
                sbST.append("、实体普卡");
                flagST= true;
            }
            if(s.contains("3"))
            {
                sbST.append("、实体金卡");
                flagST= true;
            }
            if(s.contains("5"))
            {
                sbST.append("、实体钻石卡");
                flagST= true;
            }
            if(flagDZ)
            {
                strDZ = "申请"+sbDZ.toString();
            }
            if(flagST)
            {
                if(flagDZ)
                {
                    strST = "或";
                }
                strST = strST+"绑定"+sbST.toString();
            }
            
            strDZ = strDZ.replaceFirst("、", "");
            strST = strST.replaceFirst("、", "");
            str = strDZ+strST+"的用户都可享受此次活动";
            return str;
        }
    }
}
