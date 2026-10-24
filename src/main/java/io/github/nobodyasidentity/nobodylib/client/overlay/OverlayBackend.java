package io.github.nobodyasidentity.nobodylib.client.overlay;

/** One way of putting the overlay on screen. Windows and everything else need quite different tricks. */
public interface OverlayBackend{
    /** Creates the window and GL context. Returns false (after logging why) if the overlay can't run here. */
    boolean init(long mcWindowHandle);
    /** Called every client tick while the overlay is shown: keep the window over Minecraft and draw a frame. */
    void tick(long mcWindowHandle);
    void setVisible(boolean visible);
    /** Whether Minecraft is minimized or fully hidden behind other windows. */
    default boolean isMinecraftCovered(long mcWindowHandle){return false;}
}