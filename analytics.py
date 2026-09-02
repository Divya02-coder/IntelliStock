
import mysql.connector
import pandas as pd
import matplotlib.pyplot as plt


# -----------------------------
# MySQL Database Connection
# -----------------------------
try:
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="Devashri236*",
        database="intellistock"
    )

    print("Connected to MySQL successfully.")

except mysql.connector.Error as err:
    print(f"Database connection failed: {err}")
    exit()


# -----------------------------
# Fetch Sales Data
# -----------------------------
query = """
    SELECT
        sku,
        SUM(quantity) AS total_sales
    FROM sales
    GROUP BY sku
    ORDER BY total_sales DESC
"""

try:
    sales = pd.read_sql(query, conn)

except Exception as err:
    print(f"Error fetching sales data: {err}")
    conn.close()
    exit()


# -----------------------------
# Display Sales Data
# -----------------------------
print("\nTotal Sales per Product:")
print(sales)


# -----------------------------
# Sales Visualization
# -----------------------------
if not sales.empty:

    plt.figure(figsize=(10, 6))

    plt.bar(
        sales["sku"].astype(str),
        sales["total_sales"]
    )

    plt.title("Total Sales per Product")
    plt.xlabel("SKU")
    plt.ylabel("Units Sold")

    plt.xticks(rotation=45)
    plt.tight_layout()

    # Save dashboard image
    plt.savefig(
        "sales_dashboard.png",
        dpi=300,
        bbox_inches="tight"
    )

    plt.show()

else:
    print("No sales data available for analysis.")


# -----------------------------
# Close Database Connection
# -----------------------------
conn.close()

print("\nMySQL connection closed.")

