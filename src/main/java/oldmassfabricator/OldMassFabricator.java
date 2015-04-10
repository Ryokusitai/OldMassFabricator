package oldmassfabricator;

import oldmassfabricator.network.PacketHandlerOMF;
import ic2.api.info.Info;
import ic2.api.item.IC2Items;
import chocolate.ObjHandlerChoco;
import peaa.PEAACore;
import peaa.utils.GuiHandlerPEAA;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = OldMassFabricator.MODID, name =  OldMassFabricator.MODNAME, version =  OldMassFabricator.VERSION, dependencies="required-after:IC2;after:solidMatter")
public class OldMassFabricator
{
	public static final String MODID = "OldMassFabricator";
	public static final String MODNAME = "OldMassFabricator";
	public static final String VERSION = "@VERSION@";

	@Instance(MODID)
	public static OldMassFabricator instance;

	public static Block oldMassFabricator = new BlockOldMassFabricator().setCreativeTab(CreativeTabs.tabBlock);
	public static Item solidMatter = new Item().setUnlocalizedName("solidMatter").setTextureName(MODID.toLowerCase() + ":uuMatter")
			.setCreativeTab(CreativeTabs.tabMaterials);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(OldMassFabricator.instance, new GuiHandlerOMF());

		GameRegistry.registerBlock(oldMassFabricator, "Old MassFabricator");
		GameRegistry.registerItem(solidMatter, "nameSolidMatter");

		try {
			CreativeTabs tabIC2 = (CreativeTabs)Info.ic2ModInstance.getClass().getField("tabIC2").get(Info.ic2ModInstance);
			oldMassFabricator.setCreativeTab(tabIC2);
			solidMatter.setCreativeTab(tabIC2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// とりあえず現在のマター製造機と時計で旧マター製造機
		GameRegistry.addShapelessRecipe(new ItemStack(oldMassFabricator),
				new ItemStack(IC2Items.getItem("massFabricator").getItem(), 1, IC2Items.getItem("massFabricator").getItemDamage()),
				new ItemStack(Items.clock));

		MatterRecipes.registerRecipes();
		
		// GUIに表示するデータの同期用
		PacketHandlerOMF.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.registerTileEntity(OldMassFabricatorTile.class, "OldMassFabricator Tile");
	}

}
