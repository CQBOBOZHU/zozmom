/*
 * 文件名：MD5Util.java
 * 版权：Copyright (c) 2014, doit.com All Rights Reserved. 
 * 描述：TODO
 * 修改人：Estn
 * 修改时间：2014-3-1 下午3:41:10
 */
package com.zozmom.util;

import java.security.MessageDigest;

/**
 * @author Estn
 * @description MD5加密工具
 * @date 2014-3-1
 */
public class MD5Utils {

	/**
	 * MD5普通加密
	 * 
	 * @param rawPass
	 *            明文
	 * @return
	 */
	public static String encrypt(String rawPass) {
		return encrypt(rawPass, null);
	}

	/**
	 * * MD5盐值加密
	 * 
	 * @param rawPass
	 *            明文
	 * @param salt
	 *            盐值
	 * @return
	 */
	public static String encrypt(String rawPass, Object salt) {
		String saltedPassword = mergePasswordAndSalt(rawPass, salt);
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] digest = messageDigest.digest(saltedPassword
					.getBytes("UTF-8"));
			return new String(encode(digest));
		} catch (Exception e) {
			return rawPass;
		}
	}

	/**
	 * 拼接密码与盐值
	 * 
	 * @param password
	 * @param salt
	 * @return 密码{盐值}
	 */
	private static String mergePasswordAndSalt(String password, Object salt) {
		if (salt == null || "".equals(salt.toString().trim())) {
			return password;
		}
		return password + "{" + salt.toString() + "}";
	}

	/**
	 * encrypt
	 * 
	 * @param bytes
	 * @return
	 */
	private static char[] encode(byte[] bytes) {
		char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };
		int nBytes = bytes.length;
		char[] result = new char[2 * nBytes];
		int j = 0;
		for (byte aByte : bytes) {
			result[j++] = HEX[(0xF0 & aByte) >>> 4];
			result[j++] = HEX[(0x0F & aByte)];
		}
		return result;
	}

	/**
	 * 校验明文加验证MD5后的值是否等于密文
	 * 
	 * @param plainText
	 *            明文
	 * @param salt
	 *            盐值
	 * @param cipherText
	 *            密文
	 * @return
	 */
	public static boolean verify(String plainText, String salt,
			String cipherText) {
		return (MD5Utils.encrypt(plainText, salt)).equals(cipherText);
	}

	public static String changText(String pd) {
		return pd + "@zhumeng";
	}

	public static String epPwd(String pd) {
		pd = changText(pd);
		return encrypt(pd);
	}

}