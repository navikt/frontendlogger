import { serializeError } from 'serialize-error';

type Object = { [k: string]: any };
type LogData = string | { message: string, [key: string]: any };

interface FrontendLogger {
	info: (data: LogData) => void;
	warn: (data: LogData) => void;
	error: (data: LogData) => void;
	event: (name: string, fields?: Object, tags?: Object) => void;
}

enum LogLevel {
	INFO = 'info',
	WARN = 'warn',
	ERROR = 'error'
}

export const DEFAULT_FRONTENDLOGGER_API_URL = '/frontendlogger/api'

export function createFrontendLogger(appName: string, apiUrl: string): FrontendLogger {
	return {
		info: createLogger(apiUrl, appName, LogLevel.INFO),
		warn: createLogger(apiUrl, appName, LogLevel.WARN),
		error: createLogger(apiUrl, appName, LogLevel.ERROR),
		event: createEventLogger(apiUrl, appName)
	};
}

export function createMockFrontendLogger(appName: string): FrontendLogger {
	return {
		info: createMockLogger(appName, LogLevel.INFO),
		warn: createMockLogger(appName, LogLevel.WARN),
		error: createMockLogger(appName, LogLevel.ERROR),
		event: createMockEventLogger()
	};
}

export function setUpErrorReporting(logger: { error: (data: LogData) => void }) {
	const oldOnError = window.onerror;

	window.onerror = function (message, url, line, column, error) {
		const json: LogData = {
			message: message.toString(),
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
				error: serializeError(error)
			};
		}

		logger.error(json);

		if (oldOnError) {
			oldOnError(message, url, line, column, error);
		}
	};
}

function objectifyData(data: LogData): Object {
	if (typeof data === 'object') {
		return data;
	}

	return { message: data };
}

function enrichData(data: Object, appName: string): Object {
	const additionalData = {
		url: window.location.href,
		userAgent: window.navigator.userAgent,
		appname: appName
	};

	return Object.assign({}, data, additionalData);
}

function createLogger(apiUrl: string, appName: string, level: LogLevel): (data: LogData) => void {
	return (data: LogData) => {
		const url = joinPaths(apiUrl, level);
		const enrichedData = enrichData(objectifyData(data), appName);

		sendData(url, appName, enrichedData)
	};
}

function createMockLogger(appName: string, level: LogLevel): (data: LogData) => void {
	return (data: LogData) => {
		const enrichedData = enrichData(objectifyData(data), appName);
		console.log('Log: ' + level, enrichedData);
	};
}

function createEventLogger(apiUrl: string, appName: string): (name: string, fields?: Object, tags?: Object) => void {
	return (name: string, fields?: object, tags?: object) => {
		const url = joinPaths(apiUrl, 'event');
		const data = { name, fields: fields || {}, tags: tags || {} };

		sendData(url, appName, data);
	};
}

function createMockEventLogger(): (name: string, fields?: Object, tags?: Object) => void {
	return (name: string, fields?: Object, tags?: Object) => {
		console.log('Event ' + name, 'Fields:', fields, 'Tags:', tags);
	};
}

function sendData(url: string, appName: string, data: object) {
	fetch(url, {
		method: 'POST',
		credentials: 'same-origin',
		headers: {
			'Content-Type': 'application/json',
			'Nav-Consumer-Id': appName
		},
		body: JSON.stringify(data)
	}).catch(); // Squelch errors
}

function joinPaths(...paths: string[]): string {
	return paths.map((path, idx) => {
		if (path.endsWith('/') && idx !== paths.length - 1) {
			return path.substr(0, path.length - 1);
		}

		return path;
	}).join('/');
}

