package com.eastcom.baseframe.web.common.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 该类用于完成下载操作，把输入流写到response的输出流中， 然后response把文件返回到请求页面
 * 
 * Title: jksupport_selfhelp <br>
 * Description: <br>
 * Copyright: eastcom Copyright (C) 2009 <br>
 * 
 * @author <a href="mailto:chenzd@eastcom-sw.com">czd</a><br>
 * @e-mail:chenzd@eastcom-sw.com <br>
 * @version 1.0 <br>
 * @creatdate 2014-6-23 下午5:44:16 <br>
 *
 */

public class DownloadUtil {

	protected static final Logger logger = Logger.getLogger(DownloadUtil.class);

	private static final Map<String, String> application_Map = new HashMap<String, String>();

	private static final String default_application = "application/octet-stream";

	static {
		application_Map.put("xls", "application/vnd.ms-excel");
		application_Map.put("xlsx", "application/vnd.ms-excel");
		application_Map.put("doc", "application/msword");
		application_Map.put("docx", "application/msword");
		application_Map.put("pdf", "application/pdf");
		application_Map.put("ppt", "application/vnd.ms-powerpoint");
		application_Map.put("pptx", "application/vnd.ms-powerpoint");
		application_Map.put("rar", "application/octet-stream");
		application_Map.put("zip", "application/zip");
		application_Map.put("txt", "application/txt");
		
	}

	private DownloadUtil() {
	}

	/**
	 * 第一个参数是文件在硬盘的路径，第二个参数是下载显示的文件名，第三个参数是response
	 * 
	 * @param localpath
	 * @param show
	 * @param resp
	 */
	public static void downloadFile(String localpath, String showname,
			HttpServletResponse resp) {

		// 下载时候显示的文件名
		String downloadname = null;
		FileInputStream fis = null;
		OutputStream os = null;
		try {
			// downloadname=new String(oriname.getBytes("gb2312"), "ISO8859-1");
			// 把下载显示的文件名编码设置为utf-8
			downloadname = java.net.URLEncoder.encode(showname, "UTF-8");
			resp.reset();

			String suffix = StringUtils.lowerCase(showname.substring(showname
					.lastIndexOf('.') + 1));
			String application = application_Map.containsKey(suffix) ? application_Map
					.get(suffix) : default_application;

			resp.setContentType(application + "; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename="
					+ downloadname);
			
			fis = new FileInputStream(localpath);
			os = resp.getOutputStream();
			
			byte[] buffers = new byte[1024];
			int lenth = 0;
			// 把文件流输出到response中
			while ((lenth = fis.read(buffers)) > 0) {
				os.write(buffers, 0, lenth);
			}
			os.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}
	
	/**
	 * 第一个参数是文件输入流，第二个参数是下载显示的文件名，第三个参数是response
	 * 
	 * @param in
	 * @param show
	 * @param resp
	 */
	public static void downloadFile(InputStream in, String showname,
			HttpServletResponse resp) {

		// 下载时候显示的文件名
		String downloadname = null;
		OutputStream os = null;
		try {
			// downloadname=new String(oriname.getBytes("gb2312"), "ISO8859-1");
			// 把下载显示的文件名编码设置为utf-8
			downloadname = java.net.URLEncoder.encode(showname, "UTF-8");
			resp.reset();

			String suffix = StringUtils.lowerCase(showname.substring(showname
					.lastIndexOf('.') + 1));
			String application = application_Map.containsKey(suffix) ? application_Map
					.get(suffix) : default_application;

			resp.setContentType(application + "; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename="
					+ downloadname);
			
			os = resp.getOutputStream();
			
			byte[] buffers = new byte[1024];
			int lenth = 0;
			// 把文件流输出到response中
			while ((lenth = in.read(buffers)) > 0) {
				os.write(buffers, 0, lenth);
			}
			os.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	/**
	 * 第一个参数是文件输入流，第二个参数是临时文件名，第三个参数是临时文件输出流
	 * 
	 * @param in
	 * @param show
	 * @param resp
	 */
	public static void downloadTempFile(InputStream in, String showname,
			FileOutputStream fos ) {

		// 下载时候显示的文件名
		String downloadname = null;
		//OutputStream os = null;
		try {
			// downloadname=new String(oriname.getBytes("gb2312"), "ISO8859-1");
			// 把下载显示的文件名编码设置为utf-8
			downloadname = java.net.URLEncoder.encode(showname, "UTF-8");
			//resp.reset();

			String suffix = StringUtils.lowerCase(showname.substring(showname
					.lastIndexOf('.') + 1));
			String application = application_Map.containsKey(suffix) ? application_Map
					.get(suffix) : default_application;

			/*resp.setContentType(application + "; charset=utf-8");
			resp.addHeader("Content-Disposition", "attachment; filename="
					+ downloadname);
			
			os = resp.getOutputStream();*/
			
			byte[] buffers = new byte[1024];
			int lenth = 0;
			// 把文件流输出到response中
			while ((lenth = in.read(buffers)) > 0) {
				fos.write(buffers, 0, lenth);
			}
			fos.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
}
