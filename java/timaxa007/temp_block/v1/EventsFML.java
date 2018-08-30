package timaxa007.temp_block.v1;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class EventsFML {

	@SubscribeEvent
	public void tickWorld(TickEvent.WorldTickEvent event) {
		if (event.side.isClient()) return;
		TempBlockWSD tempBlockWSD = TempBlockWSD.get(event.world);
		if (tempBlockWSD == null) return;
		switch (event.phase) {
		case END:
			tempBlockWSD.update();
			break;
		default:break;
		}
	}

}
