package com.test.util;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.shards.ShardId;
import org.hibernate.shards.ShardedConfiguration;
import org.hibernate.shards.cfg.ConfigurationToShardConfigurationAdapter;
import org.hibernate.shards.strategy.ShardStrategy;
import org.hibernate.shards.strategy.ShardStrategyFactory;
import org.hibernate.shards.strategy.ShardStrategyImpl;
import org.hibernate.shards.strategy.access.SequentialShardAccessStrategy;
import org.hibernate.shards.strategy.access.ShardAccessStrategy;
import org.hibernate.shards.strategy.resolution.ShardResolutionStrategy;
import org.hibernate.shards.strategy.selection.ShardSelectionStrategy;

import com.test.shard.strategy.UserShardResolutionStrategy;
import com.test.shard.strategy.UserShardSelectionStrategy;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class HibernateShardUtil {
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	static {
		try {
			Configuration config = new Configuration();
			config.configure("/config/hibernate1.cfg.xml");
			config.addResource("com/test/domain/User.hbm.xml");
			List shardConfigs = new ArrayList();
			shardConfigs.add(new ConfigurationToShardConfigurationAdapter(
					new Configuration().configure("/config/hibernate1.cfg.xml")));
			shardConfigs.add(new ConfigurationToShardConfigurationAdapter(
					new Configuration().configure("/config/hibernate2.cfg.xml")));
			ShardStrategyFactory shardStrategyFactory = buildShardStrategyFactory();
			ShardedConfiguration shardedConfig = new ShardedConfiguration(
					config, shardConfigs, shardStrategyFactory);
			sessionFactory = shardedConfig.buildShardedSessionFactory();
		} catch (Throwable ex) {
			ex.printStackTrace();
			sessionFactory = null;
		}
	}

	static ShardStrategyFactory buildShardStrategyFactory() {
		ShardStrategyFactory shardStrategyFactory = new ShardStrategyFactory() {

			@Override
			public ShardStrategy newShardStrategy(List<ShardId> shardIds) {
				ShardSelectionStrategy pss = new UserShardSelectionStrategy();
				ShardResolutionStrategy prs = new UserShardResolutionStrategy(
						shardIds);
				ShardAccessStrategy pas = new SequentialShardAccessStrategy();
				return new ShardStrategyImpl(pss, prs, pas);
			}
		};
		return shardStrategyFactory;
	}
}
