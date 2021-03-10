package com.deutschebank.trade.store;

import com.deutschebank.trade.store.entity.TradeStore;
import com.deutschebank.trade.store.repository.TradeStoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TradeStoreServiceTest {

    @Mock
    private TradeStoreRepository tradeStoreRepository;

    @InjectMocks
    private TradeStoreService tradeStoreService;

    @Test
    void verifyAddTrade() {
        Mockito.when(tradeStoreRepository.save(any())).thenReturn(null);
        tradeStoreService.addTrade(TradeStore.builder().tradeId("T1").version(1).build());
        verify(tradeStoreRepository, Mockito.times(1)).save(any());
    }

    @Test
    void verifyGetTrades() {
        Mockito.when(tradeStoreRepository.findAll()).thenReturn(null);
        tradeStoreService.findAllTrades();
        verify(tradeStoreRepository, Mockito.times(1)).findAll();
    }
}