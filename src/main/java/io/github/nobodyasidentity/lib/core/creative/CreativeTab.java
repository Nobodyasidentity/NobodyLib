package io.github.nobodyasidentity.lib.core.creative;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class CreativeTab{
    private List<Consumer<CreativeModeTab.Output>>ENTRIES;
    private ResourceKey<CreativeModeTab>KEY;
    private CreativeModeTab TAB;

    public static final CreativeTab OP_BLOCKS=new CreativeTab(CreativeModeTabs.OP_BLOCKS);
    public static final CreativeTab BUILDING_BLOCKS=new CreativeTab(CreativeModeTabs.BUILDING_BLOCKS);
    public static final CreativeTab COLORED_BLOCKS=new CreativeTab(CreativeModeTabs.COLORED_BLOCKS);
    public static final CreativeTab COMBAT=new CreativeTab(CreativeModeTabs.COMBAT);
    public static final CreativeTab FOOD_AND_DRINKS=new CreativeTab(CreativeModeTabs.FOOD_AND_DRINKS);
    public static final CreativeTab FUNCTIONAL_BLOCKS=new CreativeTab(CreativeModeTabs.FUNCTIONAL_BLOCKS);
    public static final CreativeTab HOTBAR=new CreativeTab(CreativeModeTabs.HOTBAR);
    public static final CreativeTab INGREDIENTS=new CreativeTab(CreativeModeTabs.INGREDIENTS);
    public static final CreativeTab INVENTORY=new CreativeTab(CreativeModeTabs.INVENTORY);
    public static final CreativeTab NATURAL_BLOCKS=new CreativeTab(CreativeModeTabs.NATURAL_BLOCKS);
    public static final CreativeTab REDSTONE_BLOCKS=new CreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
    public static final CreativeTab SEARCH=new CreativeTab(CreativeModeTabs.SEARCH);
    public static final CreativeTab SPAWN_EGGS=new CreativeTab(CreativeModeTabs.SPAWN_EGGS);
    public static final CreativeTab TOOLS_AND_UTILITIES=new CreativeTab(CreativeModeTabs.TOOLS_AND_UTILITIES);

    public CreativeTab(String mod_id,Item icon,@Nullable String name){this(mod_id,()->new ItemStack(icon),name);}
    public CreativeTab(String mod_id,Supplier<ItemStack>icon,@Nullable String name){
        this.ENTRIES=new ArrayList<>();
        this.KEY=ResourceKey.create(Registries.CREATIVE_MODE_TAB,Identifier.fromNamespaceAndPath(mod_id,"creative_tab"+(name==null||name.length()<=0?"":("."+name))));
        this.TAB=FabricCreativeModeTab.builder()
            .icon(icon)
            .title(Component.translatable("itemGroup."+mod_id+(name==null||name.length()<=0?"":("."+name))))
            .displayItems((parameters,output)->{
                this.ENTRIES.forEach(entry->entry.accept(output));
            }).build();
    }
    public CreativeTab(CreativeModeTab tab){
        this.ENTRIES=new ArrayList<>();
        this.TAB=tab;
        this.KEY=BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab)
            .orElseThrow(()->new IllegalArgumentException("Tab is not registered!"));
        CreativeModeTabEvents.modifyOutputEvent(this.KEY).register(output->{
            this.ENTRIES.forEach(entry->entry.accept(output));
        });
    }
    public CreativeTab(ResourceKey<CreativeModeTab>tab){
        this.ENTRIES=new ArrayList<>();
        this.KEY=tab;
        this.TAB=BuiltInRegistries.CREATIVE_MODE_TAB.getOptional(tab)
            .orElseThrow(()->new IllegalArgumentException("Tab for key "+tab.identifier()+" is not registered!"));
        CreativeModeTabEvents.modifyOutputEvent(this.KEY).register(output->{
            this.ENTRIES.forEach(entry->entry.accept(output));
        });
    }
    public void initialize(){
        if(!BuiltInRegistries.CREATIVE_MODE_TAB.containsKey(this.KEY)){
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,this.KEY,this.TAB);
        }
    }
    public ResourceKey<CreativeModeTab>getKey(){return this.KEY;}
    public CreativeModeTab getInstance(){return this.TAB;}
    public List<Consumer<CreativeModeTab.Output>>getEntries(){return this.ENTRIES;}
    public Consumer<?>get(int position){return this.ENTRIES.get(position);}
    public int size(){return this.ENTRIES.size();}
    public void add(){return;}
    public void add(Consumer<CreativeModeTab.Output>item){
        this.ENTRIES.add(item);
    }
    public void add(Supplier<ItemStack>item){
        this.add(this.tryCastToOutputConsumer(item));
    }
    @SuppressWarnings("unchecked")
    public void add(Supplier<ItemStack>...items){
        for(Supplier<ItemStack>item:items){
            this.add(item);
        }
    }
    public void add(@Nullable Object item){
        if(item==null)return;
        this.add(this.tryCastToOutputConsumer(item));
    }
    public void add(@Nullable Object...items){
        for(@Nullable Object item:items){
            this.add(item);
        }
    }
    @SuppressWarnings("unchecked")
    public Consumer<CreativeModeTab.Output>tryCastToOutputConsumer(@Nullable Object object){
        if(object instanceof Consumer<?>consumer){
            return(Consumer<CreativeModeTab.Output>)consumer;
        }
        else if(object instanceof Supplier<?>supplier){
            return output->output.accept(((Supplier<ItemStack>)supplier).get());
        }
        else if(object instanceof ItemLike itemlike){
            return output->output.accept(new ItemStack(itemlike));
        }
        else if(object instanceof Item item){
            return output->output.accept(new ItemStack(item));
        }
        else if(object instanceof ItemStack itemstack){
            return output->output.accept(itemstack);
        }
        else if(object instanceof ResourceKey<?>rk&&rk.isFor(Registries.ITEM)){
            return output->output.accept(BuiltInRegistries.ITEM.getValue((ResourceKey<Item>)rk));
        }
        else if(object instanceof Integer i){
            return output->output.accept(BuiltInRegistries.ITEM.byId(i.intValue()));
        }
        else if(object instanceof Identifier id){
            return output->output.accept(BuiltInRegistries.ITEM.getValue(id));
        }
        else{throw new IllegalArgumentException("Cannot cast object of type "+object.getClass().getName()+" to output consumer!");}
    }public Consumer<CreativeModeTab.Output>tryCastToOutputConsumer(){return this.tryCastToOutputConsumer(null);}

    public Consumer<CreativeModeTab.Output>remove(int index){
        return this.ENTRIES.remove(index);
    }
    public boolean remove(Object object){
        return this.ENTRIES.remove(object);
    }
    public void clear(){
        this.ENTRIES.clear();
    }
    public static CreativeTab recreate(CreativeModeTab tab,String mod_id,String name){
        CreativeTab t=new CreativeTab(mod_id,tab::getIconItem,name);
        tab.getDisplayItems().forEach(item->t.add(item));
        return t;
    }
}