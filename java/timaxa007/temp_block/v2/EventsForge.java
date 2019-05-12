package timaxa007.temp_block.v2;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public class EventsForge {

	@SubscribeEvent
	public void blockPlace(BlockEvent.PlaceEvent event) {
		if (event.player.capabilities.isCreativeMode) return;
		TempBlockWSD tempBlock = TempBlockWSD.get(event.world);
		if (tempBlock == null) return;
		if (event.block == Blocks.lit_furnace) {
			tempBlock.removingTempBlock(event.x, event.y, event.z, event.world.getTotalWorldTime() + 60L);
		}
	}

	@SubscribeEvent
	public void blockBreak(BlockEvent.BreakEvent event) {
		TempBlockWSD tempBlock = TempBlockWSD.get(event.world);
		if (tempBlock == null) return;
		if (tempBlock.chunkLoad.isEmpty()) return;

		for (int i = 0; i < tempBlock.chunkLoad.size(); ++i) {
			TempBlockChunk tbc = tempBlock.chunkLoad.get(i);
			if (tbc.tempBlock.isEmpty()) continue;
			for (int j = 0; j < tbc.tempBlock.size(); ++j) {
				TempBlock tb = tbc.tempBlock.get(j);
				if (tb.x == event.x && tb.y == event.y && tb.z == event.z) 
					tbc.tempBlock.remove(j--);
			}
			if (tbc.tempBlock.isEmpty()) {
				tbc.tempBlock.clear();
				tbc.tempBlock.trimToSize();
			}
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
