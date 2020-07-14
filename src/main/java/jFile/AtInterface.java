package jFile;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@EqualsAndHashCode
//注解
public class AtInterface {
	
	private String _interface;//注解源码全字符串
	private String interfaceName;//注解名
	private List<String> interfaceParms;//注解参数列表
	
	public AtInterface(String string) {
		this._interface = string.trim();
		this.interfaceName = this._interface.substring(
				this._interface.indexOf('@'), this._interface.indexOf('('));
		this.interfaceParms = new ArrayList<>();
		String parmString = this._interface.substring(
				this._interface.indexOf('('), this._interface.indexOf(')'));
		String[] split = parmString.split(",");
		for (String parm : split) {
			interfaceParms.add(parm.trim());
		}
	}

	
}
