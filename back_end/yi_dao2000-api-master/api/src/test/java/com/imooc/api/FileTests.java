package com.imooc.api;

import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 测试文件
 *
 * @author 尹冬飞
 * @create 2017-10-12 9:45
 */
public class FileTests {

	@Test
	public void FileTest2() {
		String fileName = "about.json";
		String a = getJsonContentInStatic(fileName);
		System.out.println(a);
	}

	/**
	 * 获取resouce下面的文件的内容 比如"classpath:\\static\\about.json"
	 *
	 * @param fileName 文件名称 about.json
	 * @return 文件内容
	 */
	private String getJsonContentInStatic(String fileName) {
		String url = "classpath:\\static\\" + fileName;
		return getFileContent(url);
	}

	/**
	 * 获取resouce下面的文件的内容
	 *
	 * @param url 比如"classpath:\\static\\about.json"
	 * @return 文件内容
	 */
	private String getFileContent(String url) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(url);
		InputStream inputStream = null;
		StringBuilder stringBuilder = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = resource.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			stringBuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return stringBuilder.toString();
	}
}
