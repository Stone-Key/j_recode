package launcher;

import util.FileIoUtil;

import java.io.File;
import java.io.IOException;

public class Launcher {
	
	public static void main(String[] args) {

		File file = new File("E:\\工作\\InfoCollectProvinceAction.java");
		try {
			StringBuffer stringBuffer = FileIoUtil.getString(file);
			System.out.println(stringBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
