■成果物名：Java顧客検索ツール

■実行方法
　① コンパイル
　　javac -encoding UTF-8 -cp "lib\mssql-jdbc-13.4.0.jre11.jar" -d . src\JavaCustomerSearch.java

　② 実行
　　java -cp ".;lib\mssql-jdbc-13.4.0.jre11.jar" JavaCustomerSearch

　③ SQL ServerにCustomerDB作成
　　テーブル：customers
　　　　　　　customer_id INT
　　　　　　　customer_name VARCHAR(50)
　　　　　　　email VARCHAR(100)