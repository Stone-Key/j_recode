package launcher;

import jFile.util.CodeUtil;
import jFile.util.FileIoUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Launcher {
	
	public static void main(String[] args) {

		File file = new File("E:\\工作\\InfoCollectProvinceAction.java");
		StringBuffer stringBuffer = null;
		try {
			stringBuffer = FileIoUtil.getString(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> codeLines = CodeUtil.getCodeLines(stringBuffer.toString());
		for (String item: codeLines ) {
			System.out.println(item);
		}

	}

}
