# jdbq [![tests](https://github.com/sigpwned/jdbq/actions/workflows/tests.yml/badge.svg)](https://github.com/sigpwned/jdbq/actions/workflows/tests.yml)  ![Maven Central](https://img.shields.io/maven-central/v/com.sigpwned/jdbq) [![javadoc](https://javadoc.io/badge2/com.sigpwned/jdbq/javadoc.svg)](https://javadoc.io/doc/com.sigpwned/jdbq)

A [JDBI](https://jdbi.org/)-inspired database access framework for Java 8+ and [Google BigQuery](https://cloud.google.com/bigquery).

## Motivation

BigQuery is one of the best data lake implementations available on the market today. While its official Java client supports several useful features for working with data stores, such as parameter bindings, there are many important patterns it does not support, such as modular row and column mappers. JDBI is the state-of-the-art tool for working with persistence stores. JDBQ is a library for using BigQuery that makes the best features of JDBI available to BigQuery users.

## Goals

* To work directly with the official Java client
* To provide the most important features from the JDBI framework
* To improve the QOL and productivity of Java developers using BigQuery

## Non-Goals

* To provide all features from the JDBI framework

## Example Usage

For the below examples, imagine a table with the following structure:

    CREATE TABLE sales(
        buyer STRING NOT NULL,
        invoice STRING NOT NULL,
        sku STRING NOT NULL,
        quantity INT64 NOT NULL,
        timestamp TIMESTAMP NOT NULL
    );
    
### Initial Setup

First, the user should create a `Jdbq` instance:

    BigQuery client=createBigQueryClient();
    Jdbq jdbq=new Jdbq(client);
    
Users may also find it useful to add a customizer to set default dataset, SQL dialect, etc.

    jdbq.getConfig().get(SqlStatements.class).addCustomizer(new StatementCustomizer() {
        @Override
        public void beforeExecution(QueryJobConfiguration.Builder stmt, StatementContext ctx) {
            stmt.setDefaultDataset(DEFAULT_DATASET_NAME).setUseLegacySql(false);
        }
    });
    
### Column Mapped Results

Consider the following query:

    long quantity=jdbq.createQuery("""
            SELECT
                SUM(quantity) AS quantity
            FROM sales
            WHERE sku=:sku
                AND timestamp BETWEEN :since AND :until""")
        .bind("sku", "abcd1234")
        .bind("since", LocalDate.of(2023, 1, 1))
        .bind("until", LocalDate.of(2023, 3, 31))
        .mapTo(Long.class)
        .one();
        
This query computes how many units of SKU `abcd1234` were sold in Q1 2023.

In this example, we see that we have named parameters in the query (e.g., `:sku`) with values provided using the `bind` method later. Next, the result is mapped to `Long` values, and then exactly one value is retrieved, otherwise an exception is thrown. This works because there are built-in `ColumnMapper` classes for most builtin types, such as `Integer`, `Long`, `Double`, `String`, `LocalDate`, and so on.

### Row Mapped Results

Now consider this code:

    Jdbq jdbq=createJdbq();
    
    record SkuSales(String sku, long quantity) {}
    
    jdbq.getConfig(RowMappers.class).register(new RowMapper<SkuSales>() {
        @Override
        public SkuSales map(FieldValueList fvs, StatementContext ctx) {
            String sku=fvs.get("sku").getStringValue();
            long quantity=fvs.get("quantity").getLongValue();
            return new SkuSales(sku, quantity);
        }
    });

    List<SkuSales> sales=jdbq.createQuery("""
            SELECT
                sku AS sku,
                SUM(quantity) AS quantity
            FROM sales
            WHERE timestamp BETWEEN :since AND :until
            GROUP BY 1
            ORDER BY 2 DESC
            LIMIT 10""")
        .bind("since", LocalDate.of(2023, 1, 1))
        .bind("until", LocalDate.of(2023, 3, 31))
        .mapTo(SkuSales.class)
        .list();

This query computes the top 10 SKUs with the most sales in Q1 2023.

In this example, we see our first `RowMapper`, which is custom code used to map a SQL query result row to a Java bean. In this case, each row is mapped to a `SkuSales` object. Note that the registering the `RowMapper` for the `SkuSales` class during initialization effectively decouples the serialization of records from business logic.

### DML

The library also supports [DML operations](https://cloud.google.com/bigquery/docs/reference/standard-sql/dml-syntax).

    Jdbq jdbq=createJdbq();

    long deleted=jdbq.createUpdate("""
            DELETE FROM sales
            WHERE sku=:sku AND quantity=0""")
        .bind("sku", "1234")
        .execute();

This query deletes all sales records with sku `1234` and quantity `0`.

### QueryFragment

JDBQ does have one important innovation over the rote JDBI feature set: the `QueryFragment`. A `QueryFragment` allows users to bundle SQL along with attributes and arguments for use in a query, which may contain other `QueryFragment` instances, and so on. For example:

    Jdbq jdbq=createJdbq();
    jdbq.get(SqlStatements.class).setTemplateEngine(new QueryFragmentTemplateEngine());
    
    record SkuSales(String sku, long quantity) {}
    
    jdbq.getConfig(RowMappers.class).register(new RowMapper<SkuSales>() {
        @Override
        public SkuSales map(FieldValueList fvs, StatementContext ctx) {
            String sku=fvs.get("sku").getStringValue();
            long quantity=fvs.get("quantity").getLongValue();
            return new SkuSales(sku, quantity);
        }
    });
    
    QueryFragment buyerPredicate;
    if(filterToBuyer != null) {
        buyerPredicate = new QueryPredicate("buyer=:buyer").bind("buyer", filterToBuyer);
    }
    else {
        buyerPredicate = new QueryPredicate("TRUE");
    }

    List<SkuSales> sales=jdbq.createQuery("""
            SELECT
                sku AS sku,
                SUM(quantity) AS quantity
            FROM sales
            WHERE timestamp BETWEEN :since AND :until
                AND (<BUYER>)
            GROUP BY 1
            ORDER BY 2 DESC
            LIMIT 10""")
        .define("BUYER", buyerPredicate)
        .bind("since", LocalDate.of(2023, 1, 1))
        .bind("until", LocalDate.of(2023, 3, 31))
        .mapTo(SkuSales.class)
        .list();

This query computes the top 10 SKUs with the most sales in Q1 2023 from the given optional buyer.

Note that the buyer predicate includes an argument. Using a `QueryFragment`, the entire predicate is self-contained because it supports not only SQL but also attributes and arguments, and is therefore reusable. If this were handled without `QueryFragment`, then the builder of the overall query would have to know about how the predicate works, which violates encapsulation and reduces reusability.

The `QueryFragment` feature allows users to divide and conquer query generation, as well as to reuse components of query generation more freely. This style of query generation is sometimes referred to as the [specification pattern](https://en.wikipedia.org/wiki/Specification_pattern).

Each `QueryFragment` has its own logical "namespace," which means that users don't have to worry about attribute or argument name overlap between `QueryFragment` instances, even when used in the same query.

## Extensibility

The following key features have been brought over from JDBI:

* Column mappers
* Row mappers
* Query customizers
* Pluggable template engines
* Custom arguments

## FAQ

### Why not just use JDBI with the Simba JDBC driver?

That is a fine option, and may work for many businesses. However, not all BigQuery features are available through the JDBC driver, and not all users are comfortable using a closed-source JDBC driver for their data lake. In short: feel free to use the JDBC option if it works for you, but it does not work for everyone.

## Roadmap

More features, such as [JDBI-style annotated methods](https://jdbi.org/#_annotated_methods), may be added to the library if there is demand.
