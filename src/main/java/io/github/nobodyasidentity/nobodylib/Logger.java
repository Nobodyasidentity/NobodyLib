package io.github.nobodyasidentity.nobodylib;
import org.slf4j.LoggerFactory;
public class Logger{
    private final org.slf4j.Logger logger;
    private Logger(String modid){logger=LoggerFactory.getLogger(modid);}
    public static Logger create(String modid){return new Logger(modid);}
    public void info(String text){logger.info(text);}
    public void warn(String text){logger.warn(text);}
    public void error(String text){logger.error(text);}
}