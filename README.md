# Spring Boot JPA Insert Benchmark

本專案用於比較不同主鍵/實作策略下，`insert` 效能差異，重點觀察：

- `application.properties`
- `id`
- `saveAll` vs. `save`（批次 vs. 個別）
- `@Transactional`（有無）

目前有 3 個分支：

- `main`
- `uuid`
- `persistable`

## 環境需求

- Java 17+
- Maven 3.8+
- Spring Boot
- 可連線資料庫（依專案設定）

## 啟動方式

```bash
mvn spring-boot:run
```

啟動後預設可透過 `http://localhost:8080` 呼叫 API。

## 測試 API（Insert）

Controller 路徑前綴：`/products`

- `POST /products/batch-insert-test?count={n}`
- `POST /products/insert-individual-test?count={n}`

範例（`count=100`）：

```bash
curl -X POST "http://localhost:8080/products/batch-insert-test?count=100"
curl -X POST "http://localhost:8080/products/insert-individual-test?count=100"
```

## 分支用途說明

- `main`：基準版本
- `uuid`：採用 UUID 相關策略
- `persistable`：採用 `Persistable` 判斷新舊實體策略

## 效能比較（batch vs. individual）

| Branch | Batch Insert | Individual Insert |
|---|---:|---:|
| `main` | 3.998 s | 14.991 s |
| `uuid` | 0.332 s | 18.857 s |
| `persistable` | 0.623 s | 18.647 s |

