# IntelliStock — AI-Powered Inventory Management

**Smarter inventory. Better decisions. Less guesswork.**

IntelliStock is a full-stack inventory management platform designed to help retail businesses manage products, track sales, monitor performance, and make data-driven inventory decisions. It combines an interactive dashboard with analytics, demand forecasting, and role-based access control.

## Key Features

- **Inventory Management** — Manage products and maintain inventory records.
- **Sales & Billing** — Track sales, transactions, invoices, and customer workflows.
- **Analytics Dashboard** — Monitor business KPIs, trends, and performance.
- **AI-Powered Forecasting** — Analyze historical data to forecast demand and support inventory planning.
- **Smart Reorder Insights** — Use forecasting insights to make more informed replenishment decisions.
- **Role-Based Access Control** — Support separate admin, staff, and customer roles.
- **Reports & Audit Logs** — Track business activity and access reporting features.
- **REST API Documentation** — Explore API endpoints through Swagger UI.

## Tech Stack

| Layer | Technologies |
|---|---|
| Frontend | React 18, Vite, Tailwind CSS |
| UI & Charts | Framer Motion, Recharts |
| Backend | Node.js, Express 5 |
| Database | PostgreSQL |
| Authentication | JWT |
| Machine Learning | Python, FastAPI, Pandas, NumPy, scikit-learn |
| Database Integration | SQLAlchemy, psycopg2 |
| API Documentation | Swagger UI |

## Architecture

```text
IntelliStock/
├── client/          # React frontend
├── server/          # Express REST API
├── ml-service/     # Python forecasting service
├── package.json
├── SETUP.sh
└── README.md
```

**How it works**

1. The React frontend communicates with the backend through REST APIs.
2. The Express backend handles authentication, business logic, and database operations.
3. PostgreSQL stores application data.
4. The FastAPI machine-learning service provides forecasting and analytical results.
5. The backend integrates with the ML service using API-key authentication.

## Getting Started

### Prerequisites

Install the following before starting:

- Node.js 18 or later
- npm 9 or later
- Python 3.10 or later
- PostgreSQL 13 or later

### 1. Clone the Repository

```bash
git clone https://github.com/Divya02-coder/IntelliStock.git
cd IntelliStock
```

### 2. Install Dependencies

```bash
npm install
npm install --prefix client
npm install --prefix server
```

Install the Python dependencies:

```bash
cd ml-service
python -m venv venv
```

Activate the virtual environment.

**Windows PowerShell:**

```powershell
.\venv\Scripts\Activate.ps1
```

**Linux/macOS:**

```bash
source venv/bin/activate
```

Then install the dependencies:

```bash
pip install -r requirements.txt
cd ..
```

### 3. Configure Environment Variables

Create the following environment files.

**`server/.env`**

```env
PORT=5000
NODE_ENV=development

DB_HOST=localhost
DB_PORT=5432
DB_NAME=intellistock
DB_USER=postgres
DB_PASSWORD=your_db_password

CORS_ORIGIN=http://localhost:5173,http://localhost:3000,http://localhost:3001

ML_API_KEY=replace_with_secure_key
```

**`ml-service/.env`**

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=intellistock
DB_USER=postgres
DB_PASSWORD=your_db_password

ML_API_KEY=replace_with_secure_key
```

Use the same `ML_API_KEY` in both services.

**`client/.env.local`**

```env
VITE_API_URL=http://localhost:5000/api
```

Replace the example database credentials with your local PostgreSQL configuration. Never commit real secrets to GitHub.

### 4. Set Up the Database

Create a PostgreSQL database named `intellistock`, then execute the schema files in order:

1. `server/db/schema.sql`
2. `server/db/add-shop-and-customers.sql`

Run any additional database scripts required by your checkout.

### 5. Run the Application

Start each service in a separate terminal from the project root.

**Terminal 1 — Backend**

```bash
npm run dev:server
```

**Terminal 2 — Frontend**

```bash
npm run dev:client
```

**Terminal 3 — Machine Learning Service**

```bash
cd ml-service
```

Activate the Python virtual environment if necessary, then run:

```bash
uvicorn main:app --host 127.0.0.1 --port 8000 --reload
```

### 6. Access the Application

| Service | URL |
|---|---|
| Frontend | http://localhost:5173 |
| Backend | http://localhost:5000 |
| Database health | http://localhost:5000/health/db |
| Swagger API docs | http://localhost:5000/api-docs |
| ML service health | http://127.0.0.1:8000/ |

These addresses assume the default local development configuration.

## API Modules

The backend organizes its REST API into modules including:

- `/api/auth` — Authentication
- `/api/products` — Product management
- `/api/sales` — Sales and transactions
- `/api/dashboard` — Dashboard data
- `/api/analytics` — Business analytics
- `/api/reports` — Reports
- `/api/ml` — Machine-learning integration
- `/api/notifications` — Notifications
- `/api/audit-logs` — Audit records
- `/api/shop` — Shop management
- `/api/invoices` — Invoice workflows

Explore the available endpoints at [Swagger UI](http://localhost:5000/api-docs) after starting the backend.

## Machine Learning Integration

The ML service supports forecasting and analytical operations, including:

- Demand forecasting
- Product-level forecasting
- Trend analysis
- Moving-average analysis
- Seasonality analysis
- Anomaly detection
- Forecast-versus-actual comparisons
- Growth-rate analysis

The backend communicates with the ML service using the configured API key. Both services must be running and able to access the required database for the integrated workflow to function.

## Useful Commands

```bash
# Start frontend
npm run dev:client

# Start backend
npm run dev:server

# Build frontend
npm run build --prefix client

# Start backend without development watch mode
npm run start
```

## Security Notes

- Use strong database credentials.
- Generate a secure, private ML API key.
- Change or disable demo credentials outside local development.
- Restrict CORS to trusted origins in production.
- Use HTTPS in deployed environments.
- Keep `.env` files and other secrets out of version control.

## Future Improvements

Potential areas for further development include:

- Barcode scanning and inventory automation
- Low-stock alerts and configurable reorder thresholds
- Enhanced forecasting evaluation and model monitoring
- Automated testing and CI/CD
- Cloud deployment and production monitoring

## Contributing

Contributions and suggestions are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Make your changes and test the affected services.
4. Submit a pull request with a clear description.

## License

No top-level license is currently documented. Add a `LICENSE` file before distributing the project under an open-source license.

---

**Built with React, Node.js, PostgreSQL, and Python.**
