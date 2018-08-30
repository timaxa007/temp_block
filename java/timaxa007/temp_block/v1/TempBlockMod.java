package timaxa007.temp_block.v1;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = TempBlockMod.MODID, name = TempBlockMod.NAME, version = TempBlockMod.VERSION)
public class TempBlockMod {

	public static final String
	MODID = "temp_block",
	NAME = "Temporary Block Mod",
	VERSION = "0.01";

	@Mod.Instance(MODID)
	public static TempBlockMod instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventsForge());
		FMLCommonHandler.instance().bus().register(new EventsFML());
	}

}
