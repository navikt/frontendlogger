const serializError = require('serialize-error');
const api = require('./api');

const apiUrl = '/frontendlogger/api/';
const appname = window.frontendlogger.appname;
const oldOnError = window.onerror;

function report(level, data) {
    data.url = window.location.href;
    data.userAgent = window.navigator.userAgent;
    data.appname = appname;

    return api.post(`${apiUrl}${level}`, data);
}

function log(level, data) {
    if (typeof data === 'string') {
        data = { message: data };
    }
    report(level, data);
}

function reportEvent(name, fields, tags) {
    report('event', { name, fields, tags });
}

function logPerformance(loadTime) {
    report('performance', { pageLoadTime: loadTime });
}

window.onerror = function (message, url, line, column, error) {
    const json = {
        message: message,
        jsFileUrl: url,
        lineNumber: line,
        column: column,
        messageIndexed: message
    };

    if (error) {
        json.stacktrace = error.stack ? error.stack : error;
        json.pinpoint = {
            message,
            url,
            line,
            column,
            error: serializError(error)
        };
    }

    report('error', json);

    if (oldOnError) {
        oldOnError.apply(this, arguments);
    }
};

window.addEventListener('load',function() {
    reportInitialLoad();
});

function reportInitialLoad() {
    const navEntry = window.performance && window.performance.getEntriesByType ? window.performance.getEntriesByType('navigation')[0] : undefined;
    if (navEntry && navEntry.loadEventEnd > 0) {
        // https://w3c.github.io/navigation-timing/#processing-model
        const loadTime = navEntry.loadEventEnd - navEntry.fetchStart;
        logPerformance(loadTime)
    } else {
        setTimeout(reportInitialLoad, 1000);
    }
}

window.frontendlogger.info = function(data) { log('info', data); };
window.frontendlogger.warn = function(data) { log('warn', data); };
window.frontendlogger.error = function(data) { log('error', data); };
window.frontendlogger.event = function(name, fields, tags) { reportEvent(name, fields, tags); };
