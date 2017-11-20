package com.merise.net.heart.utils;

import java.security.MessageDigest;

/**
 * @version 1.0
 * @since  
 * @Date 2014 2014-00-00 00:00:00
 * @Copyright (c) 2010-2014, xy All Rights Reserved.
 */
public class MD5 {

	/**
	 * Md5 32λ or 16λ ����
	 * 
	 * @return 32λ����
	 */
	public static String md5Encode(String inStr) throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

}
