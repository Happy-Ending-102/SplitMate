# SplitMate

**SplitMate** is a desktop application to help friends and groups share expenses, track debts, and settle up seamlessly. It combines a JavaFX-based user interface with a Spring Boot back-end and MongoDB for persistence.

---

## 🚀 Core Features

- **User Profiles**

  - Each user has a name, avatar (Base64-encoded or default placeholder), and a list of balances in multiple currencies.
  - Bi-directional friendships: add friends, view mutual groups, and track shared expenses.
- **Group Management**

  - Create groups with a name, avatar, default currency and conversion policy.
  - Browse group transaction history and member debts.
- **Expense & Payment Tracking**

  - Log individual expenses or payments between group members.
  - Notifications for incoming payments or friend requests.
  - View per-user balances and transaction history sorted by date.
- **Automatic Debt Settlement**

  - Our application uses linear programming to compute minimal transfers that settle all balances.
  - Converts multi-currency balances into Turkish Lira (TL) for calculation, then persists the optimized “who owes whom” debts back into MongoDB.

---

## 🏗 Architecture

- **Frontend**:
  - JavaFX for UI, FXML for layouts (or code-based scenes), controllers wired via Spring.
- **Backend**:
  - Spring Boot with Spring Data MongoDB, services for Users, Groups, Friendships, Payments, Notifications, and Debt calculations.
- **Database**:
  - MongoDB (local or Atlas), storing documents for Users, Groups, Friendships, Transactions, Debts, etc.
- **Solver**:
  - Python 3 + PuLP for optimal debt settlement, communicated over stdin/stdout JSON.

---

## 📦 Prerequisites

- **Java 17** (or newer)
- **Maven 3.6+**
- **MongoDB**
  - Local instance (`mongod`) or MongoDB Atlas
- *(Optional)* MongoDB Compass for visual DB browsing

---

## 💿 Installation & Setup

1. ### Clone the repo


   ```bash
   git clone https://github.com/your-org/SplitMate.git
   cd SplitMate
   ```
2. ### Configure MongoDB


   - **Local**:

     ```properties
     # src/main/resources/application.properties
     spring.data.mongodb.uri=mongodb://localhost:27017/splitmate
     spring.data.mongodb.database=splitmate
     ```

     Start MongoDB:

     ```bash
     sudo systemctl start mongod
     ```
   - **Atlas**: update `spring.data.mongodb.uri` to your connection string.
3. ### Build the app


   ```bash
   mvn clean package
   ```
4. ### Run the application


   ```bash
   mvn javafx:run
   ```

   or

   ```bash
   java -jar target/SplitMate-0.1.0.jar
   ```
5. 

---

## ▶️ Usage

1. **Login / Register**: launch the app and create or select your user account.
2. **Friends**: add existing users as friends to share expenses.
3. **Groups**: create a new group, invite friends, set default currency.
4. **Expenses**: add expenses or payments under a group context.
5. **History**: view sorted transaction history per friend or group.

## 🤝 Contributing

Feel free to open issues or submit pull requests. Please follow the existing code style and add tests for new features.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
