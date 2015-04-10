package oldmassfabricator.network;

import oldmassfabricator.OldMassFabricatorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class OMFSyncPKTHandler implements IMessageHandler<OMFSyncPKT, IMessage>
{
	@Override
	public IMessage onMessage(OMFSyncPKT pkt, MessageContext ctx)
	{
		TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(pkt.getX(), pkt.getY(), pkt.getZ());

		if (tile == null)
		{
			System.out.println("NULL tile entity reference int OMF sync packet!");
		}
		else {
			OldMassFabricatorTile omf = (OldMassFabricatorTile) tile;
			omf.setEnergy(pkt.getEnergy());
			omf.setScrap(pkt.getScrap());
		}

		return null;
	}

}
