package timaxa007.temp_block.v2;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public class EventsForge {

	@SubscribeEvent
	public void blockPlace(BlockEvent.PlaceEvent event) {
		TempBlockWSD tempBlock = TempBlockWSD.get(event.world);
		if (tempBlock == null) return;
		if (event.block == Blocks.lit_furnace) {
			System.out.println(event.x + " - " + event.y + " - " + event.z);
			tempBlock.removingTempBlock(event.x, event.y, event.z, event.world.getTotalWorldTime() + 60L);
		}
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

	@SubscribeEvent
	public void chunkLoad(ChunkEvent.Load event) {
		TempBlockWSD tempBlock = TempBlockWSD.get(event.world);
		if (tempBlock == null) return;
		tempBlock.chunkLoad(event.getChunk());
	}

	@SubscribeEvent
	public void chunkUnload(ChunkEvent.Unload event) {
		TempBlockWSD tempBlock = TempBlockWSD.get(event.world);
		if (tempBlock == null) return;
		tempBlock.chunkUnload(event.getChunk());
	}

}
