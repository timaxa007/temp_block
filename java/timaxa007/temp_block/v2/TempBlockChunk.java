package timaxa007.temp_block.v2;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TempBlockChunk {

	public final int chunkX, chunkZ;
	public final ArrayList<TempBlock> tempBlock = new ArrayList<TempBlock>();

	public TempBlockChunk(final int chunkX, final int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tagcomp) {
		tagcomp.setInteger("cX", chunkX);
		tagcomp.setInteger("cZ", chunkZ);

		NBTTagList list2 = new NBTTagList();
		NBTTagCompound tagcomp2;
		for (int j = 0; j < tempBlock.size(); ++j) {
			TempBlock tb = tempBlock.get(j);
			tagcomp2 = new NBTTagCompound();
			tagcomp2.setByte("x", (byte)((chunkX * 16) - tb.x));
			tagcomp2.setInteger("y", tb.y);
			tagcomp2.setByte("z", (byte)((chunkZ * 16) - tb.z));
			tagcomp2.setLong("time", tb.time);
			list2.appendTag(tagcomp2);
		}
		if (list2.tagCount() > 0) tagcomp.setTag("tb", list2);
		return tagcomp;
	}

}
