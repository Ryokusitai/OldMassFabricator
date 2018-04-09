package oldmassfabricator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class OMFSyncPKT implements IMessage
{
	private double displayEnergy;
	private int displayScrap;
	private int x;
	private int y;
	private int z;

	public OMFSyncPKT() {}

	public OMFSyncPKT(double displayEnergy, int displayScrap, int x, int y,int z)
	{
		this.displayEnergy = displayEnergy;
		this.displayScrap = displayScrap;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		displayEnergy = buf.readDouble();
		displayScrap = buf.readInt();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeDouble(displayEnergy);
		buf.writeInt(displayScrap);
		buf.writeInt(getX());
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public double getEnergy()
	{
		return displayEnergy;
	}

	public int getScrap()
	{
		return displayScrap;
	}

	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public int getZ()
	{
		return z;
	}

}
