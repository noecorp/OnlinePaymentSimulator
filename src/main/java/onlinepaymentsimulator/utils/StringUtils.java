package onlinepaymentsimulator.utils;


public class StringUtils {
    
    /**
     * Adds leading zeros to string. E.g. 1 -> 01 and 10 -> 10.
     * @param number
     * @return 
     */
    public static String addLeadingZero(int number) {
        String str = String.valueOf(number);
        int len = str.length();
        
        
        if(len < 2) {
            for(int i = len; i > 0; i--) {
                str = "0" + str;
            }
        }
        
        return str;
    }
}
