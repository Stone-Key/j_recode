package jFile.body;
/**
 * 此整个包的目的是将读取到的java文件源码字符串拆分还原为各种类的基本结构，以方便对java类、接口进行改编重写
 */
import jFile.exception.FileUncodeException;
import lombok.Data;
import jFile.util.CodeUtil;
import jFile.util.FileIoUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Cfile {

	private File file;
	private String pkg;//包路径信息
	private List<String> imports = new ArrayList<>();//导入的包
	private List<Cclass> classes = new ArrayList<>();//文件包含的类
	private List<String>  clines = new ArrayList<>();//代码的具体每一行
	private int blockStake = 0; //当前遍历的代码在代码块的底几层(就是它属于第几层代码块)

	public Cfile(File file){
		this.file = file;
		StringBuffer stringBuffer = null;
		try {
			stringBuffer = FileIoUtil.getString(file);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileUncodeException("无法获取源码! ==> " + file.getName());
		}
		List<String> codeLines = CodeUtil.getCodeLines(stringBuffer.toString());
		this.clines = codeLines;
		//获取包的信息
		for (String item: codeLines ) {
			if(item.startsWith("package")){
				this.pkg = item;
				break;
			}
		}
		//获取导入的包的信息
		for (String item: codeLines ) {
			if(item.startsWith("import")){
				this.imports.add(item);
			}
		}
		boolean writeClass = false;
		ArrayList<String> strings = new ArrayList<>();
		//找到所有在文件中定义的类
		int classBeginLineNum = 0 ;
		for (int ind = 0 ; ind < codeLines.size(); ind ++ ) {
			String item = codeLines.get(ind);
			if(strDefineClass(item)){
				classBeginLineNum = ind;
				writeClass = true;
			}
			if (writeClass){
				for (int i = 0; i <item.length() ; i++) {
					if (item.charAt(i) == '{') blockStake ++;
					if (item.charAt(i) == '}') blockStake --;
					if (blockStake <0){
						throw new FileUncodeException("读取类归属代码块层级错误!");
					}
				}
				strings.add(item);
			}
			if (writeClass && blockStake == 0){
				writeClass = false;
				List<String> underClassCodes = getUnderClassLines(classBeginLineNum,codeLines);
				Cclass cclass = new Cclass(strings,underClassCodes);
				this.classes.add(cclass);
				strings = new ArrayList<>();
			}
		}
	}

	//获取类上面的注解,注释等内容.如果遇到与类的定义无关的内容,则结束
	private List<String> getUnderClassLines(int lineIndex , List<String> source){
		if (lineIndex > source.size()){
			throw new FileUncodeException("行索引错误!");
		}
		ArrayList<String> result = new ArrayList<>();
		for (int i = lineIndex -1 ; i >0 ; i--) {
			String s = source.get(i);
			if (s.startsWith("import") || s.endsWith("}") || s.startsWith("package")){
				break;
			}
			result.add(s);
		}
		Collections.reverse(result);
		return  result;
	}

	//判断字符串是否包含了定义一个类的字符
	private boolean strDefineClass(String str){
		if (this.blockStake != 0){
			return false;
		}
		if (str.startsWith("public")){
			int aClass = str.indexOf("class");
			if ( 5 < aClass ){
				for (int i = 6; i < aClass ; i++) {
					//当权限修饰符与类定义关键字之间有其它字符
					if (str.charAt(i) != ' ' && str.charAt(i) != '\n' && str.charAt(i) != '\r'){
						return  false;
					}
				}
			}
		}
		else if (! str.startsWith("class")){
			return false;
		}
		return true;
	}

}
