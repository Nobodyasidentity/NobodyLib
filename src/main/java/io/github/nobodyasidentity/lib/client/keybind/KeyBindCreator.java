package io.github.nobodyasidentity.lib.client.keybind;

import java.util.Map;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.nobodyasidentity.nobodylib.NobodyLib;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public class KeyBindCreator{
    public static final KeyMapping.Category createCategory(String mod_id,String name){
        return KeyMapping.Category.register(Identifier.fromNamespaceAndPath(mod_id,name));
    }
    public static final KeyMapping create(String mod_id,String name,InputConstants.Type type,int key,KeyMapping.Category category){
        return KeyMappingHelper.registerKeyMapping(new KeyMapping("key."+mod_id+"."+name,type,key,category));
    }
    public static final KeyMapping create(String mod_id,String name,int key,KeyMapping.Category category){
        return create(mod_id,name,keyboardType(),key,category);
    }
    public static final KeyMapping create(String mod_id,String name,InputConstants.Type type,@Nullable String key,KeyMapping.Category category){
        return create(mod_id,name,type,getKeyFromName(key).getValue(),category);
    }
    public static final KeyMapping create(String mod_id,String name,@Nullable String key,KeyMapping.Category category){
        InputConstants.Key bound=getKeyFromName(key);
        return create(mod_id,name,bound.getType(),bound.getValue(),category);
    }
    public static final KeyMapping create(String mod_id,String name,int key){
        return create(mod_id,name,key,KeyMapping.Category.MISC);
    }
    public static final KeyMapping create(String mod_id,String name,InputConstants.Type type,String key){
        return create(mod_id,name,type,getKeyInt(key),KeyMapping.Category.MISC);
    }
    public static final void registerEvent(KeyMapping keybind,ClientTickEvents.EndTick fn,boolean prepare){
        if(prepare){
            ClientTickEvents.END_CLIENT_TICK.register(client->{
                while(keybind.consumeClick()){
                    fn.onEndTick(client);
                }
            });
        }else{
            ClientTickEvents.END_CLIENT_TICK.register(fn);
        }
    }
    public static final void registerEvent(KeyMapping keybind,ClientTickEvents.EndTick fn){
        registerEvent(keybind,fn,true);
    }
    public static final void registerEvent(KeyMapping keybind,Function<Minecraft,?>fn,boolean prepare){
        registerEvent(keybind,(ClientTickEvents.EndTick)fn,prepare);
    }
    public static final void registerEvent(KeyMapping keybind,Function<Minecraft,?>fn){
        registerEvent(keybind,(ClientTickEvents.EndTick)fn,true);
    }
    public static final void registerGlobalEvent(KeyMapping keybind,ClientTickEvents.EndTick fn){
        ClientTickEvents.END_CLIENT_TICK.register(client->{
            while(keybind.consumeClick()){
                fn.onEndTick(client);
            }
        });
        ScreenEvents.BEFORE_INIT.register((client,screen,scaledWidth,scaledHeight)->{
            ScreenKeyboardEvents.allowKeyPress(screen).register((activeScreen,keyEvent)->{
                if(!keybind.isUnbound()&&keybind.matches(keyEvent)){
                    fn.onEndTick(client);
                    return false;
                }
                return true;
            });
        });
    }
    public static final void registerGlobalEvent(KeyMapping keybind,Function<Minecraft,?>fn){
        registerGlobalEvent(keybind,(ClientTickEvents.EndTick)fn);
    }
    public static InputConstants.Key getKey(KeyMapping keybind){
        return KeyMappingHelper.getBoundKeyOf(keybind);
    }
    public static int getKeyInt(char key){
        return getKeyInt(Character.toString(key));
    }
    public static int getKeyInt(KeyMapping keybind){
        return getKey(keybind).getValue();
    }
    private static InputConstants.Type keyboardType(){
        return InputConstants.UNKNOWN.getType();
    }
    public static int getKeyInt(@Nullable String key){
        return getKeyFromName(key).getValue();
    }
    private static final Map<String,String> KEY_NAMES=Map.ofEntries(
        Map.entry("+","keypad.add"),
        Map.entry("'","apostrophe"),
        Map.entry("\\","backslash"),
        Map.entry("<-","backspace"),
        Map.entry("caps","caps.lock"),Map.entry("capslock","caps.lock"),Map.entry("caps lock","caps.lock"),
        Map.entry(",","comma"),
        Map.entry("=","equal"),
        Map.entry("esc","escape"),
        Map.entry("`","grave.accent"),
        Map.entry("last","menu"),
        Map.entry("left alt","left.alt"),Map.entry("alt","left.alt"),Map.entry("lalt","left.alt"),
        Map.entry("[","left.bracket"),
        Map.entry("left ctrl","left.control"),Map.entry("ctrl","left.control"),Map.entry("lctrl","left.control"),
        Map.entry("left shift","left.shift"),Map.entry("shift","left.shift"),Map.entry("lshift","left.shift"),
        Map.entry("left super","left.win"),Map.entry("super","left.win"),Map.entry("lsuper","left.win"),
        Map.entry("left win","left.win"),Map.entry("win","left.win"),Map.entry("lwin","left.win"),
        Map.entry("-","minus"),
        Map.entry("num lock","num.lock"),Map.entry("numlock","num.lock"),Map.entry("num","num.lock"),
        Map.entry("page down","page.down"),Map.entry("pagedown","page.down"),
        Map.entry("page up","page.up"),Map.entry("pageup","page.up"),
        Map.entry(".","period"),
        Map.entry("print screen","print.screen"),Map.entry("printscreen","print.screen"),
        Map.entry("right alt","right.alt"),Map.entry("ralt","right.alt"),
        Map.entry("]","right.bracket"),
        Map.entry("right ctrl","right.control"),Map.entry("rctrl","right.control"),
        Map.entry("right shift","right.shift"),Map.entry("rshift","right.shift"),
        Map.entry("right super","right.win"),Map.entry("rsuper","right.win"),
        Map.entry("scroll lock","scroll.lock"),Map.entry("scrolllock","scroll.lock"),
        Map.entry(";","semicolon"),
        Map.entry("/","slash"),
        Map.entry(" ","space"),
        Map.entry("  ","tab"),Map.entry("   ","tab"),Map.entry("    ","tab")
    );
    public static InputConstants.Key getKeyFromName(@Nullable String key){
        if(key==null)key="";
        key=key.toLowerCase();
        if(key.isEmpty()||key.equals("unknown"))return InputConstants.UNKNOWN;
        String name=KEY_NAMES.getOrDefault(key,key);
        try{
            return InputConstants.getKey("key.keyboard."+name);
        }catch(IllegalArgumentException e){
            NobodyLib.LOGGER.warn("Unknown key string: "+key);
            return InputConstants.UNKNOWN;
        }
    }
}