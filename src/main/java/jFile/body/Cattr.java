package jFile.body;

import jFile.util.Logger;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *属性
 */
@Data
public class Cattr {

	public static final String MPUB = "public";
	public static final String MPRO = "protected";
	public static final String MPRI = "private";

	private String source ; //属性原来代码的样子
	private List<AtInterface> atInterfaces = new ArrayList<>();//属性上的注解
	private List<String> infos = new ArrayList<>();//属性上的注释
	private String modifier = "";//属性的权限类别public/protected/private
	private String type;//属性的类型
	private String name;//属性名
	private String value;//属性在定义时的属性值
	private boolean isStatic = false;//是否是静态变量
	private boolean isFinal = false;//是否是常量
	private String initVal; //定义的时候的赋值(初始值)
	
	public Cattr(String source , List<String> beforMsgs){
		this.source = source;
		//判断权限类型
		if (source.startsWith(MPUB)){
			this.modifier = MPUB;
			source = source.replace(MPUB,"").trim();
		}else if (source.startsWith(MPRO)){
			this.modifier = MPRO;
			source = source.replace(MPRO,"").trim();
		}else if (source.startsWith(MPRI)){
			this.modifier = MPRI ;
			source = source.replace(MPRI,"").trim();
		}
		//判断是否是类变量或者常量
		if (source.contains(" static ")){
			this.isStatic = true;
			source = source.replace(" static " , " ").trim();
		}
		if (source.contains(" final ")){
			this.isFinal = true;
			source = source.replace(" final " , " ").trim();
		}
		//声明数据类型
		this.type = source.substring(0,source.indexOf(' '));
		source = source.substring(source.indexOf(' ')).trim();
		//声明属性名
		this.type = source.substring(0,source.indexOf(' '));
		source = source.substring(source.indexOf(' ')).trim();
		//是否有赋值
		if (source.contains("=") ){
			if (source.endsWith(";"))
				this.initVal = source.substring(0,source.length()-1).replace("=" , "").trim();
			else
				this.initVal = source.replace("=" , "").trim();
		}
		//处理属性上的注释和注解
		for (String item :	beforMsgs) {
			if (item.startsWith("@")){
				this.atInterfaces.add(new AtInterface(item));
			}else if (item.startsWith("//")){
				this.infos.add(item);
			}else {
				Logger.appendLog("Cattr类String,List<String>构造器中未被识别的代码: ==> {  " + item + "  }");
			}
		}
	}

	
}
