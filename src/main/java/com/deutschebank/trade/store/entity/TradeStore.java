package com.deutschebank.trade.store.entity;

import com.deutschebank.trade.store.DateUtil;
import com.deutschebank.trade.store.TradeStoreService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeStore {
    @Id
    private String tradeId;
    private Integer version;
    private String counterPartyId;
    private String bookId;
    private Date maturityDate;
    private Date createdDate;

    @Transient
    private String expired;

    public String getExpired() {
        return maturityDate.after(DateUtil.todayDate()) ? "N" : "Y";
    }


}
