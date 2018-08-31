package timaxa007.temp_block.v2;

import java.util.ArrayList;

public class TempBlockChunk {

	public final int chunkX, chunkZ;
	public final ArrayList<TempBlock> tempBlock = new ArrayList<TempBlock>();

	public TempBlockChunk(final int chunkX, final int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}

}
