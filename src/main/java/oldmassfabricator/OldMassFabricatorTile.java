package oldmassfabricator;

import oldmassfabricator.network.OMFSyncPKT;
import oldmassfabricator.network.PacketHandlerOMF;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.IC2Items;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class OldMassFabricatorTile extends TileEntity implements IInventory, ISidedInventory, IEnergySink, IWrenchable
{
	private static final int[] slotsSides = new int[] {1};
	private static final int[] slotsOther = new int[] {0};
	// 0がスクラップ 1がマター4
	private ItemStack[] OMFItemStacks = new ItemStack[2];

	// ic2関連
	private double energy = 0.0D;
	private final int maxEnergy = 1100000;			// 最大で100万+ちょっと予備EUまで貯蓄可能
	private final int needEnergy = 1000000;		// マターの作成に必要なエネルギー(デフォルト100万EU)
	public boolean addedToEnergyNet = false;	// 何回もイベントを登録しないようにするための判断用
	private int scrap = 0;
	private final int scrapPoint = 5000;
	private final int scrapBoxPoint = scrapPoint * 9;
	
	private int numUsing;	// GUI同期用のフラグ

	@Override
	public void updateEntity() {
		if (!addedToEnergyNet)
		{
			if (!worldObj.isRemote)
			{
				// ic2のEUを受け取るための登録処理
				EnergyTileLoadEvent event = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(event);

			}
			this.addedToEnergyNet = true;
		}

		if (!worldObj.isRemote) {
			checkScrap();
			checkCreateMatter();
			
			// clientとの同期
			if (numUsing > 0) {
				PacketHandlerOMF.INSTANCE.sendToAll(new OMFSyncPKT(this.energy, this.scrap, this.xCoord, this.yCoord, this.zCoord));
			}
		}
	}

	public void checkScrap()
	{
		// スクラップもしくはスクラップボックスがセットされているなら
		if (isItemScrap(this.OMFItemStacks[0]))
		{
			if (this.scrap < 1000)
			{
				scrap += getScrapValue(this.OMFItemStacks[0]);
				this.OMFItemStacks[0].stackSize--;
				if (this.OMFItemStacks[0].stackSize == 0)
				{
					this.OMFItemStacks[0] = null;
				}
			}
		}
	}

	public void checkCreateMatter()
	{

		if (this.energy >= this.needEnergy)
		{
			ItemStack stack = this.OMFItemStacks[1];
			if (stack != null && stack.getItem().equals(OldMassFabricator.solidMatter))
			{
				OMFItemStacks[1].stackSize++;
			}
			else {
				OMFItemStacks[1] = new ItemStack(OldMassFabricator.solidMatter);
			}

			this.energy -= this.needEnergy;
		}
	}

	public static boolean isItemScrap(ItemStack stack)
	{
		return stack == null ? false : stack.getItem().equals(IC2Items.getItem("scrap").getItem()) || stack.getItem().equals(IC2Items.getItem("scrapBox").getItem());
	}

	public int getScrapValue(ItemStack stack)
	{
		if (stack.getItem().equals(IC2Items.getItem("scrap").getItem()))
		{
			return this.scrapPoint;
		}
		else
		{
			return this.scrapBoxPoint;
		}
	}

	@Override
	public void invalidate() {
		super.invalidate();
		onChunkUnload();
	}

	@Override
	public void onChunkUnload() {
		if (this.addedToEnergyNet)
		{
			EnergyTileUnloadEvent event = new EnergyTileUnloadEvent(this);
			MinecraftForge.EVENT_BUS.post(event);
			this.addedToEnergyNet = false;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.energy = nbt.getDouble("energy");
		this.scrap = nbt.getInteger("scrap");

		NBTTagList list = nbt.getTagList("Items", 10);
		this.OMFItemStacks = new ItemStack[getSizeInventory()];

		for (int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			byte b0 = nbt1.getByte("Slot");

			if (b0 >= 0 & b0 < this.OMFItemStacks.length)
			{
				this.OMFItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt1);
			}
		}

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setDouble("energy", energy);
		nbt.setInteger("scrap", scrap);
		NBTTagList list = new NBTTagList();

		for (int i = 0; i < this.OMFItemStacks.length; ++i)
		{
			if (this.OMFItemStacks[i] != null) {
				NBTTagCompound nbt1 = new NBTTagCompound();
				nbt1.setByte("Slot", (byte)i);
				this.OMFItemStacks[i].writeToNBT(nbt1);
				list.appendTag(nbt1);
			}
		}

		nbt.setTag("Items", list);

	}


	// ここから下 実装メソッド
	@Override
	public int getSizeInventory()
	{
		return this.OMFItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.OMFItemStacks[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int decSize)
	{
		ItemStack stack = this.OMFItemStacks[slot];

		if (stack != null)
		{
			if (stack.stackSize <= decSize)
			{
				this.OMFItemStacks[slot] = null;
			}
			else
			{
				stack = this.OMFItemStacks[slot].splitStack(decSize);
				if (this.OMFItemStacks[slot].stackSize == 0)
					this.OMFItemStacks[slot] = null;
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if (this.OMFItemStacks[slot] != null)
		{
			ItemStack stack = this.OMFItemStacks[slot];
			this.OMFItemStacks[slot] = null;
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.OMFItemStacks[slot] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName()
	{
		return "Old MassFabricator";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	// これは自分で呼ぶことはあってもバニラに呼ばれることはない？
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
		this.numUsing++;
	}

	@Override
	public void closeInventory() {
		this.numUsing--;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return slot == 1 ? false : isItemScrap(stack);
	}

	/**
	 * 上下からスクラップ。横からマター
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return (side == 0 || side == 1) ? slotsOther : slotsSides;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack,
			int side)
	{
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack,
			int side)
	{
		return slot == 1;
	}

	/**
	 * この機械が要求するエネルギー
	 * 基本的には「最大貯蓄可能量 - 現在の貯蓄量」
	 */
	@Override
	public double getDemandedEnergy()
	{
		return this.maxEnergy - this.energy;
	}

	/**
	 * この機械が一度に受け取り可能なエネルギー量
	 * 1 = LV, 2 = MV, 3 = HV, 4 = EV etc. らしい
	 */
	@Override
	public int getSinkTier()
	{
		return 3;
	}

	@Override
	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
	{
		if (energy >= maxEnergy)
		{
			return amount;
		}
		// 旧ic2のマター製造機のソースをそのまま利用
		int bonus = (int)amount;
		if (bonus > this.scrap)
		{
			bonus = this.scrap;
		}
		this.scrap -= bonus;
		this.energy += amount + 5 * bonus;
		int re = 0;
		if (this.energy > this.maxEnergy)
		{
			re = (int) (this.energy - this.maxEnergy);
			this.energy = this.maxEnergy;
		}
		return (double)re;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
	{
		return true;
	}

	// ここより下レンチ関係
	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return false;
	}

	@Override
	public short getFacing() {
		return 0;
	}

	@Override
	public void setFacing(short facing) {

	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0f;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(this.getBlockType(), 1);
	}

	// ここから下GUI表示用
	public int getProgress() {
		return (int)((this.energy / this.needEnergy) * 100);
	}

	public int getScrap() {
		return this.scrap;
	}

	public double getEnergy() {
		return this.energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public void setScrap(int scrap) {
		this.scrap = scrap;
	}

}
