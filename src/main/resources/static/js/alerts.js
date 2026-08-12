(function () {
    'use strict';

    function closeAlert(alertElement) {
        if (!alertElement) return;
        alertElement.style.transition = 'opacity 0.5s ease';
        alertElement.style.opacity = '0';
        setTimeout(function () {
            alertElement.remove();
        }, 500);
    }

    function initAlerts() {
        var alerts = document.querySelectorAll('.alert-flash');
        if (alerts.length === 0) return;

        alerts.forEach(function (alert) {
            setTimeout(function () {
                closeAlert(alert);
            }, 5000);

            var closeBtn = alert.querySelector('.btn-close');
            if (closeBtn) {
                closeBtn.addEventListener('click', function (e) {
                    e.preventDefault();
                    closeAlert(alert);
                });
            }
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initAlerts);
    } else {
        initAlerts();
    }

    document.addEventListener('htmx:afterSwap', initAlerts);
})();