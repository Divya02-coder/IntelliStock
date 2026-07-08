CREATE TABLE IF NOT EXISTS products (
    sku TEXT PRIMARY KEY,
    category TEXT NOT NULL,
    name TEXT NOT NULL,
    quantity INTEGER NOT NULL,
    cost_price REAL NOT NULL,
    base_price REAL NOT NULL,
    reorder_threshold INTEGER NOT NULL,
    
    -- Apparel specific fields
    size TEXT,
    season TEXT,
    
    -- Electronics specific fields
    warranty_months INTEGER,
    brand TEXT,
    
    -- Perishable specific fields
    expiry_date TEXT
);