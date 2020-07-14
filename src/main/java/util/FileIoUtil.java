package util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileIoUtil {

	public static final StringBuffer getString(File file) throws IOException {
		StringBuffer result = new StringBuffer();
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		char[] buf = new char[1024];
		int len;
		while((len=bufferedReader.read(buf)) != -1){
			result.append(buf,0,len);
		}
		return  result;
	}
	
	
}
