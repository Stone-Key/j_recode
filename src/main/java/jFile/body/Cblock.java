package jFile.body;

import lombok.Data;

import java.util.List;

/**
 * 代码块（专指类上的代码块，在方法、构造器中使用的代码块慎用,貌似也没什么人在这样用）
 *
 */
@Data
public class Cblock {
	
	private List<String> codes;
	private List<Cblock> blocks;

}
