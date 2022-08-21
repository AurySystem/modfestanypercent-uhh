package gay.aurum.TelePads;

import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelePads implements ModInitializer {
	public static String MODID = "goldtelepads";
	public static Identifier ID(String name){return new Identifier(MODID, name);}
	public static final Logger LOGGER = LoggerFactory.getLogger("Gold Telepads");

	public static final Block TelePad = Registry.register(Registry.BLOCK, ID("telepad"),
			new TelepadBlock(QuiltBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).sounds(BlockSoundGroup.METAL).resistance(1f)  ));

	public static final Item TelePadItem = Registry.register(Registry.ITEM, ID("telepad"),
			new BlockItem(TelePad, new QuiltItemSettings().group(ItemGroup.DECORATIONS)));

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("{} Loaded! ^^^w^^^", mod.metadata().name());
	}
}
