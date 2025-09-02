# GitHub Actions for TmdbAi

This directory contains GitHub Actions workflows and configurations for automated CI/CD, testing, and deployment of the TmdbAi Android application.

## Workflows Overview

### 1. Android CI/CD (`android-ci-cd.yml`)
Main workflow that handles:
- **Build and Test**: Compiles the app, runs tests, and builds debug APKs for all flavors
- **Release Build**: Creates signed release APKs and bundles for production
- **Security Checks**: Runs lint, dependency analysis, and vulnerability scans
- **Documentation**: Generates version information and build metadata

**Triggers:**
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop` branches
- Release publication

### 2. Pull Request Checks (`pull-request.yml`)
Comprehensive PR validation including:
- **Code Quality**: Formatting checks, static analysis, and linting
- **Security**: Vulnerability scanning and dependency analysis
- **Build Verification**: Ensures all flavors compile successfully
- **PR Comments**: Automatically comments with check results

**Triggers:**
- Pull requests to `main` or `develop` branches

### 3. Dependency Update (`dependency-update.yml`)
Automated dependency management:
- **Weekly Checks**: Runs every Monday at 9:00 AM UTC
- **Update Detection**: Identifies outdated dependencies
- **Automated PRs**: Creates PRs for dependency updates
- **Manual Trigger**: Can be run manually via workflow dispatch

**Triggers:**
- Scheduled (weekly)
- Manual dispatch

## Reusable Actions

### Setup Android Environment (`actions/setup-android/action.yml`)
Reusable action for setting up Android development environment:
- Java JDK setup
- Gradle configuration
- Android SDK installation
- Caching optimization

## Required Secrets

To use these workflows, you need to configure the following secrets in your GitHub repository:

### For Signing (Release Builds)
```bash
KEYSTORE_BASE64          # Base64 encoded keystore file
KEY_ALIAS                # Keystore alias
KEYSTORE_PASSWORD        # Keystore password
KEY_PASSWORD             # Key password
```

### For Security Scanning
```bash
SNYK_TOKEN               # Snyk API token for vulnerability scanning
```

### For Notifications (Optional)
```bash
SLACK_WEBHOOK_URL        # Slack webhook for build notifications
DISCORD_WEBHOOK_URL      # Discord webhook for build notifications
```

## Configuration

### Version Management
The workflows automatically generate version information:
- `VERSION_NAME`: Based on branch name and build number
- `VERSION_CODE`: Incremental build number
- `GITHUB_SHA`: Git commit hash
- `BUILD_TIME`: Timestamp of build

### Build Flavors
The app supports multiple build flavors:
- **development**: Development environment with debug features
- **staging**: Staging environment for testing
- **production**: Production release builds

### Signing Configuration
Release builds are automatically signed using:
- V1, V2, V3, and V4 signing for maximum compatibility
- Environment-based keystore configuration
- Secure secret management

## Usage Examples

### Manual Workflow Dispatch
```bash
# Trigger dependency update check
gh workflow run dependency-update.yml

# Trigger manual build
gh workflow run android-ci-cd.yml
```

### Branch Protection
Configure branch protection rules to require:
- All CI checks to pass
- Security scans to complete
- Build verification to succeed

### Release Process
1. Create a new release tag
2. Workflow automatically builds signed APK and bundle
3. Assets are uploaded to the release
4. Release notes are generated

## Customization

### Adding New Flavors
1. Update `app/build.gradle.kts` with new flavor configuration
2. Add corresponding build steps in workflows
3. Update artifact upload paths

### Modifying Build Steps
1. Edit the appropriate workflow file
2. Add or remove steps as needed
3. Update dependencies and tools

### Environment-Specific Configuration
Use GitHub Environments to manage:
- Different signing keys per environment
- Environment-specific build parameters
- Deployment targets

## Troubleshooting

### Common Issues

#### Build Failures
- Check Gradle version compatibility
- Verify Android SDK installation
- Review dependency conflicts

#### Signing Issues
- Ensure keystore secrets are properly configured
- Verify keystore file format and encoding
- Check alias and password configuration

#### Cache Issues
- Clear GitHub Actions cache if needed
- Verify cache key configuration
- Check cache size limits

### Debug Mode
Enable debug logging by setting:
```bash
GRADLE_OPTS="-Dorg.gradle.debug=true"
```

## Performance Optimization

### Caching Strategy
- Gradle packages and dependencies
- Android SDK components
- Build artifacts between jobs

### Parallel Execution
- Multiple jobs run in parallel where possible
- Dependency-based job ordering
- Resource optimization

### Resource Management
- Memory allocation for Gradle builds
- CPU optimization for compilation
- Network bandwidth for artifact uploads

## Security Considerations

### Secret Management
- Never commit secrets to version control
- Use GitHub Secrets for sensitive data
- Rotate keys regularly

### Dependency Scanning
- Regular vulnerability checks
- Automated security updates
- Compliance monitoring

### Build Integrity
- Signed builds for verification
- Checksum validation
- Audit trail maintenance

## Support and Maintenance

### Regular Tasks
- Update workflow dependencies
- Review security advisories
- Monitor build performance
- Update documentation

### Monitoring
- Build success rates
- Performance metrics
- Security scan results
- Dependency update frequency

For questions or issues, please:
1. Check the workflow logs
2. Review the configuration
3. Consult the troubleshooting guide
4. Create an issue in the repository
