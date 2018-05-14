(function(){
    var url = '/frontendlogger/api/';
    var appname = window.frontendlogger.appname;

    function post(level, data) {
        if (typeof data === 'string') {
            data = { message: data };
        }

        data.url = window.location.href;
        data.userAgent = window.navigator.userAgent;
        data.appname = appname;

        var xhr = new XMLHttpRequest();
        xhr.open('POST', url + level, true);
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.send(JSON.stringify(data));
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
