package io.github.nobodyasidentity.lib.client.keybind;

import java.util.function.Function;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.nobodyasidentity.lib.NobodyLib;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
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
    public static final KeyMapping create(String mod_id,String name,InputConstants.Type type,String key,KeyMapping.Category category){
        return create(mod_id,name,type,getKeyInt(key),category);
    }
    public static final KeyMapping create(String mod_id,String name,String key,KeyMapping.Category category){
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
    public static int getKeyInt(String key){
        key=key.toLowerCase();
        int keysum=-2;
        switch(key){
            case"":
                keysum=GLFW.GLFW_KEY_UNKNOWN;
                break;
            case"1":
                keysum=GLFW.GLFW_KEY_1;
                break;
            case"2":
                keysum=GLFW.GLFW_KEY_2;
                break;
            case"3":
                keysum=GLFW.GLFW_KEY_3;
                break;
            case"4":
                keysum=GLFW.GLFW_KEY_4;
                break;
            case"5":
                keysum=GLFW.GLFW_KEY_5;
                break;
            case"6":
                keysum=GLFW.GLFW_KEY_6;
                break;
            case"7":
                keysum=GLFW.GLFW_KEY_7;
                break;
            case"8":
                keysum=GLFW.GLFW_KEY_8;
                break;
            case"9":
                keysum=GLFW.GLFW_KEY_9;
                break;
            case"0":
                keysum=GLFW.GLFW_KEY_0;
                break;
            case"+":
                keysum=GLFW.GLFW_KEY_KP_ADD;
                break;
            case"a":
                keysum=GLFW.GLFW_KEY_A;
                break;
            case"'":
                keysum=GLFW.GLFW_KEY_APOSTROPHE;
                break;
            case"b":
                keysum=GLFW.GLFW_KEY_B;
                break;
            case"\\":
                keysum=GLFW.GLFW_KEY_BACKSLASH;
                break;
            case"backspace":
                keysum=GLFW.GLFW_KEY_BACKSPACE;
                break;
            case"<-":
                keysum=GLFW.GLFW_KEY_BACKSPACE;
                break;
            case"c":
                keysum=GLFW.GLFW_KEY_C;
                break;
            case"caps":
                keysum=GLFW.GLFW_KEY_CAPS_LOCK;
                break;
            case"capslock":
                keysum=GLFW.GLFW_KEY_CAPS_LOCK;
                break;
            case"caps lock":
                keysum=GLFW.GLFW_KEY_CAPS_LOCK;
                break;
            case",":
                keysum=GLFW.GLFW_KEY_COMMA;
                break;
            case"d":
                keysum=GLFW.GLFW_KEY_D;
                break;
            case"delete":
                keysum=GLFW.GLFW_KEY_DELETE;
                break;
            case"down":
                keysum=GLFW.GLFW_KEY_DOWN;
                break;
            case"e":
                keysum=GLFW.GLFW_KEY_E;
                break;
            case"end":
                keysum=GLFW.GLFW_KEY_END;
                break;
            case"enter":
                keysum=GLFW.GLFW_KEY_ENTER;
                break;
            case"=":
                keysum=GLFW.GLFW_KEY_EQUAL;
                break;
            case"escape":
                keysum=GLFW.GLFW_KEY_ESCAPE;
                break;
            case"esc":
                keysum=GLFW.GLFW_KEY_ESCAPE;
                break;
            case"f":
                keysum=GLFW.GLFW_KEY_F;
                break;
            case"f1":
                keysum=GLFW.GLFW_KEY_F1;
                break;
            case"f2":
                keysum=GLFW.GLFW_KEY_F2;
                break;
            case"f3":
                keysum=GLFW.GLFW_KEY_F3;
                break;
            case"f4":
                keysum=GLFW.GLFW_KEY_F4;
                break;
            case"f5":
                keysum=GLFW.GLFW_KEY_F5;
                break;
            case"f6":
                keysum=GLFW.GLFW_KEY_F6;
                break;
            case"f7":
                keysum=GLFW.GLFW_KEY_F7;
                break;
            case"f8":
                keysum=GLFW.GLFW_KEY_F8;
                break;
            case"f9":
                keysum=GLFW.GLFW_KEY_F9;
                break;
            case"f10":
                keysum=GLFW.GLFW_KEY_F10;
                break;
            case"f11":
                keysum=GLFW.GLFW_KEY_F11;
                break;
            case"f12":
                keysum=GLFW.GLFW_KEY_F12;
                break;
            case"f13":
                keysum=GLFW.GLFW_KEY_F13;
                break;
            case"f14":
                keysum=GLFW.GLFW_KEY_F14;
                break;
            case"f15":
                keysum=GLFW.GLFW_KEY_F15;
                break;
            case"f16":
                keysum=GLFW.GLFW_KEY_F16;
                break;
            case"f17":
                keysum=GLFW.GLFW_KEY_F17;
                break;
            case"f18":
                keysum=GLFW.GLFW_KEY_F18;
                break;
            case"f19":
                keysum=GLFW.GLFW_KEY_F19;
                break;
            case"f20":
                keysum=GLFW.GLFW_KEY_F20;
                break;
            case"f21":
                keysum=GLFW.GLFW_KEY_F21;
                break;
            case"f22":
                keysum=GLFW.GLFW_KEY_F22;
                break;
            case"f23":
                keysum=GLFW.GLFW_KEY_F23;
                break;
            case"f24":
                keysum=GLFW.GLFW_KEY_F24;
                break;
            case"f25":
                keysum=GLFW.GLFW_KEY_F25;
                break;
            case"g":
                keysum=GLFW.GLFW_KEY_G;
                break;
            case"`":
                keysum=GLFW.GLFW_KEY_GRAVE_ACCENT;
                break;
            case"h":
                keysum=GLFW.GLFW_KEY_H;
                break;
            case"home":
                keysum=GLFW.GLFW_KEY_HOME;
                break;
            case"i":
                keysum=GLFW.GLFW_KEY_I;
                break;
            case"insert":
                keysum=GLFW.GLFW_KEY_INSERT;
                break;
            case"j":
                keysum=GLFW.GLFW_KEY_J;
                break;
            case"k":
                keysum=GLFW.GLFW_KEY_K;
                break;
            case"l":
                keysum=GLFW.GLFW_KEY_L;
                break;
            case"last":
                keysum=GLFW.GLFW_KEY_LAST;
                break;
            case"left":
                keysum=GLFW.GLFW_KEY_LEFT;
                break;
            case"left alt":
                keysum=GLFW.GLFW_KEY_LEFT_ALT;
                break;
            case"alt":
                keysum=GLFW.GLFW_KEY_LEFT_ALT;
                break;
            case"lalt":
                keysum=GLFW.GLFW_KEY_LEFT_ALT;
                break;
            case"[":
                keysum=GLFW.GLFW_KEY_LEFT_BRACKET;
                break;
            case"left ctrl":
                keysum=GLFW.GLFW_KEY_LEFT_CONTROL;
                break;
            case"ctrl":
                keysum=GLFW.GLFW_KEY_LEFT_CONTROL;
                break;
            case"lctrl":
                keysum=GLFW.GLFW_KEY_LEFT_CONTROL;
                break;
            case"left shift":
                keysum=GLFW.GLFW_KEY_LEFT_SHIFT;
                break;
            case"shift":
                keysum=GLFW.GLFW_KEY_LEFT_SHIFT;
                break;
            case"lshift":
                keysum=GLFW.GLFW_KEY_LEFT_SHIFT;
                break;
            case"left super":
                keysum=GLFW.GLFW_KEY_LEFT_SUPER;
                break;
            case"super":
                keysum=GLFW.GLFW_KEY_LEFT_SUPER;
                break;
            case"lsuper":
                keysum=GLFW.GLFW_KEY_LEFT_SUPER;
                break;
            case"left win":
                keysum=GLFW.GLFW_KEY_LEFT_SUPER;
                break;
            case"win":
                keysum=GLFW.GLFW_KEY_LEFT_SUPER;
                break;
            case"lwin":
                keysum=GLFW.GLFW_KEY_LEFT_SUPER;
                break;
            case"m":
                keysum=GLFW.GLFW_KEY_M;
                break;
            case"menu":
                keysum=GLFW.GLFW_KEY_MENU;
                break;
            case"-":
                keysum=GLFW.GLFW_KEY_MINUS;
                break;
            case"n":
                keysum=GLFW.GLFW_KEY_N;
                break;
            case"num lock":
                keysum=GLFW.GLFW_KEY_NUM_LOCK;
                break;
            case"numlock":
                keysum=GLFW.GLFW_KEY_NUM_LOCK;
                break;
            case"num":
                keysum=GLFW.GLFW_KEY_NUM_LOCK;
                break;
            case"o":
                keysum=GLFW.GLFW_KEY_O;
                break;
            case"p":
                keysum=GLFW.GLFW_KEY_P;
                break;
            case"page down":
                keysum=GLFW.GLFW_KEY_PAGE_DOWN;
                break;
            case"pagedown":
                keysum=GLFW.GLFW_KEY_PAGE_DOWN;
                break;
            case"page up":
                keysum=GLFW.GLFW_KEY_PAGE_UP;
                break;
            case"pageup":
                keysum=GLFW.GLFW_KEY_PAGE_UP;
                break;
            case"pause":
                keysum=GLFW.GLFW_KEY_PAUSE;
                break;
            case".":
                keysum=GLFW.GLFW_KEY_PERIOD;
                break;
            case"print screen":
                keysum=GLFW.GLFW_KEY_PRINT_SCREEN;
                break;
            case"printscreen":
                keysum=GLFW.GLFW_KEY_PRINT_SCREEN;
                break;
            case"q":
                keysum=GLFW.GLFW_KEY_Q;
                break;
            case"r":
                keysum=GLFW.GLFW_KEY_R;
                break;
            case"right":
                keysum=GLFW.GLFW_KEY_RIGHT;
                break;
            case"right alt":
                keysum=GLFW.GLFW_KEY_RIGHT_ALT;
                break;
            case"ralt":
                keysum=GLFW.GLFW_KEY_RIGHT_ALT;
                break;
            case"]":
                keysum=GLFW.GLFW_KEY_RIGHT_BRACKET;
                break;
            case"right ctrl":
                keysum=GLFW.GLFW_KEY_RIGHT_CONTROL;
                break;
            case"rctrl":
                keysum=GLFW.GLFW_KEY_RIGHT_CONTROL;
                break;
            case"right shift":
                keysum=GLFW.GLFW_KEY_RIGHT_SHIFT;
                break;
            case"rshift":
                keysum=GLFW.GLFW_KEY_RIGHT_SHIFT;
                break;
            case"right super":
                keysum=GLFW.GLFW_KEY_RIGHT_SUPER;
                break;
            case"rsuper":
                keysum=GLFW.GLFW_KEY_RIGHT_SUPER;
                break;
            case"s":
                keysum=GLFW.GLFW_KEY_S;
                break;
            case"scroll lock":
                keysum=GLFW.GLFW_KEY_SCROLL_LOCK;
                break;
            case"scrolllock":
                keysum=GLFW.GLFW_KEY_SCROLL_LOCK;
                break;
            case";":
                keysum=GLFW.GLFW_KEY_SEMICOLON;
                break;
            case"/":
                keysum=GLFW.GLFW_KEY_SLASH;
                break;
            case" ":
                keysum=GLFW.GLFW_KEY_SPACE;
                break;
            case"space":
                keysum=GLFW.GLFW_KEY_SPACE;
                break;
            case"t":
                keysum=GLFW.GLFW_KEY_T;
                break;
            case"tab":
                keysum=GLFW.GLFW_KEY_TAB;
                break;
            case"  ":
                keysum=GLFW.GLFW_KEY_TAB;
                break;
            case"   ":
                keysum=GLFW.GLFW_KEY_TAB;
                break;
            case"    ":
                keysum=GLFW.GLFW_KEY_TAB;
                break;
            case"u":
                keysum=GLFW.GLFW_KEY_U;
                break;
            case"unknown":
                keysum=GLFW.GLFW_KEY_UNKNOWN;
                break;
            case"up":
                keysum=GLFW.GLFW_KEY_UP;
                break;
            case"v":
                keysum=GLFW.GLFW_KEY_V;
                break;
            case"w":
                keysum=GLFW.GLFW_KEY_W;
                break;
            case"x":
                keysum=GLFW.GLFW_KEY_X;
                break;
            case"y":
                keysum=GLFW.GLFW_KEY_Y;
                break;
            case"z":
                keysum=GLFW.GLFW_KEY_Z;
                break;
        }
        if(keysum==-2){
            NobodyLib.LOGGER.warn("Key \""+key+"\" was not found");
            keysum=GLFW.GLFW_KEY_UNKNOWN;
        }
        return keysum;
    }
}