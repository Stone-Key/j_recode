package jFile.util;

import java.util.ArrayList;
import java.util.List;

public class CodeUtil {

    public static List<String> getCodeLines(String source){
        String[] temp =  source.split("\n");
        List<String> result = new ArrayList<>();
        for (String item :  temp) {
           item = item.replaceAll("\r" , "").replaceAll("\t", "");
           result.add(item.trim());
        }

        return  result;
    }
}
