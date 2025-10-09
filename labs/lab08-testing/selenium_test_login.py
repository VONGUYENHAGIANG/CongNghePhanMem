import pytest, time
from selenium import webdriver
from selenium.webdriver.common.by import By

URL = "http://127.0.0.1:5500/Lab08-Testing/login_form/Login_Form.html"   # chạy bằng: python -m http.server 5500

@pytest.fixture
def driver():
    driver = webdriver.Chrome()      # ChromeDriver
    driver.get(URL)
    driver.maximize_window()
    yield driver
    driver.quit()

def test_login_success(driver):
    driver.find_element(By.ID, "username").send_keys("student")
    driver.find_element(By.ID, "password").send_keys("pass123")
    driver.find_element(By.ID, "btnLogin").click()
    time.sleep(1)
    message = driver.find_element(By.ID, "formMessage").text
    assert "Đăng nhập thành công" in message

def test_login_wrong_password(driver):
    driver.find_element(By.ID, "username").send_keys("student")
    driver.find_element(By.ID, "password").send_keys("wrong123")
    driver.find_element(By.ID, "btnLogin").click()
    time.sleep(1)
    message = driver.find_element(By.ID, "formMessage").text
    assert "không đúng" in message.lower()

def test_login_empty_input(driver):
    driver.find_element(By.ID, "btnLogin").click()
    time.sleep(1)
    err_user = driver.find_element(By.ID, "err-username").text
    err_pass = driver.find_element(By.ID, "err-password").text
    assert "Vui lòng nhập username" in err_user
    assert "Vui lòng nhập password" in err_pass
