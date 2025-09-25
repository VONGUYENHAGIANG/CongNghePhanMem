// script.js
const form = document.getElementById('loginForm');
const username = document.getElementById('username');
const password = document.getElementById('password');
const remember = document.getElementById('remember');
const errorBox = document.getElementById('error');
const successBox = document.getElementById('success');
const cancelBtn = document.getElementById('cancelBtn');

function showError(msg){
  successBox.style.display = "none";
  errorBox.textContent = msg;
  errorBox.style.display = "block";
}
function showSuccess(msg){
  errorBox.style.display = "none";
  successBox.textContent = msg;
  successBox.style.display = "block";
}

// Load saved username if "remember me"
window.addEventListener('DOMContentLoaded', () => {
  const saved = localStorage.getItem('lab04_username');
  if (saved) {
    username.value = saved;
    remember.checked = true;
  }
});

// Simple validation: not empty, password >= 6
form.addEventListener('submit', (e) => {
  e.preventDefault();
  errorBox.style.display = "none";
  successBox.style.display = "none";

  const u = username.value.trim();
  const p = password.value;

  if (!u) { showError('Vui lòng nhập username hoặc email.'); username.focus(); return; }
  if (!p) { showError('Vui lòng nhập mật khẩu.'); password.focus(); return; }
  if (p.length < 6) { showError('Mật khẩu phải có ít nhất 6 ký tự.'); password.focus(); return; }

  // Remember-me logic
  if (remember.checked) localStorage.setItem('lab04_username', u);
  else localStorage.removeItem('lab04_username');

  // Simulate successful login (no backend)
  showSuccess('Login thành công (mô phỏng). Bạn có thể chụp màn hình để nộp.');
  // Optionally clear password
  password.value = '';
});

// Cancel button clears inputs (and optionally localStorage)
cancelBtn.addEventListener('click', () => {
  username.value = '';
  password.value = '';
  remember.checked = false;
  localStorage.removeItem('lab04_username');
  errorBox.style.display = successBox.style.display = "none";
});
