package jFile;
/**
 * 此整个包的目的是将读取到的java文件源码字符串拆分还原为各种类的基本结构，以方便对java类、接口进行改编重写
 */
import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class Cfile {

	private File file;
	private String pkg;//包路径信息
	private List<String> imports;//导入的包
	private List<Cclass> classes;//文件包含的类
	private List<Cline>  clines;

	public Cfile(File file){
		this.file = file;
	}

}
