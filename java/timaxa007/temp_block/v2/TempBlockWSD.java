package timaxa007.temp_block.v2;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants.NBT;

public class TempBlockWSD extends WorldSavedData {

	public static final String ID = "TempBlockWSD";
	private World world;
	public final ArrayList<TempBlockChunk>
	chunkLoad = new ArrayList<TempBlockChunk>(),
	chunkUnload = new ArrayList<TempBlockChunk>();

	public TempBlockWSD(String tag) {
		super(tag);
	}

	public void update() {
		if (chunkLoad.isEmpty()) return;

		for (int i = 0; i < chunkLoad.size(); ++i) {
			TempBlockChunk tbc = chunkLoad.get(i);
			if (tbc.tempBlock.isEmpty()) continue;
			for (int j = 0; j < tbc.tempBlock.size(); ++j) {
				TempBlock tb = tbc.tempBlock.get(j);
				if (world.getTotalWorldTime() >= tb.time) {
					world.func_147480_a((tbc.chunkX * 16) + (tb.xz & 0b1111), tb.y, (tbc.chunkZ * 16) + ((tb.xz >> 4) & 0b1111), false);
					tbc.tempBlock.remove(j--);
				}
			}
			if (tbc.tempBlock.isEmpty()) {
				tbc.tempBlock.clear();
				tbc.tempBlock.trimToSize();
			}
		}

	}

	public void removingTempBlock(final int x, final int y, final int z, final long time) {
		int chunkX = x / 16;
		int chunkZ = z / 16;
		int i;
		TempBlockChunk tbc = null;

		if (!chunkLoad.isEmpty())
			for (i = 0; i < chunkLoad.size(); ++i) {
				TempBlockChunk tbcc = chunkLoad.get(i);
				if (tbcc.chunkX == chunkX && tbcc.chunkZ == chunkZ) {
					tbc = tbcc;
					break;
				}
			}

		if (!chunkUnload.isEmpty() && tbc == null)
			for (i = 0; i < chunkUnload.size(); ++i) {
				TempBlockChunk tbcc = chunkUnload.get(i);
				if (tbcc.chunkX == chunkX && tbcc.chunkZ == chunkZ) {
					tbc = tbcc;
					break;
				}
			}

		if (tbc == null)
			tbc = new TempBlockChunk(chunkX, chunkZ);

		tbc.tempBlock.add(new TempBlock((byte)((z % 16) << 4 | (x % 16)), y, time));

		if (world.getChunkProvider().chunkExists(chunkX, chunkZ))
			chunkLoad.add(tbc); else chunkUnload.add(tbc);
	}

	public void chunkLoad(Chunk chunk) {
		if (chunkUnload.isEmpty()) return;
		for (int i = 0; i < chunkUnload.size(); ++i) {
			TempBlockChunk tbc = chunkUnload.get(i);
			if (tbc.chunkX == chunk.xPosition && tbc.chunkZ == chunk.zPosition) {
				chunkLoad.add(tbc);
				chunkUnload.remove(i--);
				break;
			}
		}
	}

	public void chunkUnload(Chunk chunk) {
		if (chunkLoad.isEmpty()) return;
		for (int i = 0; i < chunkLoad.size(); ++i) {
			TempBlockChunk tbc = chunkLoad.get(i);
			if (tbc.chunkX == chunk.xPosition && tbc.chunkZ == chunk.zPosition) {
				chunkUnload.add(tbc);
				chunkLoad.remove(i--);
				break;
			}
		}
	}

	public static TempBlockWSD get(final World world) {
		if (world.mapStorage == null) return null;
		TempBlockWSD data = (TempBlockWSD)world.mapStorage.loadData(TempBlockWSD.class, ID);
		if (data == null) {
			data = new TempBlockWSD(ID);
			data.markDirty();
			world.mapStorage.setData(ID, data);
		}
		data.world = world;
		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (!nbt.hasKey("tbc", NBT.TAG_LIST)) return;
		chunkLoad.clear();
		chunkUnload.clear();
		NBTTagList list = nbt.getTagList("tbc", NBT.TAG_COMPOUND);
		NBTTagCompound tagcomp;
		for (int i = 0; i < list.tagCount(); ++i) {
			tagcomp = list.getCompoundTagAt(i);
			TempBlockChunk tbc = new TempBlockChunk(tagcomp.getInteger("cX"), tagcomp.getInteger("cZ"));
			NBTTagList list2 = tagcomp.getTagList("tb", NBT.TAG_COMPOUND);
			NBTTagCompound tagcomp2;
			for (int j = 0; j < list2.tagCount(); ++j) {
				tagcomp2 = list2.getCompoundTagAt(j);
				tbc.tempBlock.add(new TempBlock(tagcomp2.getByte("xz"), tagcomp2.getInteger("y"), tagcomp2.getLong("time")));
			}
			chunkUnload.add(tbc);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList list = new NBTTagList();
		NBTTagCompound tagcomp;

		if (!chunkLoad.isEmpty())
			for (int i = 0; i < chunkLoad.size(); ++i) {
				TempBlockChunk tbc = chunkLoad.get(i);
				if (tbc.tempBlock.isEmpty()) continue;
				tagcomp = new NBTTagCompound();
				tagcomp.setInteger("cX", tbc.chunkX);
				tagcomp.setInteger("cZ", tbc.chunkZ);

				NBTTagList list2 = new NBTTagList();
				NBTTagCompound tagcomp2;
				for (int j = 0; j < tbc.tempBlock.size(); ++j) {
					TempBlock tb = tbc.tempBlock.get(j);
					tagcomp2 = new NBTTagCompound();
					tagcomp2.setByte("xz", tb.xz);
					tagcomp2.setInteger("y", tb.y);
					tagcomp2.setLong("time", tb.time);
					list2.appendTag(tagcomp2);
				}
				if (list2.tagCount() > 0) tagcomp.setTag("tb", list2);
				list.appendTag(tagcomp);
			}

		if (!chunkUnload.isEmpty())
			for (int i = 0; i < chunkUnload.size(); ++i) {
				TempBlockChunk tbc = chunkUnload.get(i);
				if (tbc.tempBlock.isEmpty()) continue;
				tagcomp = new NBTTagCompound();
				tagcomp.setInteger("cX", tbc.chunkX);
				tagcomp.setInteger("cZ", tbc.chunkZ);

				NBTTagList list2 = new NBTTagList();
				NBTTagCompound tagcomp2;
				for (int j = 0; j < tbc.tempBlock.size(); ++j) {
					TempBlock tb = tbc.tempBlock.get(j);
					tagcomp2 = new NBTTagCompound();
					tagcomp2.setByte("xz", tb.xz);
					tagcomp2.setInteger("y", tb.y);
					tagcomp2.setLong("time", tb.time);
					list2.appendTag(tagcomp2);
				}
				if (list2.tagCount() > 0) tagcomp.setTag("tb", list2);
				list.appendTag(tagcomp);
			}

		if (list.tagCount() > 0) nbt.setTag("tbc", list);
	}

}
