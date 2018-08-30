package timaxa007.temp_block.v1;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

public class EventsForge {

	@SubscribeEvent
	public void block(BlockEvent.PlaceEvent event) {
		TempBlockWSD tempBlock = TempBlockWSD.get(event.world);
		if (tempBlock == null) return;
		if (event.block == Blocks.lit_furnace)
			tempBlock.removingTempBlock(event.x, event.y, event.z, event.world.getTotalWorldTime() + 60L);
	}

	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event) {
		TempBlockWSD.get(event.world);
	}

	@SubscribeEvent
	public void worldSave(WorldEvent.Save event) {
		TempBlockWSD tempBlock = TempBlockWSD.get(event.world);
		if (tempBlock == null) return;
		tempBlock.markDirty();
	}

}
