package com.status102.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

public class ToolJ {
	private static final String begin = "-----BEGIN PUBLIC KEY-----\n";
	private static final String end = "\n-----END PUBLIC KEY-----\n";

	/**
	 * 产生一个随机数
	 *
	 * @param num 随机数的位数
	 * @return
	 */
	public static String getNewNumber(int num) {
		Random rnd = new Random();
		String re = "";
		int r = 0;

		for (int i = 0; i < num; i++) {
			if (num > 1 && i == (num - 1)) {
				r = rnd.nextInt(9) + 1;
				re += r;
			} else {
				r = rnd.nextInt(10);
				re += r;
			}
		}
		return re;
	}

	/**
	 * 产生一个指定范围的随机数
	 *
	 * @param from
	 * @param to
	 * @return
	 */
	public static int getNewRandomInt(int from, int to) {
		int num = String.valueOf(to - from).length();

		double r = Math.pow(10, num);
		num = (int) ((Double.valueOf(getNewNumber(num) + ".0") / r) * (to - from) + from);
		return num;
	}

	// 获取md5
	public static String getMD5(String content) throws NoSuchAlgorithmException {
		MessageDigest digest;
		digest = MessageDigest.getInstance("MD5");
		digest.update(content.getBytes());

		StringBuilder builder = new StringBuilder();
		for (byte b : digest.digest()) {
			builder.append(Integer.toHexString((b >> 4) & 0xf));
			builder.append(Integer.toHexString(b & 0xf));
		}
		return builder.toString().toLowerCase();
	}

	public static String streamToString(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		baos.close();
		is.close();
		byte[] byteArray = baos.toByteArray();
		return new String(byteArray);
	}

	/**
	 * 读取TXT文件
	 */
	/*
	 * public static String readFile(String path) throws FileNotFoundException { //
	 * String pathname = path; // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件 //
	 * 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw; // 不关闭文件会导致资源的泄露，读写文件都同理 //
	 * Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/
	 * 12665271 StringBuilder str = new StringBuilder(); BufferedReader br = null;
	 * try { FileReader reader = new FileReader(path); br = new
	 * BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
	 *
	 * String line; // 网友推荐更加简洁的写法 while ((line = br.readLine()) != null) { //
	 * 一次读入一行数据 str.append(line).append("\n"); } } catch (FileNotFoundException e) {
	 * throw e; } catch (IOException e) { e.printStackTrace(); } finally { try { if
	 * (br != null) br.close(); } catch (IOException e) { e.printStackTrace(); } }
	 * return str.toString(); }
	 *
	 * public static void writeFile(String path, String msg, boolean append) throws
	 * FileNotFoundException { // System.out.println(path); //
	 * System.out.println(path.substring(0, path.lastIndexOf("/"))); msg =
	 * msg.replace("\n", "\r\n"); BufferedWriter out = null; try { File fileDir =
	 * new File(path.substring(0, path.lastIndexOf("/"))); if (!fileDir.exists())
	 * fileDir.mkdirs(); File file = new File(path); //
	 * 相对路径，如果没有则要建立一个新的output.txt文件 FileWriter writer = null; if (append) { if
	 * (!file.exists()) { file.createNewFile();// 如果文件不存在，就创建该文件 writer = new
	 * FileWriter(file);// 首次写入获取 } else { // 如果文件已存在，那么就在文件末尾追加写入 writer = new
	 * FileWriter(file, true);// 这里构造方法多了一个参数true,表示在文件末尾追加写入 } } else {
	 * file.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖 writer = new FileWriter(file); }
	 * out = new BufferedWriter(writer); out.write(msg); // \r\n即为换行 out.flush(); //
	 * 把缓存区内容压入文件 } catch (FileNotFoundException e) { throw e; } catch (IOException
	 * e) { e.printStackTrace(); } finally { try { if (out != null) out.close(); }
	 * catch (IOException e) { e.printStackTrace(); } } }
	 */
	public static String readFile(String path) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("“" + path + "”不存在");
		} else {
			return readFile(file);
		}
	}

	public static String readFile(File file) {
		StringBuilder str = new StringBuilder();
		String cache;
		try (InputStreamReader isReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
		     BufferedReader reader = new BufferedReader(isReader);) {
			while ((cache = reader.readLine()) != null) {
				str.append(cache);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str.toString();
	}

	public static void writeFile(String path, String msg, boolean append) throws FileNotFoundException {
		//File fileDir = new File(path.substring(0, path.lastIndexOf("\\")));
		File file = new File(path);
		File fileDir = file.getParentFile();
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		try (OutputStreamWriter osWriter = new OutputStreamWriter(new FileOutputStream(file, append), Charset.forName("utf-8"));
		     BufferedWriter writer = new BufferedWriter(osWriter);) {
			if (!file.exists()) {
				file.createNewFile();
			}

			writer.write(msg.replace("\n", "\r\n"));
			writer.flush();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String RSAEncrypt(String str, String mKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		String key = mKey.replace(begin, "").replace(end, "").replace("\n", "");

		Key generatePublic = KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(key)));
		Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		instance.init(Cipher.ENCRYPT_MODE, generatePublic);
		return Base64.getEncoder().encodeToString(instance.doFinal(str.getBytes()));
		//throw new mException("RSA加密错误，\nStr=" + str + "\nKey=" + key + "\ne=" + Tool.getStackTraceInfo(e));

		// 某些手机缺少Java库不支持java.util.base64
		/**
		 * try {
		 * 			Key generatePublic = KeyFactory.getInstance("RSA").generatePublic(
		 * 					new X509EncodedKeySpec(android.util.Base64.decode(key.getBytes(),
		 * 							android.util.Base64.DEFAULT)));
		 * 			Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		 * 			instance.init(Cipher.ENCRYPT_MODE, generatePublic);
		 * 			return android.util.Base64.encodeToString(
		 * 					instance.doFinal(str.getBytes()), android.util.Base64.DEFAULT);
		 *                } catch (Exception e2) {
		 * 			e2.printStackTrace();
		 * 			throw new mException("RSA加密错误，\nStr=" + str + "\nKey=" + key + "\ne=" +
		 * 					Tool.getStackTraceInfo(e2));
		 *        }
		 */
	}

	/**
	 * 返回格式化JSON字符串。
	 *
	 * @param json 未格式化的JSON字符串。
	 * @return 格式化的JSON字符串。
	 */
	public static String formatJson(JsonElement json) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();
		return gson.toJson(json);
	}

	public static String formatUrlSaveJson(JsonElement json) {
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		return gson.toJson(json);
	}

	public static String getStackTraceInfo(Throwable e) {

		StringWriter sw = null;
		PrintWriter pw = null;

		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);// 将出错的栈信息输出到printWriter中
			pw.flush();
			sw.flush();

			return sw.toString();
		} catch (Exception ex) {

			return "发生错误";
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}

	}

	// 中文转Unicode
	public static String encodeUnicode(final String gbString) {
		// gbString = "测试"
		char[] utfBytes = gbString.toCharArray();// utfBytes = [测, 试]
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);// 转换为16进制整型字符串
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		return unicodeBytes;
	}

	// Unicode转中文
	public static String decodeUnicode(String unicodeStr) {
		if (unicodeStr == null) {
			return null;
		}
		StringBuffer retBuf = new StringBuffer();
		int maxLoop = unicodeStr.length();
		for (int i = 0; i < maxLoop; i++) {
			if (unicodeStr.charAt(i) == '\\') {
				if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
					try {
						retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
						i += 5;
					} catch (NumberFormatException localNumberFormatException) {
						retBuf.append(unicodeStr.charAt(i));
					}
				else
					retBuf.append(unicodeStr.charAt(i));
			} else {
				retBuf.append(unicodeStr.charAt(i));
			}
		}
		return retBuf.toString();
	}
}
