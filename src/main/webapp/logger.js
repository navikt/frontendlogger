(function(){
    var url = '/frontendlogger/api/';
    var harSjekketFetch = false;
    var fetchOk = false;
    var appname = window.frontendlogger.appname;

    var headers = new Headers();
    headers.append('Content-Type', 'application/json');

    function post(level, data) {
        sjekkFetchOppsett();
        if (!fetchOk) {
            return;
        }

        if (typeof data === 'string') {
            data = { message: data };
        }

        data.url = window.location.href;
        data.userAgent = window.navigator.userAgent;
        data.appname = appname;
        fetch(url + level, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(data)
        });
    }

    function sjekkFetchOppsett() {
        if (!harSjekketFetch && typeof fetch !== 'function') {
            console.error('fo-frontendlogger støtter bare fetch for ajax. Last inn polyfill om nødvendig');
            fetchOk = false;
        } else {
            fetchOk = true;
        }
    }

    window.onerror = function (message, url, line, column) {
        var json = {
            message: message,
            jsFileUrl: url,
            lineNumber: line,
            column: column
        };
        post('error', json);
    };

    window.frontendlogger.info = function(data) { post('info', data); };
    window.frontendlogger.warn = function(data) { post('warn', data); };
    window.frontendlogger.error = function(data) { post('error', data); };
})();
