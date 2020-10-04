package net.ludocrypt.carvepump.blocks.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class CarvedBlockEntity extends BlockEntity {

	public static BlockEntityType<?> blockEntityType;

	public byte[] pixelsCarved1 = new byte[16];
	public byte[] pixelsCarved2 = new byte[16];
	public byte[] pixelsCarved3 = new byte[16];
	public byte[] pixelsCarved4 = new byte[16];
	public byte[] pixelsCarved5 = new byte[16];
	public byte[] pixelsCarved6 = new byte[16];
	public byte[] pixelsCarved7 = new byte[16];
	public byte[] pixelsCarved8 = new byte[16];
	public byte[] pixelsCarved9 = new byte[16];
	public byte[] pixelsCarved10 = new byte[16];
	public byte[] pixelsCarved11 = new byte[16];
	public byte[] pixelsCarved12 = new byte[16];
	public byte[] pixelsCarved13 = new byte[16];
	public byte[] pixelsCarved14 = new byte[16];
	public byte[] pixelsCarved15 = new byte[16];
	public byte[] pixelsCarved16 = new byte[16];

	public CarvedBlockEntity() {
		super(blockEntityType);
		for (int x = 0; x > 16; x++) {
			for (int y = 0; y > 16; y++) {
				setValue(x, y, (byte) 0);
			}
		}

	}

	public void setValue(int x, int y, byte newValue) {
		byte[][] carvedArray = get2DArray();
		carvedArray[x][y] = newValue;
		deserializeCarving(carvedArray);
	}

	public byte getValue(int x, int y) {
		byte[][] carvedArray = get2DArray();
		return carvedArray[x][y];
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		return serializeCarving(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		deserializeCarving(tag);
	}

	public byte[][] get2DArray() {
		byte[][] array = { pixelsCarved1.clone(), pixelsCarved2.clone(), pixelsCarved3.clone(), pixelsCarved4.clone(),
				pixelsCarved5.clone(), pixelsCarved6.clone(), pixelsCarved7.clone(), pixelsCarved8.clone(),
				pixelsCarved9.clone(), pixelsCarved10.clone(), pixelsCarved11.clone(), pixelsCarved12.clone(),
				pixelsCarved13.clone(), pixelsCarved14.clone(), pixelsCarved15.clone(), pixelsCarved16.clone() };
		return array;
	}

	public byte[] getArrayFrom2DArray(byte[][] carving, int i) {
		i--;
		byte[] array = {

				carving[i][0], carving[i][1], carving[i][2], carving[i][3], carving[i][4], carving[i][5], carving[i][6],
				carving[i][7], carving[i][8], carving[i][9], carving[i][10], carving[i][11], carving[i][12],
				carving[i][13], carving[i][14], carving[i][15]

		};
		return array;
	}

	public boolean isUncarved() {
		byte[][] array = get2DArray();
		boolean carved = false;
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				if (!carved) {
					carved = array[x][y] == 0 ? false : true;
				}
			}
		}
		return !carved;
	}

	public CompoundTag serializeCarving(CompoundTag tag) {
		tag.putByteArray("pixelsCarved1", pixelsCarved1);
		tag.putByteArray("pixelsCarved2", pixelsCarved2);
		tag.putByteArray("pixelsCarved3", pixelsCarved3);
		tag.putByteArray("pixelsCarved4", pixelsCarved4);
		tag.putByteArray("pixelsCarved5", pixelsCarved5);
		tag.putByteArray("pixelsCarved6", pixelsCarved6);
		tag.putByteArray("pixelsCarved7", pixelsCarved7);
		tag.putByteArray("pixelsCarved8", pixelsCarved8);
		tag.putByteArray("pixelsCarved9", pixelsCarved9);
		tag.putByteArray("pixelsCarved10", pixelsCarved10);
		tag.putByteArray("pixelsCarved11", pixelsCarved11);
		tag.putByteArray("pixelsCarved12", pixelsCarved12);
		tag.putByteArray("pixelsCarved13", pixelsCarved13);
		tag.putByteArray("pixelsCarved14", pixelsCarved14);
		tag.putByteArray("pixelsCarved15", pixelsCarved15);
		tag.putByteArray("pixelsCarved16", pixelsCarved16);
		return tag;
	}

	public void deserializeCarving(CompoundTag tag) {
		if (tag.contains("pixelsCarved1", 7)) {
			pixelsCarved1 = tag.getByteArray("pixelsCarved1");
		}
		if (tag.contains("pixelsCarved2", 7)) {
			pixelsCarved2 = tag.getByteArray("pixelsCarved2");
		}
		if (tag.contains("pixelsCarved3", 7)) {
			pixelsCarved3 = tag.getByteArray("pixelsCarved3");
		}
		if (tag.contains("pixelsCarved4", 7)) {
			pixelsCarved4 = tag.getByteArray("pixelsCarved4");
		}
		if (tag.contains("pixelsCarved5", 7)) {
			pixelsCarved5 = tag.getByteArray("pixelsCarved5");
		}
		if (tag.contains("pixelsCarved6", 7)) {
			pixelsCarved6 = tag.getByteArray("pixelsCarved6");
		}
		if (tag.contains("pixelsCarved7", 7)) {
			pixelsCarved7 = tag.getByteArray("pixelsCarved7");
		}
		if (tag.contains("pixelsCarved8", 7)) {
			pixelsCarved8 = tag.getByteArray("pixelsCarved8");
		}
		if (tag.contains("pixelsCarved9", 7)) {
			pixelsCarved9 = tag.getByteArray("pixelsCarved9");
		}
		if (tag.contains("pixelsCarved10", 7)) {
			pixelsCarved10 = tag.getByteArray("pixelsCarved10");
		}
		if (tag.contains("pixelsCarved11", 7)) {
			pixelsCarved11 = tag.getByteArray("pixelsCarved11");
		}
		if (tag.contains("pixelsCarved12", 7)) {
			pixelsCarved12 = tag.getByteArray("pixelsCarved12");
		}
		if (tag.contains("pixelsCarved13", 7)) {
			pixelsCarved13 = tag.getByteArray("pixelsCarved13");
		}
		if (tag.contains("pixelsCarved14", 7)) {
			pixelsCarved14 = tag.getByteArray("pixelsCarved14");
		}
		if (tag.contains("pixelsCarved15", 7)) {
			pixelsCarved15 = tag.getByteArray("pixelsCarved15");
		}
		if (tag.contains("pixelsCarved16", 7)) {
			pixelsCarved16 = tag.getByteArray("pixelsCarved16");
		}
	}

	public void deserializeCarving(byte[][] carving) {
		pixelsCarved1 = getArrayFrom2DArray(carving, 1);
		pixelsCarved2 = getArrayFrom2DArray(carving, 2);
		pixelsCarved3 = getArrayFrom2DArray(carving, 3);
		pixelsCarved4 = getArrayFrom2DArray(carving, 4);
		pixelsCarved5 = getArrayFrom2DArray(carving, 5);
		pixelsCarved6 = getArrayFrom2DArray(carving, 6);
		pixelsCarved7 = getArrayFrom2DArray(carving, 7);
		pixelsCarved8 = getArrayFrom2DArray(carving, 8);
		pixelsCarved9 = getArrayFrom2DArray(carving, 9);
		pixelsCarved10 = getArrayFrom2DArray(carving, 10);
		pixelsCarved11 = getArrayFrom2DArray(carving, 11);
		pixelsCarved12 = getArrayFrom2DArray(carving, 12);
		pixelsCarved13 = getArrayFrom2DArray(carving, 13);
		pixelsCarved14 = getArrayFrom2DArray(carving, 14);
		pixelsCarved15 = getArrayFrom2DArray(carving, 15);
		pixelsCarved16 = getArrayFrom2DArray(carving, 16);
	}

	@Environment(EnvType.CLIENT)
	public static byte[][] getCarvingFromStack(ItemStack stack) {
		byte[] stackPixelsCarved1 = null;
		byte[] stackPixelsCarved2 = null;
		byte[] stackPixelsCarved3 = null;
		byte[] stackPixelsCarved4 = null;
		byte[] stackPixelsCarved5 = null;
		byte[] stackPixelsCarved6 = null;
		byte[] stackPixelsCarved7 = null;
		byte[] stackPixelsCarved8 = null;
		byte[] stackPixelsCarved9 = null;
		byte[] stackPixelsCarved10 = null;
		byte[] stackPixelsCarved11 = null;
		byte[] stackPixelsCarved12 = null;
		byte[] stackPixelsCarved13 = null;
		byte[] stackPixelsCarved14 = null;
		byte[] stackPixelsCarved15 = null;
		byte[] stackPixelsCarved16 = null;
		if (stack.hasTag()) {
			CompoundTag tag = stack.getSubTag("BlockEntityTag");
			if (tag != null) {
				if (tag.contains("pixelsCarved1", 7)) {
					stackPixelsCarved1 = tag.getByteArray("pixelsCarved1");
				}
				if (tag.contains("pixelsCarved2", 7)) {
					stackPixelsCarved2 = tag.getByteArray("pixelsCarved2");
				}
				if (tag.contains("pixelsCarved3", 7)) {
					stackPixelsCarved3 = tag.getByteArray("pixelsCarved3");
				}
				if (tag.contains("pixelsCarved4", 7)) {
					stackPixelsCarved4 = tag.getByteArray("pixelsCarved4");
				}
				if (tag.contains("pixelsCarved5", 7)) {
					stackPixelsCarved5 = tag.getByteArray("pixelsCarved5");
				}
				if (tag.contains("pixelsCarved6", 7)) {
					stackPixelsCarved6 = tag.getByteArray("pixelsCarved6");
				}
				if (tag.contains("pixelsCarved7", 7)) {
					stackPixelsCarved7 = tag.getByteArray("pixelsCarved7");
				}
				if (tag.contains("pixelsCarved8", 7)) {
					stackPixelsCarved8 = tag.getByteArray("pixelsCarved8");
				}
				if (tag.contains("pixelsCarved9", 7)) {
					stackPixelsCarved9 = tag.getByteArray("pixelsCarved9");
				}
				if (tag.contains("pixelsCarved10", 7)) {
					stackPixelsCarved10 = tag.getByteArray("pixelsCarved10");
				}
				if (tag.contains("pixelsCarved11", 7)) {
					stackPixelsCarved11 = tag.getByteArray("pixelsCarved11");
				}
				if (tag.contains("pixelsCarved12", 7)) {
					stackPixelsCarved12 = tag.getByteArray("pixelsCarved12");
				}
				if (tag.contains("pixelsCarved13", 7)) {
					stackPixelsCarved13 = tag.getByteArray("pixelsCarved13");
				}
				if (tag.contains("pixelsCarved14", 7)) {
					stackPixelsCarved14 = tag.getByteArray("pixelsCarved14");
				}
				if (tag.contains("pixelsCarved15", 7)) {
					stackPixelsCarved15 = tag.getByteArray("pixelsCarved15");
				}
				if (tag.contains("pixelsCarved16", 7)) {
					stackPixelsCarved16 = tag.getByteArray("pixelsCarved16");
				}
			}
		}

		byte[][] carving = { stackPixelsCarved1, stackPixelsCarved2, stackPixelsCarved3, stackPixelsCarved4,
				stackPixelsCarved5, stackPixelsCarved6, stackPixelsCarved7, stackPixelsCarved8, stackPixelsCarved9,
				stackPixelsCarved10, stackPixelsCarved11, stackPixelsCarved12, stackPixelsCarved13, stackPixelsCarved14,
				stackPixelsCarved15, stackPixelsCarved16 };
		return carving;
	}

}
