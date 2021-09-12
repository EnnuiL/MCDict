package io.github.cottonmc.mcdict.api;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.function.Supplier;

import io.github.cottonmc.mcdict.MCDict;

public class IntDict<T> extends SimpleDict<T, Integer> {
	private final Object2IntMap<T> values;

	public IntDict(Identifier id, Registry<T> registry, Supplier<TagGroup<T>> group) {
		super(id, Integer.class, registry, group);
		this.values = new Object2IntArrayMap<>();
	}
	
	@Override
	protected void loadPendingTags() {
		if (hasPendingTags && !this.pendingTags.isEmpty()) {
			if (!group.get().getTags().isEmpty()) {
				Map<Map<Identifier, Integer>, Boolean> pending = Map.copyOf(this.pendingTags);
				
				pending.forEach((tags, override) -> {
					tags.forEach((tagId, value) -> {
						Tag<T> tag = group.get().getTag(tagId);
						if (tag != null) {
							for (T t : tag.values()) {
								if (!values.containsKey(t) || override) values.put(t, value.intValue());
							}
						} else {
							MCDict.logger.error("Dict references tag " + tagId.toString() + " that does not exist");
						}
					});
					this.pendingTags.remove(tags);
				});

				this.hasPendingTags = false;
			}
		}
	}

	@Override
	public boolean contains(T entry) {
		this.loadPendingTags();
		return values.containsKey(entry);
	}

	@Override
	public Collection<T> keys() {
		this.loadPendingTags();
		return values.keySet();
	}

	@Override
	public Map<T, Integer> values() {
		this.loadPendingTags();
		return values;
	}

	@Override
	@Deprecated
	public Integer get(T entry) {
		return values.getInt(entry);
	}

	public int getInt(T entry) {
		this.loadPendingTags();
		return values.getInt(entry);
	}
}
