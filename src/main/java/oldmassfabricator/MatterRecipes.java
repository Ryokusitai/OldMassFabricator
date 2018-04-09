package oldmassfabricator;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MatterRecipes
{
	public static void registerRecipes()
	{
		// solidMatter+のマターとの相互変換レシピ追加
		try {
			registerSolidMatterPlus();
		} catch (NoSuchMethodError e) {
			e.printStackTrace();
		}

		// 鉱石辞書から取得
		ItemStack solidMatter = new ItemStack(OldMassFabricator.solidMatter);

		// 石、ガラス、草ブロック、苔石、砂、雪、水、溶岩
		GameRegistry.addRecipe(new ItemStack(Blocks.stone, 16), "   ", " M ", "   ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.glass, 32), " M ", "M M", " M ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.grass, 16), "   ", "M  ", "M  ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.mossy_cobblestone, 16), "   ", " M ", "M M", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.sand, 16), "   ", "  M", " M ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.snow, 4), "M M", "   ", "   ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.water, 1), "   ", " M ", " M ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.lava, 1), " M ", " M ", " M ", 'M', solidMatter);
		// 鉄鉱石、金鉱石、黒曜石、ネザーラック、グロウストーン
		GameRegistry.addRecipe(new ItemStack(Blocks.iron_ore, 2), "M M", " M ", "M M", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.gold_ore,2), " M ", "MMM", " M ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.obsidian, 12), "M M", "M M", "   ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.netherrack, 16), "  M", " M ", "M  ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.glowstone, 8), " M ", "M M", "MMM", 'M', solidMatter);
		// オーク、サボテン、蔦、羊毛
		GameRegistry.addRecipe(new ItemStack(Blocks.log, 8, 0), " M ", "   ", "   ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.cactus, 48), " M ", "MMM", "M M", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.vine, 24), "M  ", "M  ", "M  ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.wool, 12), "M M", "   ", " M ", 'M', solidMatter);
		// 石炭、ダイヤ、レッドストーン、ラピスラズリ
		GameRegistry.addRecipe(new ItemStack(Items.coal, 20), "  M", "M  ", "  M", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Items.diamond, 1), "MMM", "MMM", "MMM", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Items.redstone, 24), "   ", " M ", "MMM", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Items.dye, 9, 4), " M ", " M ", " MM", 'M', solidMatter);
		// 羽、雪球、火薬、ねんど、カカオ豆、イカ墨、さとうきび、火打石、骨
		GameRegistry.addRecipe(new ItemStack(Items.feather, 32), " M ", " M ", "M M", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Items.snowball, 16), "   ", "   ", "MMM", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Items.gunpowder, 15), "MMM", "M  ", "MMM", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Items.clay_ball, 48), "MM ", "M  ", "MM ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Items.dye, 32, 3), "MM ", "  M", "MM ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Items.dye, 48), " MM", " MM", " M ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Items.reeds, 48), "M M", "M M", "M M", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Items.flint, 32), " M ", "MM ", "MM ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Items.bone, 32), "M  ", "MM ", "M  ", 'M', solidMatter);
		// ゴムの素、イリジウム
		GameRegistry.addRecipe(new ItemStack(IC2Items.getItem("iridiumOre").getItem(), 1), "MMM", " M ", "MMM", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(IC2Items.getItem("resin").getItem(), 21), "M M", "   ", "M M", 'M', solidMatter);
		// ポドゾル、石レンガ、銅、鈴
		GameRegistry.addRecipe(new ItemStack(Blocks.dirt, 24, 2), "   ", "M M", "MMM", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(Blocks.stonebrick, 48), "MM ", "MM ", "M  ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(IC2Items.getItem("copperOre").getItem(), 5), "  M", "M M", "   ", 'M', solidMatter);
		GameRegistry.addRecipe(new ItemStack(IC2Items.getItem("tinOre").getItem(), 5), "   ", "M M", "  M", 'M', solidMatter);


	}

	@Optional.Method(modid="solidMatter")
	public static void registerSolidMatterPlus()
	{
		IndefiniteClassLoader solidMatterPlus = new IndefiniteClassLoader("solidmatter.SolidMatterPlus");
		Item uuMatter = solidMatterPlus.getItem("uuMatter");

		GameRegistry.addShapelessRecipe(new ItemStack(OldMassFabricator.solidMatter), new ItemStack(uuMatter));
		GameRegistry.addShapelessRecipe(new ItemStack(uuMatter), new ItemStack(OldMassFabricator.solidMatter));
	}

}
