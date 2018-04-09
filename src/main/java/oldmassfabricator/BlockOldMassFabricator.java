package oldmassfabricator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class BlockOldMassFabricator extends BlockContainer
{
	private IIcon top;
	private IIcon front;
	private IIcon side;
	private IIcon bottom;

	public BlockOldMassFabricator() {
		super(Material.iron);
		setHardness(3F);
		setStepSound(soundTypeMetal);
		setBlockName("oldMassFabricator");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote) {
			if (player.isSneaking()) { return false;}
			player.openGui(OldMassFabricator.instance, ConstantsOMF.OldMassFabricator_GUI, world, x, y, z);
		}
		return true;

	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block id, int meta)
	{
		OldMassFabricatorTile tile = (OldMassFabricatorTile)world.getTileEntity(x, y, z);
		if (tile != null) {
			if (tile instanceof OldMassFabricatorTile) {
				for (int i = 0; i < ((OldMassFabricatorTile)tile).getSizeInventory(); i++)
				{
					ItemStack stack = ((OldMassFabricatorTile)tile).getStackInSlot(i);
					if (stack == null || stack.stackSize <= 0) {
						continue;
					}
					EntityItem item = new EntityItem(world, (double)x+0.5D, (double)y+0.5D, (double)z+0.5D, stack);
					world.spawnEntityInWorld(item);
				}
			}
			tile.onChunkUnload();
		}
		super.breakBlock(world, x, y, z, id, meta);
	}

	@Override
	public Item getItemDropped(int i, Random random, int j) {
		return IC2Items.getItem("machine").getItem();
	}

	@Override
	public int damageDropped(int i) {
		return IC2Items.getItem("machine").getItemDamage();
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		top 	= register.registerIcon("oldmassfabricator:top");
		front 	= register.registerIcon("oldmassfabricator:front");
		side 	= register.registerIcon("oldmassfabricator:side");
		bottom 	= register.registerIcon("oldmassfabricator:bottom");
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		//System.out.println(meta + " : " + side);
		if (side == 0)
			return this.bottom;
		else if (side == 1)
			return this.top;
		else if ((meta == 0 && side == 3) || meta == side)
			return this.front;
		else
			return this.side;
	}

	// 向きを判断する処理
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entLiving, ItemStack stack)
	{
		int l = MathHelper.floor_double((double)(entLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0)
		{
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}

		if (l == 1)
		{
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}

		if (l == 2)
		{
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}

		if (l == 3)
		{
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int i)
	{
		return new OldMassFabricatorTile();
	}

}
