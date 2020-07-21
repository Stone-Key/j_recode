package jFile.body;

import jFile.exception.FileUncodeException;
import jFile.util.Logger;
import jFile.util.StrUtil;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sun.jmx.snmp.ThreadContext.contains;

//类
@Data
public class Cclass {
	
	private List<Cattr> cattrs = new ArrayList<>();//类的属性
	private List<Cblock> cblocks = new ArrayList<>();//直属于类的代码块
	private List<Cconstractor> cconstractors = new ArrayList<>();//类中显式声明的构造器
	private List<Cmethoad> cmethoads = new ArrayList<>();//类中的方法
	private List<AtInterface> atInterfaces = new ArrayList<>();//位于类上的注解
	private List<String> notes = new ArrayList<>();//类上部的注释内容
	private String className;//类名
	private String parentClassName = "Object"; //默认类的父类是Object,如有其它继承关系,则覆盖
	private List<String> implementedInterfaces = new ArrayList<>(); // 实现的接口名
	private boolean isPublic = false ;//是不是被public修饰的类
	private boolean isFinal = false ; //是不是被final修饰的类


	public Cclass(List<String> themeClassCodes,List<String> underClassCodes) {
		decodeUnderClass(underClassCodes);
		decodeClass(themeClassCodes);
	}

	//解析在类前面的内容
	private void decodeUnderClass(List<String> underClassCodes){
		for (int i = 0; i <underClassCodes.size() ; i++) {
			String item = underClassCodes.get(i);
			//解析类上的注解
			if (item.startsWith("@")){
				String atInterfaceStr = "";
				if (item.contains("(")){//要是这个注解上有参数列表的话
					for( ; i < underClassCodes.size() ; i ++){
						item = underClassCodes.get(i);
						if (item.contains(")")){
							atInterfaceStr += item.substring(0,item.indexOf(')')+1);
							break;
						}else {
							atInterfaceStr += item + " ";
							if (i == underClassCodes.size()){
								throw new FileUncodeException("在跨行的注解内容中未找到最后一个注解的结束标志\")\"");
							}
						}
					}
					this.atInterfaces.add(new AtInterface(atInterfaceStr));
				}else {
					this.atInterfaces.add(new AtInterface(item));
				}
			}
			//解析单行注释
			else if (item.startsWith("//")){
				this.notes.add(item);
			}
			//多行注释的内容
			else if(item.startsWith("/*")){
				String noteStr = "";
				for( ; i < underClassCodes.size() ; i ++){
					item = underClassCodes.get(i);
					if (item.contains("*/")){
						noteStr += item.substring(0,item.indexOf("*/") + 2);
						if (!item.trim().endsWith("*/")){
//							throw new FileUncodeException("不允许在多行注释的同行结尾有内容! ==> " + this.className);
							Logger.appendLog("在多行注释的结尾发现内容:" + item + " ==> " + this.className);
						}
						break;
					}else {
						noteStr += item + " ";
						if (i == underClassCodes.size()){
							throw new FileUncodeException("在跨行的注释内容中未找到最后一个注释的结束标志\"*/\"");
						}
					}
				}
				this.notes.add(noteStr);
			}
		}
	}

	//解析类本身的内容
	private void decodeClass(List<String> classCodes){
		String classDefining = "";//找出所有定义类信息的字符串,防止空格/换行或者其它符号的影响
		for (int i = 0; i < classCodes.size() ; i++) {
			String item  = classCodes .get(i);
			if (item.contains("{")){
				classDefining += item.substring(0,item.indexOf('{')).trim();
				break;
			}else {
				classDefining += item + " ";
			}
		}
		if (classDefining.startsWith("final") || classDefining.contains(" final ")){
			this.isFinal = true;
			classDefining = classDefining.replace("final" , "").trim();
		}
		if (classDefining.startsWith("public")){
			this.isPublic = true;
			classDefining = classDefining.substring(6).trim();
		}
		if (!classDefining.startsWith("class")){
			throw new FileUncodeException("解析类定义代码过程中出现无法解析的字符");
		}
		classDefining = classDefining.substring(5).trim();
		this.className = classDefining.substring(0,classDefining.indexOf(" "));
		classDefining = classDefining.substring(classDefining.indexOf(" ")).trim();
		//解析实现的接口
		if (classDefining.contains("implements")){
			String[] anImplements = classDefining.split("implements");
			String interStr = anImplements[1];
			classDefining = anImplements[0].trim();
			String[] split = interStr.split(",");
			for (String str: split ) {
				this.implementedInterfaces.add(str.trim());
			}
		}
		if (classDefining.startsWith("extends")){
			classDefining = classDefining.substring(7).trim();
			this.parentClassName = classDefining;
		}

		List<String> classInner = new ArrayList<>();
		int start = 0 , end = 0 ;
		for(int k = 0 ; k < classCodes.size() ; k ++){
			String s = classCodes.get(k);
			if (s.contains("{")){
				start = k + 1;
				classInner.add(s.substring(s.indexOf("{") + 1 ));
				break;
			}
		}
		String lastCentence = "";
		for (int k = classCodes.size()-1 ; k > -1 ; k --){
			String s = classCodes.get(k);
			if (s.contains("}")){
				end = k;
				lastCentence = s.substring( 0 , s.indexOf("}")) ;
				break;
			}
		}
		for(int k = start ; k < end ; k ++){
			classInner.add(classCodes.get(k));
		}
		classInner.add(lastCentence);

		decodeClassInner(classInner);

		System.out.println(this.toString());
	}

	private void decodeClassInner(List<String> classInner) {
		int blockDeep = 0;
		List<String> otherMsgs = new ArrayList<>();//在定义语句之前的其它语句(注释/注解等)
		for (int i = 0; i < classInner.size() ; i++) {
			String item = classInner.get(i);
			if (StrUtil.isBlank(item)){
				continue;
			}
			if (item.startsWith("@") || item.startsWith("//") ){//注解 或者单行注释
				otherMsgs.add(item);
				continue;
			}
			if (item.startsWith("/*")){//多行注释
				String note = "";
				for( ; i < classInner.size() ; i ++){
					item = classInner.get(i);
					note += item + " ";
					if (item.contains("*/")){
						++i;
						if (!item.trim().endsWith("*/")){
//							throw new FileUncodeException("不允许在多行注释的同行结尾有内容! ==> " + this.className);
							Logger.appendLog("在多行注释的结尾发现内容:" + item + " ==> " + this.className);
						}
						break;
					}
				}
				otherMsgs.add(note);
				continue;
			}

			/**
			 * 下面的解析类内部的属性,方法,构造器,代码块等内容的方法要重新写.
			 * 打算先按照结构拼接完整的一行字符串,表示一个完整的方法.缺点是我得处理注释的位置和起止点,不然还原的时候不知道注释从哪里开始,从哪里结束
			 */

			if (!item.contains("{") ){//属性(对属性赋值时没有赋值匿名实现类或者其他带有代码块符号的情况)
				if (item.trim().endsWith(";")){//单行定义的属性
					this.cattrs.add(new Cattr(item,otherMsgs));
					otherMsgs = new ArrayList<>();
				}else if ( item.contains("(") ){//有引用数据类型初始化的属性,而且这个属性的声明还没结束(因为没有分号..还好java语法很严格,不然还真不好判断)
					if (item.substring(0,item.indexOf('(')).contains(" new ") || (item.substring(0,item.indexOf('(')).contains("=new "))){
						String initCode = item.substring(item.indexOf("=")).trim();
						for( ++i ; i < classInner.size() ; i ++){
							item = classInner.get(i);
							if (item.contains(";")){
								initCode += " " + item.substring(0,item.indexOf(';'));
								break;
							}else {
								initCode += " " + item;
							}
						}
						this.cattrs.add(new Cattr());
					}
				}

			}
			

		}

	}


	
}
