import pytest
import mysql.connector
from atm_demo import verify_pin, withdraw, DB_CONFIG

CARD_NO = "1111-2222-3333"
CORRECT_PIN = "1234"
WRONG_PIN = "0000"

@pytest.fixture
def db_connection():
    conn = mysql.connector.connect(**DB_CONFIG)
    yield conn
    conn.close()

def get_balance(conn, card_no):
    cur = conn.cursor()
    cur.execute("""
        SELECT a.balance
        FROM accounts a
        JOIN cards c ON a.account_id=c.account_id
        WHERE c.card_no=%s
    """, (card_no,))
    return cur.fetchone()[0]

# ---- verify_pin ----
def test_verify_pin_correct(db_connection):
    assert verify_pin(CARD_NO, CORRECT_PIN) is True

def test_verify_pin_incorrect(db_connection):
    assert verify_pin(CARD_NO, WRONG_PIN) is False

# ---- withdraw ----
def test_withdraw_success(db_connection):
    before = get_balance(db_connection, CARD_NO)
    amount = 50.0
    withdraw(CARD_NO, amount)

    db_connection.close()
    new_conn = mysql.connector.connect(**DB_CONFIG)   # kết nối mới
    after = get_balance(new_conn, CARD_NO)
    new_conn.close()

    assert after == before - amount


def test_withdraw_insufficient(db_connection):
    before = get_balance(db_connection, CARD_NO)
    amount = before + 1000.0
    withdraw(CARD_NO, amount)
    after = get_balance(db_connection, CARD_NO)
    assert after == before
