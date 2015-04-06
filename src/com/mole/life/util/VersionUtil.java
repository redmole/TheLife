package com.mole.life.util;

/**
 * Created by redmole on 2015/3/22.
 */
public class VersionUtil {
        public static boolean hasNewVersion(String localVersin, String newVersin) {
            if(newVersin == null){
               return false;
            }
            if (localVersin == null){
                return true;
            }
            int result = localVersin.compareTo(newVersin);
            if (result < 0) {
                return true;
            }
            return false;
        }
}
