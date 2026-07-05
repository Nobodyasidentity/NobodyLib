## Item
```java
import io.github.nobodyasidentity.lib.core.item.Item;

public class item_example {
    public static final String MOD_ID = "example_mod";

    public static final Item EXAMPLE_ITEM = Item.create(MOD_ID, "example_item");
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