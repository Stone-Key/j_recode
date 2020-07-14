package jFile;

import lombok.Data;

//拆解成一行一行的代码
@Data
public class Cline {
    private String lineCode;//当前行的文字
    private String source;//当前行的java代码
    private String zhushi;//单行注释的内容
    private String lineNumber;//行号

}
