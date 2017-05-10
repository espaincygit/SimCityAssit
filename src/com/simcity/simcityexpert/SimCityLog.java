package com.simcity.simcityexpert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class SimCityLog {
	
	private SimCityLog(){
		File file = new File(MainActivity.root_folder);// ("/sdcard/Image/");

		if (!file.exists()) {
			file.mkdirs();//
		}
	}
	
	private static String logroot = MainActivity.root_folder + "log.txt";

	public static void rec(String msg){ 
		try {
			OutputStream outputstream = new FileOutputStream(new File(logroot), true);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					outputstream)); 
				bw.append(msg);
				bw.append("\r\n"); 
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
