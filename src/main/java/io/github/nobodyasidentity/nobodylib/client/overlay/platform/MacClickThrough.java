package io.github.nobodyasidentity.nobodylib.client.overlay.platform;

import com.sun.jna.Function;
import com.sun.jna.NativeLong;

final class MacClickThrough{
    private static final long NS_FLOATING_WINDOW_LEVEL=3L;
    private MacClickThrough(){}

    /** @param nsWindow the NSWindow* of the overlay window */
    static void apply(long nsWindow){
        if(nsWindow==0L)return;
        Function sel_registerName=Function.getFunction("objc","sel_registerName");
        Function objc_msgSend=Function.getFunction("objc","objc_msgSend");

        long setIgnoresMouseEvents=((Number)sel_registerName.invoke(NativeLong.class,new Object[]{"setIgnoresMouseEvents:"})).longValue();
        long setLevel=((Number)sel_registerName.invoke(NativeLong.class,new Object[]{"setLevel:"})).longValue();

        objc_msgSend.invoke(void.class,new Object[]{nsWindow,setIgnoresMouseEvents,true});
        objc_msgSend.invoke(void.class,new Object[]{nsWindow,setLevel,new NativeLong(NS_FLOATING_WINDOW_LEVEL)});
    }
}