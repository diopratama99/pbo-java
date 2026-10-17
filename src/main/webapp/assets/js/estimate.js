// Preview harga di kalkulator estimasi (plan.md bagian 6).
// Ini HANYA preview. Nanti server menghitung ulang harga di Java sebelum disimpan.
(function () {
  var form = document.getElementById('estimate-form');
  if (!form) return;

  var STORAGE_KEY = 'bisa.estimate';
  var BUNDLE_MIN = 5;      // minimal jumlah fitur utama + integrasi untuk diskon
  var BUNDLE_RATE = 0.10;  // diskon 10% dari subtotal

  var numberFormat = new Intl.NumberFormat(document.documentElement.lang === 'en' ? 'en-US' : 'id-ID');
  function rupiah(value) {
    return 'Rp ' + numberFormat.format(Math.round(value));
  }

  function priceOf(input, multiplier) {
    var base = parseFloat(input.dataset.harga);
    if (input.dataset.kategori === 'CORE') return base * multiplier;
    if (input.dataset.kategori === 'INTEGRATION') return base + parseFloat(input.dataset.setup || '0');
    return base; // PLATFORM dan DATABASE: harga tetap
  }

  function update() {
    var platform = form.querySelector('input[name="platform"]:checked');
    var multiplier = platform ? parseFloat(platform.dataset.pengali) : 1;

    var subtotal = 0;
    var featureCount = 0;
    var lines = [];

    form.querySelectorAll('input[data-harga]').forEach(function (input) {
      var price = priceOf(input, multiplier);
      var priceLabel = input.closest('label').querySelector('[data-price]');
      if (priceLabel) priceLabel.textContent = rupiah(price);

      if (input.checked) {
        subtotal += price;
        lines.push({ name: input.dataset.nama, price: price });
        if (input.dataset.kategori === 'CORE' || input.dataset.kategori === 'INTEGRATION') featureCount++;
      }
    });

    var discount = featureCount >= BUNDLE_MIN ? subtotal * BUNDLE_RATE : 0;
    var total = subtotal - discount;

    form.querySelectorAll('[data-total]').forEach(function (el) { el.textContent = rupiah(total); });
    form.querySelector('[data-count]').textContent = featureCount;
    form.querySelector('[data-subtotal]').textContent = rupiah(subtotal);
    form.querySelector('[data-discount]').textContent = '- ' + rupiah(discount);
    form.querySelector('[data-discount-rows]').classList.toggle('hidden', discount === 0);
    form.querySelector('[data-bundle-hint]').classList.toggle('hidden', discount > 0);
    form.querySelector('[data-bundle-on]').classList.toggle('hidden', discount === 0);
    form.querySelector('[data-empty]').classList.toggle('hidden', lines.length > 0);

    var list = form.querySelector('[data-lines]');
    list.replaceChildren();
    lines.forEach(function (line) {
      var item = document.createElement('li');
      item.className = 'flex justify-between gap-4';
      var name = document.createElement('span');
      name.className = 'text-muted';
      name.textContent = line.name;
      var price = document.createElement('span');
      price.className = 'shrink-0 font-medium';
      price.textContent = rupiah(line.price);
      item.append(name, price);
      list.append(item);
    });
  }

  // Simpan isi form di sessionStorage, supaya pilihan tidak hilang saat ganti bahasa (halaman di-reload)
  function save() {
    var state = { checked: [], values: {} };
    form.querySelectorAll('input[type="radio"]:checked, input[type="checkbox"]:checked').forEach(function (input) {
      state.checked.push(input.name + ':' + input.value);
    });
    form.querySelectorAll('input[type="text"], input[type="tel"], input[type="email"], textarea').forEach(function (input) {
      state.values[input.name] = input.value;
    });
    try { sessionStorage.setItem(STORAGE_KEY, JSON.stringify(state)); } catch (e) { /* storage tidak tersedia */ }
  }

  function restore() {
    var state;
    try { state = JSON.parse(sessionStorage.getItem(STORAGE_KEY)); } catch (e) { state = null; }
    if (!state) return;
    form.querySelectorAll('input[type="radio"], input[type="checkbox"]').forEach(function (input) {
      input.checked = state.checked.indexOf(input.name + ':' + input.value) !== -1;
    });
    Object.keys(state.values || {}).forEach(function (name) {
      var field = form.elements[name];
      if (field) field.value = state.values[name];
    });
  }

  restore();
  update();
  form.addEventListener('change', function () { update(); save(); });
  form.addEventListener('input', save);
  form.addEventListener('submit', function () {
    try { sessionStorage.removeItem(STORAGE_KEY); } catch (e) { /* abaikan */ }
  });
})();
