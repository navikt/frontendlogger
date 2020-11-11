# FrontendloggerJs

Lite bibliotek som forenkler integrasjonen mot frontendlogger.

## Usage
```typescript

export const logger = createFrontendLogger(DEFAULT_FRONTENDLOGGER_API_URL, 'my-app-name');
export const mockedLogger = createMockFrontendLogger('my-app-name');

// Logging 
logger.info('Info');
logger.warn('Warn');
logger.error('Error');

// Metrics
logger.event('navn-pa-metrikk', { field1: 'value1' }, { tag1: 'value2' });

// Log errors with window.onerror
setUpErrorReporting(logger);

```

