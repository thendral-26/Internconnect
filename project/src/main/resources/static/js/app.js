document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('[data-confirm-delete]').forEach((form) => {
    form.addEventListener('submit', (event) => {
      if (!window.confirm('Delete this internship? This action cannot be undone.')) event.preventDefault();
    });
  });
  document.querySelectorAll('.alert-dismissible').forEach((alert) => {
    window.setTimeout(() => bootstrap.Alert.getOrCreateInstance(alert).close(), 6000);
  });
});
