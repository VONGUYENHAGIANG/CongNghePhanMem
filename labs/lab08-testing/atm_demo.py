import mysql.connector
import hashlib

DB_CONFIG = {
    "user": "root",
    "password": "theat170805",
    "database": "atm_demo",
    "host": "127.0.0.1"
}

def verify_pin(card_no: str, pin: str) -> bool:
    """
    Xác thực mã PIN của thẻ.
    """
    conn = mysql.connector.connect(**DB_CONFIG)
    cur = conn.cursor()
    cur.execute("SELECT pin_hash FROM cards WHERE card_no=%s AND status='active'", (card_no,))
    row = cur.fetchone()
    conn.close()
    if not row:
        return False
    return row[0] == hashlib.sha256(pin.encode()).hexdigest()

def withdraw(card_no: str, amount: float):
    """
    Thực hiện giao dịch rút tiền.
    """
    conn = mysql.connector.connect(**DB_CONFIG)
    cur = conn.cursor()
    try:
        conn.start_transaction()
        # Khóa hàng để tránh race condition
        cur.execute("""
            SELECT a.account_id, a.balance
            FROM accounts a
            JOIN cards c ON a.account_id=c.account_id
            WHERE c.card_no=%s
            FOR UPDATE
        """, (card_no,))
        row = cur.fetchone()
        if not row:
            raise Exception("Card not found")
        account_id, balance = row

        if balance < amount:
            raise Exception("Insufficient funds")

        # Cập nhật số dư
        cur.execute("UPDATE accounts SET balance=balance-%s WHERE account_id=%s",
                    (amount, account_id))

        # Ghi nhận giao dịch
        cur.execute("""
            INSERT INTO transactions(account_id, card_no, atm_id, tx_type, amount, balance_after)
            VALUES (%s,%s,1,'WITHDRAW',%s,%s)
        """, (account_id, card_no, amount, balance - amount))

        conn.commit()
        print(f" Withdraw success: {amount} VND")
    except Exception as e:
        conn.rollback()
        print(" Error:", e)
    finally:
        conn.close()

if __name__ == "__main__":
    card = "1111-2222-3333"
    pin = input("Enter PIN: ")
    if verify_pin(card, pin):
        amt = float(input("Enter amount to withdraw: "))
        withdraw(card, amt)
    else:
        print(" Invalid PIN or card inactive")
