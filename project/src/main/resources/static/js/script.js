document.addEventListener('DOMContentLoaded', function () {

  // ===== 1. Delete confirmation =====
  document.querySelectorAll('[data-confirm-delete]').forEach(function (form) {
    form.addEventListener('submit', function (event) {
      var msg = form.getAttribute('data-confirm-message') || 'Are you sure you want to delete this internship?';
      if (!window.confirm(msg)) {
        event.preventDefault();
      }
    });
  });

  // ===== 2. Auto-hide alerts =====
  document.querySelectorAll('.alert-dismissible[data-auto-hide]').forEach(function (alert) {
    var delay = parseInt(alert.getAttribute('data-auto-hide'), 10) || 5000;
    setTimeout(function () {
      if (window.bootstrap && bootstrap.Alert) {
        var inst = bootstrap.Alert.getOrCreateInstance(alert);
        inst.close();
      } else {
        alert.style.display = 'none';
      }
    }, delay);
  });

  // ===== 3. Password visibility toggle =====
  document.querySelectorAll('[data-password-toggle]').forEach(function (btn) {
    btn.addEventListener('click', function () {
      var targetId = btn.getAttribute('data-password-toggle');
      var input = document.getElementById(targetId);
      if (!input) return;
      var icon = btn.querySelector('i');
      if (input.type === 'password') {
        input.type = 'text';
        if (icon) { icon.classList.remove('bi-eye'); icon.classList.add('bi-eye-slash'); }
      } else {
        input.type = 'password';
        if (icon) { icon.classList.remove('bi-eye-slash'); icon.classList.add('bi-eye'); }
      }
    });
  });

  // ===== 4. CGPA validation =====
  document.querySelectorAll('input[data-cgpa]').forEach(function (input) {
    input.addEventListener('input', function () {
      var val = parseFloat(input.value);
      if (input.value !== '' && !isNaN(val)) {
        if (val < 0) input.value = 0;
        if (val > 10) input.value = 10;
      }
    });
  });

  // ===== 5. Client-side search filtering for internship cards =====
  var cardSearch = document.getElementById('cardSearch');
  if (cardSearch) {
    cardSearch.addEventListener('input', function () {
      var query = cardSearch.value.toLowerCase().trim();
      document.querySelectorAll('[data-internship-card]').forEach(function (card) {
        var text = (card.getAttribute('data-search-text') || '').toLowerCase();
        card.style.display = text.indexOf(query) !== -1 ? '' : 'none';
      });
    });
  }

  // ===== 6. Profile photo preview =====
  var photoInput = document.getElementById('photoInput');
  var photoPreview = document.getElementById('photoPreview');
  if (photoInput && photoPreview) {
    photoInput.addEventListener('change', function () {
      var file = photoInput.files[0];
      if (file) {
        var reader = new FileReader();
        reader.onload = function (e) {
          photoPreview.src = e.target.result;
          photoPreview.style.display = 'block';
        };
        reader.readAsDataURL(file);
      }
    });
  }

  // ===== 7. Last date validation (no past dates) =====
  document.querySelectorAll('input[data-future-date]').forEach(function (input) {
    var today = new Date().toISOString().split('T')[0];
    input.setAttribute('min', today);
    input.addEventListener('change', function () {
      var selected = input.value;
      if (selected && selected < today) {
        input.setCustomValidity('Please select a date in the future.');
      } else {
        input.setCustomValidity('');
      }
    });
  });

  // ===== 8. Simple form validation =====
  document.querySelectorAll('form[data-validate]').forEach(function (form) {
    form.addEventListener('submit', function (event) {
      var valid = true;
      form.querySelectorAll('[required]').forEach(function (field) {
        if (!field.value.trim()) {
          field.classList.add('is-invalid');
          valid = false;
        } else {
          field.classList.remove('is-invalid');
        }
      });
      if (!valid) {
        event.preventDefault();
      }
    });
  });

});
