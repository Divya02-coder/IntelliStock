import sqlite3
import pandas as pd
import matplotlib.pyplot as plt

conn = sqlite3.connect("intellistock.db")

sales = pd.read_sql_query(
    """
    SELECT sku,
           SUM(quantity) as total_sales
    FROM sales
    GROUP BY sku
    """,
    conn
)

print(sales)

plt.figure(figsize=(8,5))

plt.bar(
    sales["sku"],
    sales["total_sales"]
)

plt.title("Total Sales per Product")
plt.xlabel("SKU")
plt.ylabel("Units Sold")

plt.tight_layout()

plt.savefig("sales_dashboard.png", dpi=300)

plt.show()

conn.close()