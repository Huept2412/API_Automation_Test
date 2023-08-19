package com.api.auto.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import io.restassured.path.json.JsonPath;

public class PropertiesFileUtils {
	// Đường dẫn đến properties files trong folder configuration
	private static String CONFIG_PATH = "./configuration/configs.properties";
	private static String TOKEN_PATH = "./configuration/token.properties";

	// 1 Lấy ra giá trị property từ config file bất kỳ theo key.
	public static String getProperty(String key) {
		Properties properties = new Properties();
		String value = null;
		FileInputStream fileInputStream = null;
		// bat exception
		try {
			fileInputStream = new FileInputStream(CONFIG_PATH);
			// input code here…….
			properties.load(fileInputStream);
			value = properties.getProperty(key);

		} catch (Exception ex) {
			System.out.println("Xảy ra lỗi khi đọc giá trị của  " + key);
			ex.printStackTrace();
		} finally {
			// luôn nhảy vào đây dù có xảy ra exception hay không.
			// thực hiện đóng luồng đọc
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	// 2.save token vào file token.properties với key là “token”
	public static void saveToken(String token) {
		// Khái báo properties, biến cần thiết
		Properties properties = new Properties();
		properties.setProperty("token", token);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(TOKEN_PATH);

			properties.store(fileOutputStream, "Set new value in properties");
			System.out.println("Set new value in file properties success.");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Luôn nhảy vào đây dù có xảy ra exception hay không.
			// thực hiện đóng luồng
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 3 lấy ra token
	public static String getToken(String key) {
		// Khai báo properties, biến, value
		Properties pro = new Properties();
		FileInputStream fileInputStream = null;
		String value = null;
		// bat exception
		try {
			fileInputStream = new FileInputStream(TOKEN_PATH);
			pro.load(fileInputStream);
			value = pro.getProperty(key);
		} catch (Exception ex) {
			System.out.println("Xảy ra lỗi khi đọc giá trị token  ");
			ex.printStackTrace();
		} finally {
			// Luôn nhảy vào đây dù có xảy ra exception hay không.
			// thực hiện đóng luồng
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return value;

	}

}
