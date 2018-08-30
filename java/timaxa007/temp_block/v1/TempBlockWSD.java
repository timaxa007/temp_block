package timaxa007.temp_block.v1;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class TempBlockWSD extends WorldSavedData {

	public static final String ID = "TempBlockWSD";
	private World world;
	public final ArrayList<TempBlock> removeBlock = new ArrayList<TempBlock>();

	public TempBlockWSD(String tag) {
		super(tag);
	}

	public void update() {
		if (removeBlock.isEmpty()) return;
		for (int i = 0; i < removeBlock.size(); ++i) {
			TempBlock tb = removeBlock.get(i);
			if (world.getTotalWorldTime() >= tb.time) {
				world.func_147480_a(tb.x, tb.y, tb.z, false);
				removeBlock.remove(i--);
			}
		}
	}

	public void removingTempBlock(final int x, final int y, final int z, final long time) {
		removeBlock.add(new TempBlock(x, y, z, time));
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
		if (!nbt.hasKey("rB", NBT.TAG_LIST)) return;
		removeBlock.clear();
		NBTTagList list = nbt.getTagList("rB", NBT.TAG_COMPOUND);
		NBTTagCompound tagcomp;
		for (int i = 0; i < list.tagCount(); ++i) {
			tagcomp = list.getCompoundTagAt(i);
			removingTempBlock(
					tagcomp.getInteger("x"),
					tagcomp.getInteger("y"),
					tagcomp.getInteger("z"),
					tagcomp.getLong("time")
					);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		if (removeBlock.isEmpty()) return;
		NBTTagList list = new NBTTagList();
		NBTTagCompound tagcomp;
		for (int i = 0; i < removeBlock.size(); ++i) {
			TempBlock tb = removeBlock.get(i);
			tagcomp = new NBTTagCompound();
			tagcomp.setInteger("x", tb.x);
			tagcomp.setInteger("y", tb.y);
			tagcomp.setInteger("z", tb.z);
			tagcomp.setLong("time", tb.time);
			list.appendTag(tagcomp);
		}
		if (list.tagCount() > 0) nbt.setTag("rB", list);
	}

}
