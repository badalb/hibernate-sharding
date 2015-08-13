package com.test.shard.strategy;

import org.hibernate.shards.ShardId;
import org.hibernate.shards.strategy.selection.ShardSelectionStrategy;

import com.test.domain.User;

public class UserShardSelectionStrategy implements ShardSelectionStrategy {
	public ShardId selectShardIdForNewObject(Object obj) {
		if (obj instanceof User) {
			int shardId = 1;
			String country = ((User) obj).getCountry();
			if (country.equalsIgnoreCase("India"))
				shardId = 2;
			else
				shardId = 1;
			return new ShardId(shardId);
		}
		throw new IllegalArgumentException();
	}
}