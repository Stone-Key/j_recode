package jFile.body;

import lombok.Data;

import java.util.List;

/**
 * 
 *属性
 */
@Data
public class Cattr {

	private List<AtInterface> atInterfaces;//属性上的注解
	private String modifier;//属性的权限类别public/protected/private
	private String type;//属性的类型
	private String name;//属性名
	private String value;//属性在定义时的属性值
	
	

	
}
