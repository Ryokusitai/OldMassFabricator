package oldmassfabricator;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oldmassfabricator.container.OldMassFabricatorContainer;

public class GuiHandlerOMF implements IGuiHandler
{
	/* サーバー側の処理 */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);

		if (ID == ConstantsOMF.OldMassFabricator_GUI) {
			if (tile != null && tile instanceof OldMassFabricatorTile) {
				return new OldMassFabricatorContainer(player.inventory, (OldMassFabricatorTile) tile);
			}
		}

		return null;
	}

	/* クライアント側の処理 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);

		if (ID == ConstantsOMF.OldMassFabricator_GUI) {
			if (tile != null && tile instanceof OldMassFabricatorTile) {
				return new GuiOldMassFabricator(player.inventory, (OldMassFabricatorTile) tile);
			}
		}

		return null;
	}

}
