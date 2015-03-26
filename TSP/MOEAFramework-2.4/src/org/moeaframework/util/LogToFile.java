package org.moeaframework.util;

import java.io.File;  
import java.io.IOException;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.logging.FileHandler;  
import java.util.logging.Formatter;
import java.util.logging.Level;  
import java.util.logging.LogRecord;
import java.util.logging.Logger;  
import java.util.logging.SimpleFormatter;  
  
public class LogToFile {  
  
    private static final SimpleDateFormat sdf = new SimpleDateFormat(  
            "yyyy-MM-dd_HHmmss");  
    private static final String LOG_FOLDER_NAME = "MyLoggerFile";
    
    private static String LOG_FILE_PRE = "TSP";  
  
    private static final String LOG_FILE_SUFFIX = ".csv";   
    
    private synchronized static String getLogFilePath() {  
        StringBuffer logFilePath = new StringBuffer();  
        logFilePath.append(System.getProperty("user.home"));  
        logFilePath.append(File.separatorChar);  
        logFilePath.append(LOG_FOLDER_NAME);  
  
        File file = new File(logFilePath.toString());  
        if (!file.exists())  
            file.mkdir();  
  
        logFilePath.append(File.separatorChar);  
        logFilePath.append(LOG_FILE_PRE+sdf.format(new Date()));  
        logFilePath.append(LOG_FILE_SUFFIX);  
  
        return logFilePath.toString();  
    }  
  
    public synchronized static Logger setLoggerHanlder(Logger logger) {  
        return setLoggerHanlder(logger, Level.ALL);  
    }  
    
    public synchronized static Logger setLoggerHanlder(Logger logger ,  
            Level level) {    
        return setLoggerHanlder(logger, Level.ALL, "TSP");  
    }  
    
    public synchronized static Logger setLoggerHanlder(Logger logger,  
            Level level, String prefilename) {  
    	LOG_FILE_PRE = prefilename;
        FileHandler fileHandler = null;  
        try {  
            //文件日誌內容標記為可追加  
            fileHandler = new FileHandler(getLogFilePath(), true);  
  
            //以文本的形式輸出 

            fileHandler.setFormatter(new Formatter() {
                public String format(LogRecord record) {
                  //return record.getLevel() + "  :  "
                  //    + record.getSourceClassName() + " -:- "
                  //    + record.getSourceMethodName() + " -:- "
                  //    + record.getMessage() + "\n";
                  return record.getMessage() + "\n";
                }
              });
            
            //fileHandler.setFormatter(new SimpleFormatter());  
              
            logger.addHandler(fileHandler);  
            logger.setLevel(level);  
  
              
  
        } catch (SecurityException e) {  
            logger.severe(populateExceptionStackTrace(e));  
        } catch (IOException e) {  
            logger.severe(populateExceptionStackTrace(e));  
        }  
        return logger;  
    }  
  
    private synchronized static String populateExceptionStackTrace(Exception e) {  
        StringBuilder sb = new StringBuilder();  
        sb.append(e.toString()).append("\n");  
        for (StackTraceElement elem : e.getStackTrace()) {  
            sb.append("\tat ").append(elem).append("\n");  
        }  
        return sb.toString();  
    }  
}  
