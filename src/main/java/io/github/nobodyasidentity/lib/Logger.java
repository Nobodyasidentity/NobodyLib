package io.github.nobodyasidentity.lib;

import org.slf4j.LoggerFactory;
public class Logger{
    // other
    private final org.slf4j.Logger logger;
    private Logger(String modid){logger=LoggerFactory.getLogger(modid);}
    public static Logger create(String modid){return new Logger(modid);}
    public static Logger clone(org.slf4j.Logger supLogger){return new Logger(supLogger.getName());}
    public final String getName(){return logger.getName();}
    public void print(Object...args){System.out.println(Py.str("["+logger.getName()+"]",args));}
    // String
    public void info(String text){logger.info(text);}
    public void warn(String text){logger.warn(text);}
    public void error(String text){logger.error(text);}
    public void log(String text){logger.info(text);}
    public void debug(String text){logger.debug(text);}
    // String, Throwable
    public void info(String text,Throwable t){logger.info(text,t);}
    public void warn(String text,Throwable t){logger.warn(text,t);}
    public void error(String text,Throwable t){logger.error(text,t);}
    public void log(String text,Throwable t){logger.info(text,t);}
    public void debug(String text,Throwable t){logger.debug(text,t);}
    // String, Object...
    public void info(String text,Object...args){logger.info(text,args);}
    public void warn(String text,Object...args){logger.warn(text,args);}
    public void error(String text,Object...args){logger.error(text,args);}
    public void log(String text,Object...args){logger.info(text,args);}
    public void debug(String text,Object...args){logger.debug(text,args);}
    // Object...
    public void info(Object...args){logger.info(Py.str(args));}
    public void warn(Object...args){logger.warn(Py.str(args));}
    public void error(Object...args){logger.error(Py.str(args));}
    public void log(Object...args){logger.info(Py.str(args));}
    public void debug(Object...args){logger.debug(Py.str(args));}
}