package io.github.nobodyasidentity.lib;
import org.slf4j.LoggerFactory;
public class Logger{
    private final org.slf4j.Logger logger;
    private Logger(String modid){logger=LoggerFactory.getLogger(modid);}
    public static Logger create(String modid){return new Logger(modid);}
    public void error(String text,Throwable t){logger.error(text,t);}
    public void info(String text){logger.info(text);}
    public void warn(String text){logger.warn(text);}
    public void error(String text){logger.error(text);}
    public void log(String text){logger.info(text);}
    public void info(String text,Object...args){logger.info(text,args);}
    public void warn(String text,Object...args){logger.warn(text,args);}
    public void error(String text,Object...args){logger.error(text,args);}
    public void log(String text,Object...args){logger.info(text,args);}
    public void info(Object...args){logger.info(Py.str(args));}
    public void warn(Object...args){logger.warn(Py.str(args));}
    public void error(Object...args){logger.error(Py.str(args));}
    public void log(Object...args){logger.info(Py.str(args));}
}