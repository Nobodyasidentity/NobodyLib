package io.github.nobodyasidentity.nobodylib.client.overlay.platform;

import io.github.nobodyasidentity.nobodylib.NobodyLib;

final class Win32Presenter{
    private static final Object LOCK=new Object();
    private static Thread thread;

    private static boolean hasRequest=false;
    private static boolean working=false;
    private static long requestAddress;
    private static int requestX,requestY,requestWidth,requestHeight;
    private static long requestSeq;
    private static volatile long completedSeq=0;

    private Win32Presenter(){}

    static void start(){
        if(thread!=null)return;
        thread=new Thread(Win32Presenter::run,"NobodyLib overlay presenter");
        thread.setDaemon(true);
        thread.start();
    }

    /** Whether it is still working on (or about to start on) a frame. */
    static boolean isBusy(){
        synchronized(LOCK){
            return hasRequest||working;
        }
    }

    /** The sequence number of the last frame it finished with. Its source memory may be released once this reaches that frame's number. */
    static long completedSeq(){return completedSeq;}

    /** Hands over a frame. The memory at {@code address} must stay valid until {@link #completedSeq()} reaches {@code seq}. */
    static boolean submit(long address,int x,int y,int width,int height,long seq){
        synchronized(LOCK){
            if(hasRequest||working)return false;
            requestAddress=address;
            requestX=x;
            requestY=y;
            requestWidth=width;
            requestHeight=height;
            requestSeq=seq;
            hasRequest=true;
            LOCK.notifyAll();
            return true;
        }
    }

    private static void run(){
        while(true){
            long address,seq;
            int x,y,width,height;
            synchronized(LOCK){
                while(!hasRequest){
                    try{
                        LOCK.wait();
                    }catch(InterruptedException e){
                        return;
                    }
                }
                address=requestAddress;
                x=requestX;
                y=requestY;
                width=requestWidth;
                height=requestHeight;
                seq=requestSeq;
                hasRequest=false;
                working=true;
            }
            try{
                Win32LayeredWindow.present(x,y,width,height,address);
            }catch(Throwable t){
                NobodyLib.LOGGER.error("Overlay presenter failed on a frame",t);
            }finally{
                completedSeq=seq;
                synchronized(LOCK){
                    working=false;
                }
            }
        }
    }
}