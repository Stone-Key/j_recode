package jFile.body;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
//注解
public class AtInterface {
	
	private String _interface;//注解源码全字符串
	private String interfaceName;//注解名
	private List<String> interfaceParms = new ArrayList<>();//注解参数列表
	
	public AtInterface(String string) {
		this._interface = string.trim();
		if (string.contains("(")){
			String replace = string.replace("@", "");
			this.interfaceName = replace.substring(	0, replace.indexOf('('));
			String parmString = replace.substring(
					replace.indexOf('(') + 1 , replace.indexOf(')'));
			String[] split = parmString.split(",");
			for (String parm : split) {
				if (parm != null || !parm.trim().isEmpty())
					interfaceParms.add(parm.trim());
			}
		}else {
			this.interfaceName = this._interface.replace("@" , "");
		}
	}

	public String toString(){
		return this._interface;
	}
	
}
