require('jsdom-global')();
import { createFrontendLogger, setUpErrorReporting } from './index';

test('creates correct request for arbitrary data', () => {
	const logger = createFrontendLogger('/api/test', 'test');

	const mockFetch = jest.fn((input: RequestInfo, init?: RequestInit) => Promise.resolve());

	// @ts-ignore
	global.fetch = mockFetch;

	logger.info('hello');

	expect(mockFetch.mock.calls[0][0]).toBe('/api/test/info');

	expect(mockFetch.mock.calls[0][1]).toStrictEqual({
		"body": "{\"message\":\"hello\",\"url\":\"about:blank\",\"userAgent\":\"Mozilla/5.0 (darwin) AppleWebKit/537.36 (KHTML, like Gecko) jsdom/16.4.0\",\"appname\":\"test\"}",
		"credentials": "same-origin",
		"headers": {"Content-Type": "application/json", "Nav-Consumer-Id": "test"},
		"method": "POST"
	});

	logger.info(['test1', 'test2']);

	expect(mockFetch.mock.calls[1][0]).toBe('/api/test/info');

	expect(mockFetch.mock.calls[1][1]).toStrictEqual({
		"body": "{\"values\":[\"test1\",\"test2\"],\"url\":\"about:blank\",\"userAgent\":\"Mozilla/5.0 (darwin) AppleWebKit/537.36 (KHTML, like Gecko) jsdom/16.4.0\",\"appname\":\"test\"}",
		"credentials": "same-origin",
		"headers": {"Content-Type": "application/json", "Nav-Consumer-Id": "test"},
		"method": "POST"
	});

	logger.info({value1: 'test', value2: true});

	expect(mockFetch.mock.calls[2][0]).toBe('/api/test/info');

	expect(mockFetch.mock.calls[2][1]).toStrictEqual({"body": "{\"value1\":\"test\",\"value2\":true,\"url\":\"about:blank\",\"userAgent\":\"Mozilla/5.0 (darwin) AppleWebKit/537.36 (KHTML, like Gecko) jsdom/16.4.0\",\"appname\":\"test\"}", "credentials": "same-origin", "headers": {"Content-Type": "application/json", "Nav-Consumer-Id": "test"}, "method": "POST"});

});

test('creates correct request for error/warn', () => {
	const logger = createFrontendLogger('/api/test', 'test');

	const mockFetch = jest.fn((input: RequestInfo, init?: RequestInit) => Promise.resolve());

	// @ts-ignore
	global.fetch = mockFetch;


	logger.error({value1: 'test', value2: true});

	expect(mockFetch.mock.calls[0][0]).toBe('/api/test/error');


	logger.warn({value1: 'test', value2: true});

	expect(mockFetch.mock.calls[1][0]).toBe('/api/test/warn');
});

test('creates correct request for event', () => {
	const logger = createFrontendLogger('/api/test/', 'test-app');

	const mockFetch = jest.fn((input: RequestInfo, init?: RequestInit) => Promise.resolve());

	// @ts-ignore
	global.fetch = mockFetch;


	logger.event('metrikk1', {field1: 'test', field2: true}, {tag1: 54, tag2: null});

	expect(mockFetch.mock.calls[0][0]).toBe('/api/test/event');

	expect(mockFetch.mock.calls[0][1]).toStrictEqual({"body": "{\"name\":\"metrikk1\",\"fields\":{\"field1\":\"test\",\"field2\":true},\"tags\":{\"tag1\":54,\"tag2\":null}}", "credentials": "same-origin", "headers": {"Content-Type": "application/json", "Nav-Consumer-Id": "test-app"}, "method": "POST"});


	logger.event('metrikk2');
	expect(mockFetch.mock.calls[1][1]).toStrictEqual({"body": "{\"name\":\"metrikk2\",\"fields\":{},\"tags\":{}}", "credentials": "same-origin", "headers": {"Content-Type": "application/json", "Nav-Consumer-Id": "test-app"}, "method": "POST"});

});

test('setup error reporting', () => {
	const mockLogError = jest.fn((data: any) => {});

	const mockOldOnError = jest.fn((message, url, line, column, error) => {});

	window.onerror = mockOldOnError;

	setUpErrorReporting({ error: mockLogError } as any);

	window.onerror('message', 'url', 1, 2, new Error('Error'));

	expect(mockOldOnError.mock.calls[0][0]).toBe('message');
	expect(mockOldOnError.mock.calls[0][1]).toBe('url');
	expect(mockOldOnError.mock.calls[0][2]).toBe(1);
	expect(mockOldOnError.mock.calls[0][3]).toBe(2);
	expect(mockOldOnError.mock.calls[0][4]).toStrictEqual(new Error('Error'));

	const errorObj = mockLogError.mock.calls[0][0];
	errorObj.pinpoint.error.stack = "STACK";
	errorObj.stacktrace = "STACKTRACE";

	expect(mockLogError.mock.calls[0][0]).toStrictEqual({
		"column": 2,
		"jsFileUrl": "url",
		"lineNumber": 1,
		"message": "message",
		"messageIndexed": "message",
		"pinpoint": {
			"column": 2,
			"error": {
				"message": "Error",
				"name": "Error",
				"stack": "STACK"
			},
			"line": 1,
			"message": "message",
			"url": "url"
		},
		"stacktrace": "STACKTRACE"
	});

});
