import sqlite3
import pandas as pd
import matplotlib.pyplot as plt

# Connect to SQLite database
conn = sqlite3.connect("intellistock.db")

# Read inventory data
inventory = pd.read_sql_query("""
SELECT
    sku,
    name,
    quantity,
    reorder_threshold
FROM products
""", conn)

print(inventory)

# Create chart
plt.figure(figsize=(9,5))

plt.bar(
    inventory["sku"],
    inventory["quantity"],
    label="Current Stock"
)

# Draw reorder threshold line for each product
for i in range(len(inventory)):
    plt.hlines(
        inventory["reorder_threshold"][i],
        i-0.35,
        i+0.35,
        colors="red",
        linestyles="dashed"
    )

plt.title("IntelliStock Inventory Dashboard")
plt.xlabel("Product SKU")
plt.ylabel("Current Stock Quantity")

plt.legend(["Reorder Threshold", "Current Stock"])

plt.grid(axis='y', linestyle='--', alpha=0.5)

plt.tight_layout()

# Save image
plt.savefig("inventory_dashboard.png", dpi=300)

plt.show()

conn.close()