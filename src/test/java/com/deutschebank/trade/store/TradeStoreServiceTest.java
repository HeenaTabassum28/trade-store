package com.deutschebank.trade.store;

import com.deutschebank.trade.store.entity.TradeStore;
import com.deutschebank.trade.store.repository.TradeStoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeStoreServiceTest {

    @Mock
    private TradeStoreRepository tradeStoreRepository;

    @InjectMocks
    private TradeStoreService tradeStoreService;

    @Test
    void verifyAddTradeSavesNewTradeWithValidMaturityDate() throws Exception {
        when(tradeStoreRepository.findById(any())).thenReturn(Optional.empty());
        when(tradeStoreRepository.save(any())).thenReturn(null);
        tradeStoreService.addTrade(getTradeStore("2022-03-04", 1, null));
        verify(tradeStoreRepository, Mockito.times(1)).save(any());
    }

    @Test
    void verifyAddTradeThrowsExceptionIfNewTradeHavingMaturityDateBeforeCurrentDate() {
        when(tradeStoreRepository.findById(any())).thenReturn(Optional.empty());
        assertEquals("Maturity Date should be after current date"
                , Assertions.assertThrows(Exception.class, () -> tradeStoreService.addTrade(getTradeStore("2020-03-04", 1, null))).getMessage());
        verify(tradeStoreRepository, never()).save(any());
    }

    @Test
    void verifyAddTradeThrowsExceptionIfATradeIsUpdatedWithLowerVersion() {
        when(tradeStoreRepository.findById(any())).thenReturn(Optional.of(getTradeStore("2022-03-04", 1, "2021-03-10")));
        assertEquals("Trade version should be greater"
                , Assertions.assertThrows(Exception.class, () -> tradeStoreService.addTrade(getTradeStore("2022-03-04", 0, null))).getMessage());
        verify(tradeStoreRepository, never()).save(any());
    }

    @Test
    void verifyAddTradeUpdatesTradeIfPresent() throws Exception {
        when(tradeStoreRepository.findById(any())).thenReturn(Optional.of(getTradeStore("2021-03-28", 1, "2021-03-10")));
        tradeStoreService.addTrade(getTradeStore("2023-03-04", 2, null));
        verify(tradeStoreRepository, times(1)).save(any());
    }

    private TradeStore getTradeStore(String maturityDate, int version, String createdDate) {
        return TradeStore.builder()
                .tradeId("T1")
                .version(version)
                .counterPartyId("CP-1")
                .bookId("B1")
                .maturityDate(DateUtil.getDateFromString(maturityDate))
                .createdDate(null != createdDate ? DateUtil.getDateFromString(createdDate) : null)
                .build();
    }

    @Test
    void verifyGetTrades() {
        when(tradeStoreRepository.findAll()).thenReturn(Arrays.asList(getTradeStore("2022-04-01", 1, "2021-03-10"),
                getTradeStore("2021-03-09", 1, "2021-03-10")));
        List<TradeStore> trades = tradeStoreService.findAllTrades();
        assertEquals("N", trades.get(0).getExpired(), "Trade with maturity date after current date should not be expired");
        assertEquals("Y", trades.get(1).getExpired(), "Trade with maturity date after current date should be expired");
        verify(tradeStoreRepository, Mockito.times(1)).findAll();
    }
}