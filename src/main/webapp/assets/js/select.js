// Dropdown custom untuk <select data-select>.
// <select> asli tetap ada (disembunyikan) supaya nilainya ikut terkirim saat form disubmit.
// Bisa dipakai dengan mouse maupun keyboard: panah atas/bawah, Enter, Esc, Home, End.
(function () {
  var uid = 0;

  function enhance(select) {
    uid++;
    var listId = 'select-list-' + uid;

    select.classList.add('sr-only');
    select.tabIndex = -1;
    select.setAttribute('aria-hidden', 'true');

    var wrapper = document.createElement('div');
    wrapper.className = 'relative';

    var button = document.createElement('button');
    button.type = 'button';
    button.className = 'input flex items-center justify-between gap-3 text-left disabled:cursor-not-allowed disabled:bg-cream disabled:text-muted';
    button.setAttribute('aria-haspopup', 'listbox');
    button.setAttribute('aria-expanded', 'false');
    button.setAttribute('aria-controls', listId);
    if (select.getAttribute('aria-labelledby')) {
      button.setAttribute('aria-labelledby', select.getAttribute('aria-labelledby') + ' ' + listId + '-value');
    }
    button.disabled = select.disabled;
    button.innerHTML =
      '<span id="' + listId + '-value" class="truncate"></span>' +
      '<svg class="size-4 shrink-0 text-muted transition duration-300" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="m4 6 4 4 4-4"/></svg>';
    var valueLabel = button.querySelector('span');
    var chevron = button.querySelector('svg');

    var list = document.createElement('ul');
    list.id = listId;
    list.setAttribute('role', 'listbox');
    list.tabIndex = -1;
    list.className = 'card absolute inset-x-0 z-30 mt-2 hidden max-h-72 overflow-y-auto p-2 outline-none';

    var options = Array.prototype.map.call(select.options, function (option, index) {
      var item = document.createElement('li');
      item.id = listId + '-' + index;
      item.setAttribute('role', 'option');
      item.dataset.value = option.value;
      item.className = 'flex cursor-pointer items-center justify-between gap-3 rounded-full px-4 py-2.5 text-sm transition data-[active=true]:bg-sage aria-selected:font-medium';
      item.innerHTML = '<span></span><svg class="size-3.5 shrink-0 text-glacier-deep opacity-0" viewBox="0 0 12 12" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M2.5 6.2 5 8.5l4.5-5"/></svg>';
      item.querySelector('span').textContent = option.textContent.trim();
      list.appendChild(item);
      return item;
    });

    select.parentNode.insertBefore(wrapper, select.nextSibling);
    wrapper.appendChild(button);
    wrapper.appendChild(list);

    var activeIndex = select.selectedIndex;

    function render() {
      valueLabel.textContent = select.options[select.selectedIndex].textContent.trim();
      options.forEach(function (item, index) {
        var selected = index === select.selectedIndex;
        item.setAttribute('aria-selected', selected ? 'true' : 'false');
        item.querySelector('svg').classList.toggle('opacity-0', !selected);
      });
    }

    function setActive(index) {
      activeIndex = Math.max(0, Math.min(options.length - 1, index));
      options.forEach(function (item, i) { item.dataset.active = i === activeIndex ? 'true' : 'false'; });
      list.setAttribute('aria-activedescendant', options[activeIndex].id);
      options[activeIndex].scrollIntoView({ block: 'nearest' });
    }

    function open() {
      list.classList.remove('hidden');
      button.setAttribute('aria-expanded', 'true');
      chevron.classList.add('rotate-180');
      setActive(select.selectedIndex);
      list.focus();
    }

    function close(focusButton) {
      list.classList.add('hidden');
      button.setAttribute('aria-expanded', 'false');
      chevron.classList.remove('rotate-180');
      if (focusButton) button.focus();
    }

    function choose(index) {
      if (select.selectedIndex !== index) {
        select.selectedIndex = index;
        select.dispatchEvent(new Event('change', { bubbles: true }));
      }
      render();
      close(true);
    }

    button.addEventListener('click', function () {
      if (list.classList.contains('hidden')) open(); else close(true);
    });
    button.addEventListener('keydown', function (event) {
      if (['ArrowDown', 'ArrowUp', 'Enter', ' '].indexOf(event.key) !== -1) {
        event.preventDefault();
        open();
      }
    });

    list.addEventListener('keydown', function (event) {
      switch (event.key) {
        case 'ArrowDown': event.preventDefault(); setActive(activeIndex + 1); break;
        case 'ArrowUp': event.preventDefault(); setActive(activeIndex - 1); break;
        case 'Home': event.preventDefault(); setActive(0); break;
        case 'End': event.preventDefault(); setActive(options.length - 1); break;
        case 'Enter':
        case ' ': event.preventDefault(); choose(activeIndex); break;
        case 'Escape': event.preventDefault(); close(true); break;
        case 'Tab': close(false); break;
      }
    });

    options.forEach(function (item, index) {
      item.addEventListener('mousemove', function () { if (activeIndex !== index) setActive(index); });
      item.addEventListener('click', function () { choose(index); });
    });

    document.addEventListener('click', function (event) {
      if (!wrapper.contains(event.target)) close(false);
    });

    render();
  }

  document.querySelectorAll('select[data-select]').forEach(enhance);
})();
