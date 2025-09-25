# Lab03 — ATM: Withdraw Use Case & Sequence

## 🎯 Use Case Diagram
Mô tả các chức năng chính của hệ thống ATM.

![Use Case Diagram](./UC%20lab3.png)

---

## 🎯 Sequence Diagram — Quy trình Rút tiền
Mô tả luồng tương tác giữa Customer, ATM, BankServer và Account khi rút tiền.

![Sequence Diagram](./SQ%20lab03.jpg)

---

## 📝 Giải thích
- **Customer:** Actor chính, thực hiện giao dịch.  
- **ATM:** Thiết bị trung gian, giao tiếp với khách và BankServer.  
- **BankServer:** Xác thực PIN, kiểm tra số dư, xử lý rút tiền.  
- **Account:** Thực thể số dư, chịu trách nhiệm cập nhật sau giao dịch.  

**Điều kiện trước:**  
- ATM hoạt động, thẻ hợp lệ, kết nối BankServer.  

**Điều kiện sau:**  
- Nếu thành công: ATM nhả tiền, in hóa đơn, số dư trừ đi.  
- Nếu thất bại: Hệ thống báo lỗi, không trừ tiền.
