package com.status102.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import java.io.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

object ToolK {
	private const val begin = "-----BEGIN PUBLIC KEY-----\n"
	private const val end = "\n-----END PUBLIC KEY-----\n"

	/**
	 * 产生一个随机数
	 *
	 * @param num 随机数的位数
	 * @return
	 */
	fun getNewNumber(num: Int): String {
		val rnd = Random()
		var re = ""
		var r = 0
		for (i in 0 until num) {
			if (num > 1 && i == num - 1) {
				r = rnd.nextInt(9) + 1
				re += r
			} else {
				r = rnd.nextInt(10)
				re += r
			}
		}
		return re
	}

	/**
	 * 产生一个指定范围的随机数
	 *
	 * @param from
	 * @param to
	 * @return
	 */
	fun getNewRandomInt(from: Int, to: Int): Int {
		var num = (to - from).toString().length
		val r = Math.pow(10.0, num.toDouble())
		num = ((getNewNumber(num) + ".0").toDouble() / r * (to - from) + from).toInt()
		return num
	}

	@Throws(IOException::class)
	fun streamToString(`is`: InputStream): String {
		val baos = ByteArrayOutputStream()
		val buffer = ByteArray(1024)
		var len = 0
		while (`is`.read(buffer).also { len = it } != -1) {
			baos.write(buffer, 0, len)
		}
		baos.close()
		`is`.close()
		val byteArray = baos.toByteArray()
		return String(byteArray)
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
	@Throws(FileNotFoundException::class)
	fun readFile(path: String): String {
		val file = File(path)
		return if (!file.exists()) {
			throw FileNotFoundException("“$path”不存在")
		} else {
			readFile(file)
		}
	}

	fun readFile(file: File?): String {
		val str = StringBuilder()
		var cache: String?
		try {
			InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8).use { isReader ->
				BufferedReader(isReader).use { reader ->
					while (reader.readLine().also { cache = it } != null) {
						str.append(cache)
					}
				}
			}
		} catch (e: IOException) {
			e.printStackTrace()
		}
		return str.toString()
	}

	@Throws(FileNotFoundException::class)
	fun writeFile(path: String?, msg: String, append: Boolean) {
		//File fileDir = new File(path.substring(0, path.lastIndexOf("\\")));
		val file = File(path)
		val fileDir = file.parentFile
		if (!fileDir.exists()) {
			fileDir.mkdirs()
		}
		try {
			OutputStreamWriter(FileOutputStream(file, append), Charset.forName("utf-8")).use { osWriter ->
				BufferedWriter(osWriter).use { writer ->
					if (!file.exists()) {
						file.createNewFile()
					}
					writer.write(msg.replace("\n", "\r\n"))
					writer.flush()
				}
			}
		} catch (e: FileNotFoundException) {
			throw e
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	@Throws(
		NoSuchPaddingException::class,
		NoSuchAlgorithmException::class,
		InvalidKeySpecException::class,
		InvalidKeyException::class,
		BadPaddingException::class,
		IllegalBlockSizeException::class
	)
	fun RSAEncrypt(str: String, mKey: String): String {
		val key =
			mKey.replace(begin, "").replace(end, "").replace("\n", "")
		val generatePublic: Key = KeyFactory.getInstance("RSA")
			.generatePublic(X509EncodedKeySpec(Base64.getDecoder().decode(key)))
		val instance = Cipher.getInstance("RSA/ECB/PKCS1PADDING")
		instance.init(Cipher.ENCRYPT_MODE, generatePublic)
		return Base64.getEncoder().encodeToString(instance.doFinal(str.toByteArray()))
		//throw new mException("RSA加密错误，\nStr=" + str + "\nKey=" + key + "\ne=" + Tool.getStackTraceInfo(e));

		// 某些手机缺少Java库不支持java.util.base64
		/**
		 * try {
		 * Key generatePublic = KeyFactory.getInstance("RSA").generatePublic(
		 * new X509EncodedKeySpec(android.util.Base64.decode(key.getBytes(),
		 * android.util.Base64.DEFAULT)));
		 * Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		 * instance.init(Cipher.ENCRYPT_MODE, generatePublic);
		 * return android.util.Base64.encodeToString(
		 * instance.doFinal(str.getBytes()), android.util.Base64.DEFAULT);
		 * } catch (Exception e2) {
		 * e2.printStackTrace();
		 * throw new mException("RSA加密错误，\nStr=" + str + "\nKey=" + key + "\ne=" +
		 * Tool.getStackTraceInfo(e2));
		 * }
		 */
	}

	/**
	 * 返回格式化JSON字符串。
	 *
	 * @param json 未格式化的JSON字符串。
	 * @return 格式化的JSON字符串。
	 */
	fun formatJson(json: JsonElement?): String {
		val gson =
			GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create()
		return gson.toJson(json)
	}

	fun formatUrlSaveJson(json: JsonElement?): String {
		val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
		return gson.toJson(json)
	}

	fun getStackTraceInfo(e: Throwable): String {
		var sw: StringWriter? = null
		var pw: PrintWriter? = null
		return try {
			sw = StringWriter()
			pw = PrintWriter(sw)
			e.printStackTrace(pw) // 将出错的栈信息输出到printWriter中
			pw.flush()
			sw.flush()
			sw.toString()
		} catch (ex: Exception) {
			"发生错误"
		} finally {
			if (sw != null) {
				try {
					sw.close()
				} catch (e1: IOException) {
					e1.printStackTrace()
				}
			}
			pw?.close()
		}
	}

	// 中文转Unicode
	fun encodeUnicode(gbString: String): String {
		// gbString = "测试"
		val utfBytes = gbString.toCharArray() // utfBytes = [测, 试]
		var unicodeBytes = ""
		for (byteIndex in utfBytes.indices) {
			var hexB = Integer.toHexString(utfBytes[byteIndex].toInt()) // 转换为16进制整型字符串
			if (hexB.length <= 2) {
				hexB = "00$hexB"
			}
			unicodeBytes = "$unicodeBytes\\u$hexB"
		}
		return unicodeBytes
	}

	// Unicode转中文
	fun decodeUnicode(unicodeStr: String?): String? {
		if (unicodeStr == null) {
			return null
		}
		val retBuf = StringBuffer()
		val maxLoop = unicodeStr.length
		var i = 0
		while (i < maxLoop) {
			if (unicodeStr[i] == '\\') {
				if (i < maxLoop - 5 && (unicodeStr[i + 1] == 'u' || unicodeStr[i + 1] == 'U')) try {
					retBuf.append(unicodeStr.substring(i + 2, i + 6).toInt(16).toChar())
					i += 5
				} catch (localNumberFormatException: NumberFormatException) {
					retBuf.append(unicodeStr[i])
				} else retBuf.append(unicodeStr[i])
			} else {
				retBuf.append(unicodeStr[i])
			}
			i++
		}
		return retBuf.toString()
	}
}