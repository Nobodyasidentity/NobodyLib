**Nobody Lib** is a simple library mod made by/for Nobodyasidentity for developing Fabric mods.  
> <font color="orange">⚠ Nobody Lib is still under development and stuff might change</font>
# Usage
## Including in Gradle project
`build.gradle`:
```gradle
repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = "https://api.modrinth.com/maven"
            }
        }
        filter {
            includeGroup "maven.modrinth"
        }
    }
}

dependencies {
	implementation "maven.modrinth:nobodylib:${project.nobodylib_version}"
}
```
`gradle.properties`:
```properties
# Change to whatever is the latest version for your Minecraft version
nobodylib_version=0.0.4+26.2
```
## Item
```java
import io.github.nobodyasidentity.lib.core.item.Item;

public class item_example {
    public static final String MOD_ID = "example_mod";

    public static final Item EXAMPLE_ITEM = Item.create(MOD_ID, "example_item");
}
```
### Music Disc item
Note: you will have to add stuff to the data and assets folder on your own, otherwise the mod will not be able to run.
```java
import io.github.nobodyasidentity.lib.core.item.Item;

public class music_disc_example {
    public static final String MOD_ID = "example_mod";
    
    // this will also create a sound event called "example_mod:example_music_disc"
    public static final Item EXAMPLE_MUSIC_DISC = Item.createMusicDisc(MOD_ID, "example_music_disc");
}
```
### Spawn Egg item
```java
import io.github.nobodyasidentity.lib.core.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import example_mod.example_entities

public class spawn_egg_example {
    public static final String MOD_ID = "example_mod";
    
    public static final SpawnEggItem EXAMPLE_SPAWN_EGG = Item.createSpawnEgg(MOD_ID, "example_spawn_egg", example_entities.example_entity);
}
```
### Custom Item subclasses
```java
import io.github.nobodyasidentity.lib.core.item.Item;
import com.example.CustomItem

public class custom_subclass_example {
    public static final String MOD_ID = "example_mod";
    
    public static final CustomItem EXAMPLE_CUSTOM_ITEM = Item.create(MOD_ID, "example_custom_item", CustomItem::new);
}
```
## Block
```java
import net.minecraft.world.item.BlockItem;
import io.github.nobodyasidentity.lib.core.item.Block;

public class block_example {
    public static final String MOD_ID = "example_mod";
    
    public static final Block EXAMPLE_BLOCK = Block.create(MOD_ID, "example_block");
    public static final BlockItem EXAMPLE_BLOCK_ITEM = Block.register(EXAMPLE_BLOCK);
}
```
### Custom Block subclasses
```java
import net.minecraft.world.item.BlockItem;
import io.github.nobodyasidentity.lib.core.item.Block;
import com.example.CustomBlock

public class custom_block_example {
    public static final String MOD_ID = "example_mod";
    
    public static final Block EXAMPLE_CUSTOM_BLOCK = Block.create(MOD_ID, "example_custom_block", CustomBlock::new);
    public static final BlockItem EXAMPLE_CUSTOM_BLOCK_ITEM = Block.register(EXAMPLE_CUSTOM_BLOCK);
}
```
## Logger
```java
import io.github.nobodyasidentity.lib.Logger;

public class logger_example {
    public static final String MOD_ID = "example_mod";
    
    public static final Logger LOGGER = Logger.create(MOD_ID);
}
```
## Py
Nobody Lib also has some simple functions based on functions from Python
```java
import io.github.nobodyasidentity.lib.Py;

public class py_example {
    public static final String MOD_ID = "example_mod";
    
    public void init(){
        Py.print(
            py.str(null) // "null"
        );
    }
}
```
## Client Load Event
```java
import io.github.nobodyasidentity.lib.client.ClientLoadEvent;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.Window;

public class client_load_example {
    public static final String MOD_ID = "example_mod";
    
    public void init(){
        ClientLoadEvent.register(client->{
            Window window=ClientLoadEvent.Minecraft().getWindow(); // get game window
        });
    }
}
```
## Data generators
Data generators are still work-in-progress and I can't even guarantee that they will stay
# Dependencies
* <a href="https://modrinth.com/mod/fabric-api" target="_blank">**FabricAPI**</a>