# FrontendloggerJs

Lite bibliotek som forenkler integrasjonen mot frontendlogger.

Installer: `npm install @navikt/frontendlogger --save` 

Bruk:
```typescript
import {
	createFrontendLogger,
	createMockFrontendLogger,
	DEFAULT_FRONTENDLOGGER_API_URL,
    setUpErrorReporting
} from '@navikt/frontendlogger';

export const logger = createFrontendLogger('my-app-name', DEFAULT_FRONTENDLOGGER_API_URL);
//export const logger = createMockFrontendLogger('my-app-name');

// Logging 
logger.info('Info');
logger.warn('Warn');
logger.error('Error');

// Metrics
logger.event('navn-pa-metrikk', { field1: 'value1' }, { tag1: 'value2' });

// Log errors with window.onerror
setUpErrorReporting(logger);
```

**NB** `setUpErrorReporting` bør kalles så tidlig som mulig siden dette setter opp den globale error-håndteringen

