**Nobody Lib** is a simple library mod made by/for Nobodyasidentity for developing Fabric mods.  
> <font color="orange">⚠ Nobody Lib is still under development and stuff might change</font>
# Usage
## Including in Gradle project
I currently don't have a maven set up but I hope to use Modrinth's built-in maven
```gradle
dependencies {
    implementation files('libs/nobodylib-${project.nobodylib_version}.jar')
}
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
    public static final Item EXAMPLE_ITEM = Item.createMusicDisc(MOD_ID, "example_music_disc");
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
## Data generators
Data generators are still work-in-progress and I can't even guarantee that they will stay
# Dependencies
* <a href="https://modrinth.com/mod/fabric-api" target="_blank">FabricAPI</a>
