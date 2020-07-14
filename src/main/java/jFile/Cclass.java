package jFile;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//类
@Data
public class Cclass {
	
	private List<Cattr> cattrs = new ArrayList<>();
	private List<Cblock> cblocks = new ArrayList<>();
	private List<Cconstractor> cconstractors = new ArrayList<>();
	private List<Cmethoad> cmethoads = new ArrayList<>();
	private List<AtInterface> atInterfaces = new ArrayList<>();
	

	
}
