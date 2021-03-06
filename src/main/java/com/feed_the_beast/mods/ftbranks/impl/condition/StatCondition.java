package com.feed_the_beast.mods.ftbranks.impl.condition;

import com.feed_the_beast.mods.ftbranks.api.RankCondition;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class StatCondition implements RankCondition
{
	public static final int EQUALS = 1;
	public static final int NOT_EQUALS = 2;
	public static final int GREATER = 3;
	public static final int GREATER_OR_EQUAL = 4;
	public static final int LESSER = 5;
	public static final int LESSER_OR_EQUAL = 6;

	private final ResourceLocation statId;
	public final int value;
	public final int valueCheck;
	private Stat<?> stat;

	public StatCondition(JsonObject json)
	{
		statId = new ResourceLocation(json.get("stat").getAsString());
		stat = Stats.CUSTOM.get(statId);
		value = json.get("value").getAsInt();

		switch (json.get("value_check").getAsString())
		{
			case "not_equals":
			case "not":
			case "!=":
				valueCheck = NOT_EQUALS;
				break;
			case "greater":
			case ">":
				valueCheck = GREATER;
				break;
			case "greater_or_equal":
			case ">=":
				valueCheck = GREATER_OR_EQUAL;
				break;
			case "lesser":
			case "<":
				valueCheck = LESSER;
				break;
			case "lesser_or_equal":
			case "<=":
				valueCheck = LESSER_OR_EQUAL;
				break;
			default:
				valueCheck = EQUALS;
		}
	}

	@Override
	public String getType()
	{
		return "stat";
	}

	@Override
	public boolean isRankActive(ServerPlayerEntity player)
	{
		int v = player.getStats().getValue(stat);

		switch (valueCheck)
		{
			case NOT_EQUALS:
				return v != value;
			case GREATER:
				return v > value;
			case GREATER_OR_EQUAL:
				return v >= value;
			case LESSER:
				return v < value;
			case LESSER_OR_EQUAL:
				return v <= value;
			default:
				return v == value;
		}
	}

	@Override
	public void save(JsonObject json)
	{
		json.addProperty("stat", statId.toString());
		json.addProperty("value", value);

		switch (valueCheck)
		{
			case NOT_EQUALS:
				json.addProperty("value_check", "!=");
				break;
			case GREATER:
				json.addProperty("value_check", ">");
				break;
			case GREATER_OR_EQUAL:
				json.addProperty("value_check", ">=");
				break;
			case LESSER:
				json.addProperty("value_check", "<");
				break;
			case LESSER_OR_EQUAL:
				json.addProperty("value_check", "<=");
				break;
			default:
				json.addProperty("value_check", "==");
		}
	}
}