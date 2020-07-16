package jFile.util;

final public class StrUtil {
    public static boolean isBlank(String str){
        return str == null || str.isEmpty();
    }

    public static int countFlag(String str , char flag){
        int count = 0;
        char[] chars = str.toCharArray();
        for (char item: chars  ) {
            if (item == flag) count ++ ;
        }
        return count;
    }

    public static int countFlag(String str , String flag){
        int count = 0;
       while (true){
           if (str.contains(flag)){
               count ++ ;
               str = str.replace(flag,"");
           }else {
               break;
           }
       }
        return count;
    }

}
