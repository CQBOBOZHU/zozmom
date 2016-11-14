package com.zozmom.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tools {
	/**
	 * 截取list中间的某段数据
	 * 
	 * @param list
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> getList(List<String> list, int start, int end) {
		if (list == null) {
			return null;
		}
		List<String> rl = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if (start < end) {
				if (list.size() < start) {
					return null;
				}
				if (start <= i && i < end) {
					rl.add(list.get(i));
				}
			} else {
				if (end <= i && i < start) {
					rl.add(list.get(i));
				}
			}
		}
		return rl;
	}
	
	public static String getRandomString() { //length表示生成字符串的长度  
	    String base = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < 14; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }  
	
	
	/**
     * 校验银行卡卡号
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
             char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
             if(bit == 'N'){
                 return false;
             }
             return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId){
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;           
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
}
