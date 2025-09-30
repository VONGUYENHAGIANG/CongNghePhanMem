# Lab06 — ATM Class & Package Diagram

## 1. Mục tiêu
Từ Use Case và Sequence (Lab02/Lab03) thiết kế Class Diagram và Package Diagram cho ATM.

## 2. File nộp
- class-atm.puml / class-atm.png
- package-diagram.puml / package-diagram.png
- notes.md

## 3. Mô tả các lớp chính
### ATM
- **Thuộc tính:** atmId, location, cashLevel
- **Phương thức:** authenticate(), withdraw(), deposit(), transfer()
- **Vai trò:** giao diện phần cứng & điều phối nghiệp vụ rút/nạp tiền.

### Card
- **Thuộc tính:** cardNo, pinHash, status
- **Vai trò:** lưu thông tin thẻ; liên kết tới Account.

### Account
- **Thuộc tính:** accountNo, balance
- **Phương thức:** debit(), credit()
- **Vai trò:** chứa số dư, chịu trách nhiệm cập nhật khi giao dịch xảy ra.

### Transaction
- **Thuộc tính:** txId, type, amount, time, status
- **Vai trò:** đại diện cho bản ghi nghiệp vụ (rút, nạp, chuyển).

## 4. Quan hệ (giải thích)
- `ATM --> Card`: ATM đọc thẻ khi khách thao tác.
- `Card --> Account`: thẻ ánh xạ đến tài khoản khách.
- `Account --> Transaction`: tài khoản sinh và lưu transactions.
- `ATM --> Transaction`: ATM tạo transaction khi thực hiện hành vi.

## 5. Mapping với Use Case / Sequence
Ví dụ **Withdraw**:
- Customer → ATM (InsertCard, enterPIN) → ATM gọi `authenticate()` → BankService kiểm tra Account → ATM gọi `withdraw()` → tạo Transaction.

## 6. Rubric (làm rõ để giảng viên chấm)
- **Đủ lớp & quan hệ (5đ):** đã gồm ATM, Card, Account, Transaction và quan hệ chính.  
- **Thuộc tính / phương thức đúng (3đ):** mỗi lớp có thuộc tính & phương thức phù hợp nghiệp vụ.  
- **Tài liệu & repo (2đ):** file .puml + PNG + notes.md có trong `labs/lab06-atm-class/`.

## 7. Ghi chú & giả định
- `pinHash` giữ hash của PIN; xác thực thực hiện bởi BankService (không lưu PIN thô).  
- `cashLevel` của ATM là biến mô tả tiền vật lý hiện có trong máy.  
- Cần transaction log để audit.

