package com.deutschebank.trade.store.repository;

import com.deutschebank.trade.store.entity.TradeStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeStoreRepository extends JpaRepository<TradeStore, String> {
}
