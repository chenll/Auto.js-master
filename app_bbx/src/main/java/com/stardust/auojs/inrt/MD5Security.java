package com.stardust.auojs.inrt;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Security {
	public static String encryptByMD5(String str, String key) {

		String encryptStr = null;
		if (str != null && key != null) {

			byte[] src = (str + key).getBytes();
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9', 'a', 'b', 'c', 'd', 'e', 'f' };

			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("MD5");
				md.update(src);
				byte tmp[] = md.digest();

				char chr[] = new char[16 * 2];
				int k = 0;
				for (int i = 0; i < 16; i++) {
					byte byte0 = tmp[i];
					chr[k++] = hexDigits[byte0 >>> 4 & 0xf];
					chr[k++] = hexDigits[byte0 & 0xf];
				}
				encryptStr = new String(chr);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return encryptStr;
	}
	public static String EncoderByMd5(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md5.update(str.getBytes());
		String strDes = bytes2Hex(md5.digest()); // to HexString
		return strDes;
	}
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;

		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	private static String desKey = "4567";

	public final static byte[] doit(byte[] bytes) {
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(bytes);

			return mdTemp.digest();
		} catch (Exception e) {
			return null;
		}
	}

	public final static String doit(String s) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			byte[] strTemp = s.getBytes("UTF-8");
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);

			// MessageDigest.getInstance(algorithm)
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;

			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}

			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getMD5(String sStr) {
		String sReturnCode = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sStr.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer sb = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					sb.append("0");
				}
				sb.append(Integer.toHexString(i));
			}

			sReturnCode = sb.toString();
		} catch (Exception ex) {

		}
		return sReturnCode;
	}

	public static String getDesKey() {
		return desKey;
	}

	public static void setDesKey(String key) {
		MD5Security.desKey = key;
	}



	/**
	 * 获取单个文件的MD5值！

	 * @param file
	 * @return
	 */

	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
}
