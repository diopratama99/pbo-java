// Tombol salin: <button data-copy="#id-elemen" data-copied-text="Tersalin">
(function () {
  document.querySelectorAll('[data-copy]').forEach(function (button) {
    var originalText = button.textContent;
    button.addEventListener('click', function () {
      var target = document.querySelector(button.dataset.copy);
      if (!target || !navigator.clipboard) return;
      navigator.clipboard.writeText(target.textContent.trim()).then(function () {
        button.textContent = button.dataset.copiedText;
        setTimeout(function () { button.textContent = originalText; }, 2000);
      });
    });
  });
})();
