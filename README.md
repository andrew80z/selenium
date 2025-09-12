# Selenium Test Framework

This project is a Selenium-based test automation framework that supports multiple browsers and environments.

## Configuration Files

### Environment Configuration (`dataFiles/envs.json`)
Contains environment-specific configurations for:
- Development (dev)
- QA Testing (qa)
- Staging (staging)
- Production (prod)

### Browser Configuration (`dataFiles/browser-config.json`)
Contains browser-specific settings for:
- Chrome
- Firefox

## Running Tests

### Basic Usage

Run all tests with default settings (Chrome browser, QA environment):
```
mvn test
```

### Specifying Browser

Run tests with Chrome:
```
mvn test -Dbrowser=chrome
```

Run tests with Firefox:
```
mvn test -Dbrowser=firefox
```
Run tests headless: 
mvn test -Dheadless=true
```
### Specifying Environment

Available environments:
- dev
- qa (default)
- staging
- prod

Example:
```
mvn test -Denv=staging
```

### Combining Browser and Environment

```
mvn test -Dbrowser=firefox -Denv=staging
```

### Running Specific Tests

Run a specific test class:
```
mvn test -Dtest=WebTests
```

Run a specific test method:
```
mvn test -Dtest=WebTests#testABTestPage
```

## Browser Support

The framework supports the following browsers:
- Chrome (default)
- Firefox

Make sure you have both browsers installed on your system. WebDriverManager will automatically download and manage the appropriate drivers.

## Test Reports

Test reports can be found in:
- `target/surefire-reports/` - Contains detailed test execution reports
- `traceLogs.json` - Contains browser performance logs (when applicable)

## Code Quality Tools

### Code Formatting
This project uses Spotless with Google Java Format for code formatting. To format your code:

```bash
# Check code formatting
mvn spotless:check

# Apply code formatting
mvn spotless:apply
```

### Code Style Checking
Checkstyle is configured to enforce coding standards. To check your code:

```bash
# Run checkstyle
mvn checkstyle:check
```

Style rules are defined in `checkstyle.xml` and follow Google Java Style with some customizations:
- Maximum line length: 120 characters
- Maximum method length: 60 lines
- No star imports
- Required braces for control statements
- Required whitespace rules
- Class design checks

Test report view:
```
 Start-Process "target/surefire-reports/html/index.html"