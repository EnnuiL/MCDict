package io.github.cottonmc.mcdict;

import io.github.cottonmc.mcdict.api.DictInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.util.List;

public class MCDict implements ModInitializer {
	public static final String MODID = "mcdict";

	public static final Logger logger = LogManager.getLogger();

	@Override
	public void onInitialize() {
		List<DictInitializer> entrypoints = FabricLoader.getInstance().getEntrypoints(MODID, DictInitializer.class);
		entrypoints.forEach(DictInitializer::initDictTypes);
		ResourceLoader.get(ResourceType.SERVER_DATA).registerReloader(new PackDictLoader());
		if (FabricLoader.getInstance().isModLoaded("staticdata")) {
			entrypoints.forEach(DictInitializer::registerDicts);
			StaticDictLoader.load();
		}
	}
}
