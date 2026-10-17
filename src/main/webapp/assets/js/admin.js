// Script khusus halaman admin.
(function () {
  var lang = document.documentElement.lang === 'en' ? 'en-US' : 'id-ID';
  var numberFormat = new Intl.NumberFormat(lang);

  function digitsOf(text) {
    return (text || '').replace(/\D/g, '');
  }

  // ---------- Input uang: diketik sebagai teks, otomatis jadi 5.000.000 (id) atau 5,000,000 (en) ----------
  function formatMoneyInput(input) {
    var caret = input.selectionStart === null ? input.value.length : input.selectionStart;
    // Hitung berapa angka yang ada di kiri kursor, supaya posisi kursor tidak loncat setelah diformat
    var digitsBeforeCaret = digitsOf(input.value.slice(0, caret)).length;

    var digits = digitsOf(input.value).replace(/^0+(?=\d)/, '').slice(0, 13);
    input.value = digits ? numberFormat.format(Number(digits)) : '';

    var position = 0;
    var seen = 0;
    while (position < input.value.length && seen < digitsBeforeCaret) {
      if (/\d/.test(input.value[position])) seen++;
      position++;
    }
    if (document.activeElement === input) input.setSelectionRange(position, position);
  }

  document.querySelectorAll('input[data-money]').forEach(function (input) {
    input.addEventListener('input', function () { formatMoneyInput(input); });
  });

  // ---------- Form fitur ----------
  var form = document.querySelector('[data-feature-form]');
  if (!form) return;

  var select = form.querySelector('[data-kategori-select]');
  var preview = form.querySelector('[data-preview]');

  // Tampilkan input pengali hanya untuk PLATFORM dan biaya setup hanya untuk INTEGRATION
  function syncKategori() {
    form.querySelectorAll('[data-show-for]').forEach(function (field) {
      var visible = field.dataset.showFor === select.value;
      field.classList.toggle('hidden', !visible);
      field.querySelectorAll('input').forEach(function (input) {
        input.disabled = !visible;
        input.required = visible;
      });
    });
  }

  // Isi preview kartu fitur sesuai isi form
  function syncPreview() {
    if (!preview) return;
    var isEnglish = preview.dataset.lang === 'en';
    var name = (isEnglish ? form.elements.namaEn.value : form.elements.namaId.value).trim();
    var desc = (isEnglish ? form.elements.deskripsiEn.value : form.elements.deskripsiId.value).trim();

    var price = Number(digitsOf(form.elements.hargaDasar.value)) || 0;
    if (select.value === 'INTEGRATION') price += Number(digitsOf(form.elements.biayaSetup.value)) || 0;

    preview.querySelector('[data-preview-name]').textContent = name || preview.dataset.placeholderName;
    preview.querySelector('[data-preview-desc]').textContent = desc || preview.dataset.placeholderDesc;
    preview.querySelector('[data-preview-price]').textContent = 'Rp ' + numberFormat.format(price);
    preview.querySelector('[data-preview-rule]').textContent = preview.dataset['rule' + select.value.charAt(0) + select.value.slice(1).toLowerCase()];

    var active = form.elements.aktif.checked;
    preview.querySelector('[data-preview-card]').classList.toggle('opacity-40', !active);
    preview.querySelector('[data-preview-card]').classList.toggle('grayscale', !active);
    var hiddenNote = preview.querySelector('[data-preview-hidden]');
    hiddenNote.classList.toggle('hidden', active);
    hiddenNote.classList.toggle('flex', !active);
  }

  syncKategori();
  syncPreview();
  select.addEventListener('change', function () { syncKategori(); syncPreview(); });
  form.addEventListener('input', syncPreview);
  form.addEventListener('change', syncPreview);
})();
