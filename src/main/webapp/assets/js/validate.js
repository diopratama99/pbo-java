// Validasi form dengan pesan error yang ramah, menggantikan bubble bawaan browser.
// Aktif untuk <form data-validate>. Aturannya tetap memakai atribut HTML biasa
// (required, type="email", pattern, min, max), jadi tinggal menulis atribut di JSP.
//
// Pesan bisa diganti per field:
//   data-msg-required="..."  saat field kosong
//   data-msg-pattern="..."   saat format tidak cocok (pattern / url / tel)
(function () {
  var source = document.getElementById('validation-messages');
  var messages = source ? JSON.parse(source.textContent) : {};

  function fill(template, values) {
    return (template || '').replace(/\{(\w+)\}/g, function (match, key) {
      return values[key] !== undefined ? values[key] : match;
    });
  }

  function isChoice(field) {
    return field.type === 'radio' || field.type === 'checkbox';
  }

  // Nama field diambil dari teks label-nya, tanpa keterangan dalam kurung seperti "(opsional)"
  function labelOf(field) {
    if (field.dataset.label) return field.dataset.label;
    var label = field.closest('label');
    var text = label && label.querySelector('.field-label, .sr-only');
    return text ? text.textContent.replace(/\(.*?\)/g, '').trim() : field.name;
  }

  function messageFor(field) {
    var validity = field.validity;
    var values = { label: labelOf(field), min: field.min, max: field.max };

    if (validity.valueMissing) {
      return field.dataset.msgRequired || (isChoice(field) ? messages.choose : fill(messages.required, values));
    }
    if (validity.typeMismatch && field.type === 'email') return messages.email;
    if (validity.typeMismatch || validity.patternMismatch) return field.dataset.msgPattern || messages.pattern;
    if (validity.rangeUnderflow) return fill(messages.min, values);
    if (validity.rangeOverflow) return fill(messages.max, values);
    if (validity.stepMismatch) return messages.step;
    if (validity.badInput) return messages.number;
    return messages.invalid;
  }

  // Tempat pesan error: di bawah grup pilihan (fieldset) atau di bawah label input
  function containerOf(field) {
    if (isChoice(field)) return field.closest('fieldset') || field.closest('label').parentNode;
    return field.closest('label') || field.closest('div') || field.parentNode;
  }

  function groupOf(field) {
    if (field.type !== 'radio' || !field.form) return [field];
    return Array.prototype.filter.call(field.form.elements, function (el) { return el.name === field.name; });
  }

  function errorOf(field) {
    return containerOf(field).querySelector(':scope > [data-field-error]');
  }

  function showError(field, message) {
    var error = errorOf(field);
    if (!error) {
      error = document.createElement('p');
      error.id = 'error-' + (field.name || field.id) + '-' + Math.random().toString(36).slice(2, 7);
      error.className = 'field-error';
      error.setAttribute('data-field-error', '');
      error.setAttribute('aria-live', 'polite');
      containerOf(field).appendChild(error);
    }
    error.textContent = message;
    groupOf(field).forEach(function (el) {
      el.setAttribute('aria-invalid', 'true');
      el.setAttribute('aria-describedby', error.id);
    });
  }

  function clearError(field) {
    var error = errorOf(field);
    if (error) error.remove();
    groupOf(field).forEach(function (el) {
      el.removeAttribute('aria-invalid');
      el.removeAttribute('aria-describedby');
    });
  }

  function check(field) {
    if (!field.willValidate) {
      clearError(field);
      return true;
    }
    if (field.checkValidity()) {
      clearError(field);
      return true;
    }
    showError(field, messageFor(field));
    return false;
  }

  document.querySelectorAll('form[data-validate]').forEach(function (form) {
    form.setAttribute('novalidate', '');

    form.addEventListener('submit', function (event) {
      var firstInvalid = null;
      var checkedGroups = {};
      Array.prototype.forEach.call(form.elements, function (field) {
        if (!field.name || field.type === 'hidden') return;
        if (field.type === 'radio') {
          if (checkedGroups[field.name]) return;
          checkedGroups[field.name] = true;
        }
        if (!check(field) && !firstInvalid) firstInvalid = field;
      });

      if (firstInvalid) {
        event.preventDefault();
        event.stopImmediatePropagation();
        firstInvalid.focus({ preventScroll: true });
        containerOf(firstInvalid).scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
    });

    // Pesan error hilang begitu field diperbaiki, tanpa menunggu submit ulang
    function recheck(event) {
      var field = event.target;
      if (field.getAttribute && field.getAttribute('aria-invalid') === 'true') check(field);
    }
    form.addEventListener('input', recheck);
    form.addEventListener('change', recheck);

    // Setelah user selesai mengisi sebuah field, cek formatnya (bukan kosong atau tidak)
    form.addEventListener('focusout', function (event) {
      var field = event.target;
      if (field.name && !isChoice(field) && field.value !== '') check(field);
    });
  });
})();
