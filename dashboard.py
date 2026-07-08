import sqlite3
import os
import pandas as pd
import matplotlib.pyplot as plt

def run_analytics_dashboard():
    db_path = "intellistock.db"
    
    if not os.path.exists(db_path):
        print(f" Error: Could not find database file at '{db_path}'")
        return

    print("=== Connecting to IntelliStock DB via Python ===")
    conn = sqlite3.connect(db_path)
    
    try:
        # 1. Load the sales data sorted chronologically by date
        query = "SELECT sku, quantity_sold, sale_price, sale_date FROM sales ORDER BY sale_date ASC;"
        df = pd.read_sql_query(query, conn)
        
        
        
        
        if df.empty:
            print(" No sales records found yet. Run your Java simulation longer!")
            return
            
        print(f"Extracted {len(df)} sales transactions.")
        
        df['total_revenue'] = df['quantity_sold'] * df['sale_price']
        
        # 2. Predictive moving average calculation per SKU
        print("\n---  Predictive Moving Average Analytics ---")
        
        # We will keep track of forecasts for our visualization chart
        forecast_data = []
        
        for sku in df['sku'].unique():
            sku_df = df[df['sku'] == sku].copy()
            
            # Compute a Rolling/Moving Average of the last 3 orders to smooth out demand spikes
            sku_df['moving_avg_demand'] = sku_df['quantity_sold'].rolling(window=3, min_periods=1).mean()
            
            # The last value in our moving average represents our current forecasted run-rate
            latest_forecast = sku_df['moving_avg_demand'].iloc[-1]
            total_sold = sku_df['quantity_sold'].sum()
            
            # Calculate a dynamic safety stock requirement based on current volatility
            recommended_safety_stock = int(round(latest_forecast * 2.5))
            
            forecast_data.append({
                'sku': sku,
                'total_sold': total_sold,
                'forecast': latest_forecast,
                'safety_stock': recommended_safety_stock
            })
            
            print(f"SKU [{sku}]:")
            print(f"  -> Cumulative Units Drained: {total_sold}")
            print(f"  -> Moving Average Run-Rate:  {latest_forecast:.2f} units/order")
            print(f"  -> Suggested Safety Stock:   {recommended_safety_stock} units")
        
        # 3. Data Visualization: Dual Bar Chart Comparing Demand vs Forecast Target
        forecast_df = pd.DataFrame(forecast_data)
        
        fig, ax = plt.subplots(figsize=(10, 6))
        x_indices = range(len(forecast_df))
        bar_width = 0.35
        
        # Plot Historical Totals vs Forecast Safety targets side by side
        ax.bar([x - bar_width/2 for x in x_indices], forecast_df['total_sold'], bar_width, label='Total Units Sold', color='#4CAF50')
        ax.bar([x + bar_width/2 for x in x_indices], forecast_df['safety_stock'], bar_width, label='Recommended Safety Stock', color='#FF9800')
        
        ax.set_title('IntelliStock Predictive Inventory Matrix')
        ax.set_xlabel('Product SKU')
        ax.set_ylabel('Units')
        ax.set_xticks(x_indices)
        ax.set_xticklabels(forecast_df['sku'])
        ax.legend()
        ax.grid(axis='y', linestyle='--', alpha=0.5)
        
        print("\n Rendering dual-axis performance dashboard...")
        plt.tight_layout()
        plt.show()

    except Exception as e:
        print(f" Analytics pipeline failed: {e}")
    finally:
        conn.close()

if __name__ == "__main__":
    run_analytics_dashboard()