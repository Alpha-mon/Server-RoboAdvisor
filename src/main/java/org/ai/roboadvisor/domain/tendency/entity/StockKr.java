package org.ai.roboadvisor.domain.tendency.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 인자 없는 기본 생성자 필요
@Entity
@Table(name = "stock_list_kr")
public class StockKr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "stock_code")
    private String stockCode;

    @Column(nullable = false, name = "stock_name")
    private String stockName;

    @Column(nullable = false)
    private String market;

}
