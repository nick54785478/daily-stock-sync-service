# 分散式台股即時行情處理系統 (TWSE Data Pipeline)

這是一個基於 事件驅動架構 (Event-Driven Architecture) 打造的台股行情接入系統。

系統從台灣證券交易所 (TWSE) 抓取即時資料 (https://openapi.twse.com.tw/v1/exchangeReport/STOCK_DAY_ALL) ，
經過分散式中介軟體處理後，最終以 **領域驅動設計 (DDD)** 的規範進入後端微服務。

## 系統架構圖

系統採用解耦設計，確保每一層都能獨立擴展與維護：
 
    TWSE API ➔ Apache NiFi ➔ Apache Kafka ➔ Spring Boot (Consumer) ➔ Database

## 技術棧

>* Data Ingestion: Apache NiFi 1.28.0 (處理 ETL 與排程)
>* Messaging: Apache Kafka (解耦與流量削峰)
>* Backend: Spring Boot 3.3.x (Java 17)
>* Architecture: Domain-Driven Design (DDD), Hexagonal Architecture
>* Infrastructure: Docker, Docker-Compose, WSL2
>* Tools: Lombok, Jackson, JPA/Hibernate

## 核心亮點

**1. 視覺化 ETL 與排程 (NiFi)**

使用 Apache NiFi 建立強健的資料接入管線，實作了以下功能：
>* 自動化調度： 透過 Cron 驅動，僅在開盤時間執行 API 抓取，降低資源損耗。
>* 資料清洗： 透過 QueryRecord (SQL-like) 即時過濾目標個股，並進行初步的欄位轉譯。
>* 背壓管理： 內建 Back Pressure 機制，確保流量湧入時不會壓垮下游系統。

**2. 業務冪等性實作 (Idempotency)**

在分散式環境下，為了避免 Kafka 訊息重複消費導致資料不一致，系統在 Application Service 層實作了雙重防護：
>* 應用層檢查： 採用 Check-then-Act 模式，在存檔前驗證 StockCode + TradeDate 的唯一性。
>* 資料庫約束： 於資料庫層級建立聯合唯一索引 (Unique Index)，作為最後一道防線。

**3. 高度解耦的領域模型 (DDD & ACL)**

>* 防腐層 (Anticorruption Layer)： 透過 BaseDataTransformer 將外部 API 格式轉為內部的 Command 物件，確保領域核心不受外部異動污染。
>* 聚合根設計 (Aggregate Root)： 將股價封裝為 Price Value Object，並由 StockDailyPrice 聚合負責維持業務一致性。
>* 六角形架構： 將 Kafka Consumer 定義為 Inbound Adapter，核心業務邏輯封裝於 Application Service，提升了系統的可測試性。


## 遭遇挑戰與解決方案

  | 挑戰項目 | 解決方案 |
  | --- | --- |
  | Docker 容器網路隔離 | 透過修改 NiFi 的 docker-compose 加入 Kafka 外部網路，並配置 extra_hosts 解決容器間的 DNS 解析問題。 |
  | Kafka Metadata Timeout | 深入研究 Kafka 通訊協定，正確配置 ADVERTISED_LISTENERS 以支援跨網路環境的 Metadata 交換。 |
  | JSON 反序列化衝突 | 針對外部 API 傳回的 Array 格式，開發 JsonParseUtil 結合 Jackson 的 TypeReference 進行安全解析。 |
  | 未預期欄位報錯 | 實施防禦性設計，配置全域 FAIL_ON_UNKNOWN_PROPERTIES 為 false，提升系統對外部 API 異動的容錯率。 |

## 待擴充 「監控/AI 助理」 

目前僅 **完成持續去同步 台股台積電股票資料。**

看哪天心血來潮再來實作 ~

# 預計規劃:

**採用 「平日定時，收盤定錨」 的混合策略**

>* NiFi 排程設定： 09:00 - 13:30：設定每 10 或 30 分鐘抓一次。
>* 14:30：執行最後一次「最終校正」抓取。

可以考慮的點: 
> 考慮在資料庫增加一個欄位 update_time，並改用 Upsert (更新或新增) 
> 邏輯：「如果同一天資料已存在，就更新收盤價與時間，而不是直接跳過」。

**TODO List:**
>* 更換資料庫 H2 (現階段只用 h2)。
>* 調整冪等機制，考慮擴充冪等表，目前僅透過 StockCode (該股票的代碼) 和 TradeDate (交易日期) 來執行
>* 將容器 (nifi、kafka、DB) 集中在同一個 docker-compose 。
>* 擴充前端頁面 (含 WebSocket 機制)。
>* 考慮建置 Ollama 來實作 AI 助理。


