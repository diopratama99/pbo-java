// Animasi slide-up untuk elemen dengan atribut data-rise.
// Class "motion" di <html> dipasang oleh script kecil di <head> (layout.tag),
// dan tidak dipasang kalau user mengaktifkan reduce motion.
(function () {
  var root = document.documentElement;
  if (!root.classList.contains('motion')) return;

  var items = document.querySelectorAll('[data-rise]');
  if (!('IntersectionObserver' in window)) {
    items.forEach(function (el) { el.classList.add('is-visible'); });
    return;
  }

  var observer = new IntersectionObserver(function (entries) {
    entries.forEach(function (entry) {
      if (entry.isIntersecting) {
        entry.target.classList.add('is-visible');
        observer.unobserve(entry.target);
      }
    });
  }, { threshold: 0.15 });

  items.forEach(function (el) { observer.observe(el); });
})();
