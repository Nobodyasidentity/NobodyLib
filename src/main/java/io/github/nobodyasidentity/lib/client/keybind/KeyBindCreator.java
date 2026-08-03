package io.github.nobodyasidentity.lib.client.keybind;

import java.util.function.Function;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.nobodyasidentity.lib.NobodyLib;
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
        return create(mod_id,name,InputConstants.Type.KEYSYM,key,category);
    }
    public static final KeyMapping create(String mod_id,String name,InputConstants.Type type,@Nullable String key,KeyMapping.Category category){
        return create(mod_id,name,type,getKeyInt(key),category);
    }
    public static final KeyMapping create(String mod_id,String name,@Nullable String key,KeyMapping.Category category){
        return create(mod_id,name,getKeyInt(key),category);
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
                InputConstants.Key boundKey=KeyMappingHelper.getBoundKeyOf(keybind);
                int targetKey=boundKey.getValue();
                int processedKey=keyEvent.input();
                if (processedKey==targetKey&&targetKey!=GLFW.GLFW_KEY_UNKNOWN){
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
    public static int getKeyInt(char key){
        return getKeyInt(Character.toString(key));
    }
    public static int getKeyInt(@Nullable String key){
        if(key==null)key="";
        key=key.toLowerCase();
        switch(key){
            case"":return GLFW.GLFW_KEY_UNKNOWN;
            case"1":return GLFW.GLFW_KEY_1;
            case"2":return GLFW.GLFW_KEY_2;
            case"3":return GLFW.GLFW_KEY_3;
            case"4":return GLFW.GLFW_KEY_4;
            case"5":return GLFW.GLFW_KEY_5;
            case"6":return GLFW.GLFW_KEY_6;
            case"7":return GLFW.GLFW_KEY_7;
            case"8":return GLFW.GLFW_KEY_8;
            case"9":return GLFW.GLFW_KEY_9;
            case"0":return GLFW.GLFW_KEY_0;
            case"+":return GLFW.GLFW_KEY_KP_ADD;
            case"a":return GLFW.GLFW_KEY_A;
            case"'":return GLFW.GLFW_KEY_APOSTROPHE;
            case"b":return GLFW.GLFW_KEY_B;
            case"\\":return GLFW.GLFW_KEY_BACKSLASH;
            case"backspace":case"<-":return GLFW.GLFW_KEY_BACKSPACE;
            case"c":return GLFW.GLFW_KEY_C;
            case"caps":return GLFW.GLFW_KEY_CAPS_LOCK;
            case"capslock":return GLFW.GLFW_KEY_CAPS_LOCK;
            case"caps lock":return GLFW.GLFW_KEY_CAPS_LOCK;
            case",":return GLFW.GLFW_KEY_COMMA;
            case"d":return GLFW.GLFW_KEY_D;
            case"delete":return GLFW.GLFW_KEY_DELETE;
            case"down":return GLFW.GLFW_KEY_DOWN;
            case"e":return GLFW.GLFW_KEY_E;
            case"end":return GLFW.GLFW_KEY_END;
            case"enter":return GLFW.GLFW_KEY_ENTER;
            case"=":return GLFW.GLFW_KEY_EQUAL;
            case"escape":return GLFW.GLFW_KEY_ESCAPE;
            case"esc":return GLFW.GLFW_KEY_ESCAPE;
            case"f":return GLFW.GLFW_KEY_F;
            case"f1":return GLFW.GLFW_KEY_F1;
            case"f2":return GLFW.GLFW_KEY_F2;
            case"f3":return GLFW.GLFW_KEY_F3;
            case"f4":return GLFW.GLFW_KEY_F4;
            case"f5":return GLFW.GLFW_KEY_F5;
            case"f6":return GLFW.GLFW_KEY_F6;
            case"f7":return GLFW.GLFW_KEY_F7;
            case"f8":return GLFW.GLFW_KEY_F8;
            case"f9":return GLFW.GLFW_KEY_F9;
            case"f10":return GLFW.GLFW_KEY_F10;
            case"f11":return GLFW.GLFW_KEY_F11;
            case"f12":return GLFW.GLFW_KEY_F12;
            case"f13":return GLFW.GLFW_KEY_F13;
            case"f14":return GLFW.GLFW_KEY_F14;
            case"f15":return GLFW.GLFW_KEY_F15;
            case"f16":return GLFW.GLFW_KEY_F16;
            case"f17":return GLFW.GLFW_KEY_F17;
            case"f18":return GLFW.GLFW_KEY_F18;
            case"f19":return GLFW.GLFW_KEY_F19;
            case"f20":return GLFW.GLFW_KEY_F20;
            case"f21":return GLFW.GLFW_KEY_F21;
            case"f22":return GLFW.GLFW_KEY_F22;
            case"f23":return GLFW.GLFW_KEY_F23;
            case"f24":return GLFW.GLFW_KEY_F24;
            case"f25":return GLFW.GLFW_KEY_F25;
            case"g":return GLFW.GLFW_KEY_G;
            case"`":return GLFW.GLFW_KEY_GRAVE_ACCENT;
            case"h":return GLFW.GLFW_KEY_H;
            case"home":return GLFW.GLFW_KEY_HOME;
            case"i":return GLFW.GLFW_KEY_I;
            case"insert":return GLFW.GLFW_KEY_INSERT;
            case"j":return GLFW.GLFW_KEY_J;
            case"k":return GLFW.GLFW_KEY_K;
            case"l":return GLFW.GLFW_KEY_L;
            case"last":return GLFW.GLFW_KEY_LAST;
            case"left":return GLFW.GLFW_KEY_LEFT;
            case"left alt":return GLFW.GLFW_KEY_LEFT_ALT;
            case"alt":return GLFW.GLFW_KEY_LEFT_ALT;
            case"lalt":return GLFW.GLFW_KEY_LEFT_ALT;
            case"[":return GLFW.GLFW_KEY_LEFT_BRACKET;
            case"left ctrl":return GLFW.GLFW_KEY_LEFT_CONTROL;
            case"ctrl":return GLFW.GLFW_KEY_LEFT_CONTROL;
            case"lctrl":return GLFW.GLFW_KEY_LEFT_CONTROL;
            case"left shift":return GLFW.GLFW_KEY_LEFT_SHIFT;
            case"shift":return GLFW.GLFW_KEY_LEFT_SHIFT;
            case"lshift":return GLFW.GLFW_KEY_LEFT_SHIFT;
            case"left super":return GLFW.GLFW_KEY_LEFT_SUPER;
            case"super":return GLFW.GLFW_KEY_LEFT_SUPER;
            case"lsuper":return GLFW.GLFW_KEY_LEFT_SUPER;
            case"left win":return GLFW.GLFW_KEY_LEFT_SUPER;
            case"win":return GLFW.GLFW_KEY_LEFT_SUPER;
            case"lwin":return GLFW.GLFW_KEY_LEFT_SUPER;
            case"m":return GLFW.GLFW_KEY_M;
            case"menu":return GLFW.GLFW_KEY_MENU;
            case"-":return GLFW.GLFW_KEY_MINUS;
            case"n":return GLFW.GLFW_KEY_N;
            case"num lock":return GLFW.GLFW_KEY_NUM_LOCK;
            case"numlock":return GLFW.GLFW_KEY_NUM_LOCK;
            case"num":return GLFW.GLFW_KEY_NUM_LOCK;
            case"o":return GLFW.GLFW_KEY_O;
            case"p":return GLFW.GLFW_KEY_P;
            case"page down":return GLFW.GLFW_KEY_PAGE_DOWN;
            case"pagedown":return GLFW.GLFW_KEY_PAGE_DOWN;
            case"page up":return GLFW.GLFW_KEY_PAGE_UP;
            case"pageup":return GLFW.GLFW_KEY_PAGE_UP;
            case"pause":return GLFW.GLFW_KEY_PAUSE;
            case".":return GLFW.GLFW_KEY_PERIOD;
            case"print screen":return GLFW.GLFW_KEY_PRINT_SCREEN;
            case"printscreen":return GLFW.GLFW_KEY_PRINT_SCREEN;
            case"q":return GLFW.GLFW_KEY_Q;
            case"r":return GLFW.GLFW_KEY_R;
            case"right":return GLFW.GLFW_KEY_RIGHT;
            case"right alt":return GLFW.GLFW_KEY_RIGHT_ALT;
            case"ralt":return GLFW.GLFW_KEY_RIGHT_ALT;
            case"]":return GLFW.GLFW_KEY_RIGHT_BRACKET;
            case"right ctrl":return GLFW.GLFW_KEY_RIGHT_CONTROL;
            case"rctrl":return GLFW.GLFW_KEY_RIGHT_CONTROL;
            case"right shift":return GLFW.GLFW_KEY_RIGHT_SHIFT;
            case"rshift":return GLFW.GLFW_KEY_RIGHT_SHIFT;
            case"right super":return GLFW.GLFW_KEY_RIGHT_SUPER;
            case"rsuper":return GLFW.GLFW_KEY_RIGHT_SUPER;
            case"s":return GLFW.GLFW_KEY_S;
            case"scroll lock":return GLFW.GLFW_KEY_SCROLL_LOCK;
            case"scrolllock":return GLFW.GLFW_KEY_SCROLL_LOCK;
            case";":return GLFW.GLFW_KEY_SEMICOLON;
            case"/":return GLFW.GLFW_KEY_SLASH;
            case" ":return GLFW.GLFW_KEY_SPACE;
            case"space":return GLFW.GLFW_KEY_SPACE;
            case"t":return GLFW.GLFW_KEY_T;
            case"tab":return GLFW.GLFW_KEY_TAB;
            case"  ":return GLFW.GLFW_KEY_TAB;
            case"   ":return GLFW.GLFW_KEY_TAB;
            case"    ":return GLFW.GLFW_KEY_TAB;
            case"u":return GLFW.GLFW_KEY_U;
            case"unknown":return GLFW.GLFW_KEY_UNKNOWN;
            case"up":return GLFW.GLFW_KEY_UP;
            case"v":return GLFW.GLFW_KEY_V;
            case"w":return GLFW.GLFW_KEY_W;
            case"x":return GLFW.GLFW_KEY_X;
            case"y":return GLFW.GLFW_KEY_Y;
            case"z":return GLFW.GLFW_KEY_Z;
            default:
                NobodyLib.LOGGER.warn("Unknown key string: "+key);
                return GLFW.GLFW_KEY_UNKNOWN;
        }
    }
}