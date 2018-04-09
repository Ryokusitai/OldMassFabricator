package oldmassfabricator.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import oldmassfabricator.OldMassFabricator;

public class PacketHandlerOMF
{
	// このMOD用のSimpleNetworkWrapperを生成。チャンネルの文字列は固有であれば何でも良い。MODIDの利用を推奨。
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(OldMassFabricator.MODID);
	
	public static void init()
	{
		// IMessageHandlerクラスとMessageクラスの登録
		INSTANCE.registerMessage(OMFSyncPKTHandler.class, OMFSyncPKT.class, 0, Side.CLIENT);
	}

}
