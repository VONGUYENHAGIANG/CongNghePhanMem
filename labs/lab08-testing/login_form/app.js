    const form = document.getElementById('loginForm');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const rememberInput = document.getElementById('remember');
    const errUser = document.getElementById('err-username');
    const errPass = document.getElementById('err-password');
    const formMessage = document.getElementById('formMessage');
    const togglePassBtn = document.getElementById('togglePass');
    const btnCancel = document.getElementById('btnCancel');

    // Khởi tạo: nếu user đã chọn remember trước đó, lấy từ localStorage
    window.addEventListener('DOMContentLoaded', () => {
      const savedUsername = localStorage.getItem('savedUsername');
      if (savedUsername) {
        usernameInput.value = savedUsername;
        rememberInput.checked = true;
      }
    });

    // Hiện / ẩn mật khẩu
    togglePassBtn.addEventListener('click', () => {
      const shown = passwordInput.type === 'text';
      passwordInput.type = shown ? 'password' : 'text';
      togglePassBtn.textContent = shown ? 'Hiện' : 'Ẩn';
      togglePassBtn.setAttribute('aria-pressed', (!shown).toString());
      togglePassBtn.setAttribute('aria-label', shown ? 'Hiện mật khẩu' : 'Ẩn mật khẩu');
      passwordInput.focus();
    });

    // Cancel: xóa form và xóa Remember nếu không muốn
    btnCancel.addEventListener('click', () => {
      usernameInput.value = '';
      passwordInput.value = '';
      rememberInput.checked = false;
      errUser.textContent = '';
      errPass.textContent = '';
      formMessage.textContent = '';
      localStorage.removeItem('savedUsername');
      usernameInput.focus();
    });

    // Hàm validate đơn giản
    function validate() {
      let ok = true;
      errUser.textContent = '';
      errPass.textContent = '';
      formMessage.textContent = '';

      const user = usernameInput.value.trim();
      const pass = passwordInput.value;

      // Username: không rỗng, tối thiểu 3 ký tự
      if (!user) {
        errUser.textContent = 'Vui lòng nhập username.';
        ok = false;
      } else if (user.length < 3) {
        errUser.textContent = 'Username phải có ít nhất 3 ký tự.';
        ok = false;
      }

      // Password: không rỗng, tối thiểu 6 ký tự, có ít nhất 1 chữ số
      if (!pass) {
        errPass.textContent = 'Vui lòng nhập password.';
        ok = false;
      } else if (pass.length < 6) {
        errPass.textContent = 'Password phải có ít nhất 6 ký tự.';
        ok = false;
      } else if (!/\d/.test(pass)) {
        errPass.textContent = 'Password nên chứa ít nhất 1 chữ số.';
        ok = false;
      }

      return ok;
    }

    // Xử lý submit (demo: giả lập login)
    form.addEventListener('submit', (e) => {
      e.preventDefault();
      if (!validate()) return;

      // Lưu username nếu chọn remember
      if (rememberInput.checked) {
        localStorage.setItem('savedUsername', usernameInput.value.trim());
      } else {
        localStorage.removeItem('savedUsername');
      }

      // Giả lập kiểm tra "đăng nhập" (demo)
      // Trong thực tế: gửi fetch() tới backend để xác thực
      const demoUsername = 'student';
      const demoPassword = 'pass123'; // demo: chứa số

      // Hiển thị trạng thái
      formMessage.innerHTML = '<div class="success">Đang xác thực...</div>';

      // Giả lập trễ (như gọi API)
      setTimeout(() => {
        if (usernameInput.value.trim() === demoUsername && passwordInput.value === demoPassword) {
          formMessage.innerHTML = '<div class="success">Đăng nhập thành công! Chuyển hướng...</div>';
          // ở đây bạn có thể window.location = 'dashboard.html';
        } else {
          formMessage.innerHTML = '<div class="error">Tên đăng nhập hoặc mật khẩu không đúng (demo).</div>';
        }
      }, 700);
    });
