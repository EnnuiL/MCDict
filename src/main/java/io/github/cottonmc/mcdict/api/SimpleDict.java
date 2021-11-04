package io.github.cottonmc.mcdict.api;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.impl.tag.extension.TagDelegate;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.function.Supplier;

public class SimpleDict<T, V> implements Dict<T, V> {
	private final Identifier id;
	private final Class<V> type;
	private final Map<T, V> values;
	protected Registry<T> registry;
	protected Supplier<TagGroup<T>> group;

	public SimpleDict(Identifier id, Class<V> type, Registry<T> registry, Supplier<TagGroup<T>> group) {
		this.id = id;
		this.type = type;
		this.registry = registry;
		this.group = group;
		this.values = new HashMap<>();
	}

	@Override
	public void clear() {
		values().clear();
	}

	@Override
	public boolean contains(T entry) {
		return values.containsKey(entry);
	}

	@Override
	public Class<V> getType() {
		return type;
	}

	@Override
	public Collection<T> keys() {
		return values.keySet();
	}

	@Override
	public Map<T, V> values() {
		return values;
	}

	@Override
	public V get(T entry) {
		return values.get(entry);
	}

	@Override
	public T getRandom(Random random) {
		List<T> list = Lists.newArrayList(this.keys());
		return list.get(random.nextInt(list.size()));
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public Tag<T> toTag() {
		//Identifier newId = new Identifier(id.getNamespace(), "dict/" + id.getPath());

		return new TagDelegate<T>(new Identifier(id.getNamespace(), "dict/" + id.getPath()), this.group);
		//return Tag.of(values().keySet());
	}

	//TODO: libcd condition support?
	@Override
	public void fromJson(boolean replace, boolean override, JsonObject entries) throws JsonParseException {
		Map<T, V> vals = values();
		if (replace) vals.clear();
		for (Map.Entry<String, JsonElement> jsonEntry : entries.entrySet()) {
			String key = jsonEntry.getKey();
			Gson gson = new GsonBuilder().create();
			V value = gson.fromJson(jsonEntry.getValue().toString(), type);
			boolean isOptional = false;
			if (value == null) {
				throw new JsonParseException("Dict value for entry " + key + " could not be parsed into type " + type.getName());
			}
			if (key.indexOf('?') == 0) {
				key = key.substring(1);
				isOptional = true;
			}
			if (key.indexOf('#') == 0) {
				Tag<T> tag = group.get().getTag(new Identifier(key.substring(1)));
				if (tag == null) {
					if (!isOptional) throw new JsonParseException("Dict references tag " + key + " that does not exist");
					else continue;
				}
				for (T t : tag.values()) {
					if (!vals.containsKey(t) || override) vals.put(t, value);
				}
			} else {
				Optional<T> entry = registry.getOrEmpty(new Identifier(key));
				if (entry.isEmpty()) {
					if (!isOptional) throw new JsonParseException("Dict references registered object " + key + " that does not exist");
					else continue;
				}
				if (!vals.containsKey(entry.get()) || override) vals.put(entry.get(), value);
			}
		}
		TagFactory.of(group).create(new Identifier(""));
		
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.add("replace", new JsonPrimitive(false));
		JsonObject vals = new JsonObject();
		for (T t : values().keySet()) {
			vals.addProperty(registry.getId(t).toString(), values.get(t).toString());
		}
		json.add("values", vals);
		return json;
	}

}
