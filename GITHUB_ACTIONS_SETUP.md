# GitHub Actions Setup for TmdbAi

## Overview

This document summarizes the comprehensive GitHub Actions setup implemented for the TmdbAi Android project. The setup includes automated CI/CD, testing, security scanning, and deployment workflows.

## Recent Updates (v2.4.1)

### GitHub Actions Android SDK Fix
- **Issue Resolved**: Fixed 502 HTTP errors from `android-actions/setup-android@v3`
- **Solution**: Replaced with manual Android SDK setup using `wget` and `sdkmanager`
- **Benefits**: More reliable builds, faster execution, no emulator download issues
- **New Workflows**: Added `simple-test.yml` for lightweight unit testing

### Enhanced ML Model v2.0.0 Integration
- **ML Model Upgrade**: Enhanced Keyword Model v2.0.0 with 85%+ accuracy
- **New Features**: Intensity modifiers, context boosters, expanded dictionary
- **Testing**: Comprehensive unit tests for all ML features
- **Documentation**: Updated README.md and CHANGELOG.md

## Changes Made

### 1. App Build Configuration (`app/build.gradle.kts`)

#### Version Management
- **Dynamic Versioning**: Added functions to read version information from `version.properties` file or environment variables
- **Build Metadata**: Added build config fields for build time, git SHA, version name, and version code
- **Environment Variables**: Support for `VERSION_NAME`, `VERSION_CODE`, and `GITHUB_SHA` from GitHub Actions

#### Build Flavors
- **Development**: Debug builds with development API endpoints and logging enabled
- **Staging**: Debug builds with staging API endpoints and logging enabled  
- **Production**: Release builds with production API endpoints and logging disabled

#### Signing Configuration
- **Release Signing**: Configured for production release builds with V1, V2, V3, and V4 signing
- **Environment Variables**: Uses GitHub Secrets for keystore configuration
- **Conditional Signing**: Only applies to production release builds

#### Build Automation
- **Version Info Generation**: Automatically generates `version.json` in assets
- **APK Copying**: Copies release APKs to project root with versioned naming
- **Task Dependencies**: Ensures version generation before build tasks

### 2. GitHub Actions Workflows

#### Main CI/CD Workflow (`.github/workflows/android-ci-cd.yml`)
- **Triggers**: Push to main/develop, PRs, and releases
- **Jobs**:
  - Build and Test: Compiles app, runs tests, builds debug APKs
  - Release Build: Creates signed release APKs and bundles
  - Security Checks: Runs lint, dependency analysis, vulnerability scans
  - Documentation: Generates version information and build metadata

#### Build and Release Workflow (`.github/workflows/build-release-apk.yml`) - **FIXED**
- **Issue Fixed**: Resolved Android SDK setup 502 errors
- **Manual SDK Setup**: Uses `wget` and `sdkmanager` for reliable installation
- **No Emulator**: Disabled emulator download to avoid server issues
- **Fallback Support**: Robust error handling and retry logic
- **Features**: Multi-flavor builds, signing, release creation

#### Simple Test Workflow (`.github/workflows/simple-test.yml`) - **NEW**
- **Lightweight**: Unit tests only, no Android SDK dependency
- **Fast Execution**: Quick CI checks for basic validation
- **No Dependencies**: Runs without complex Android setup
- **Quality Checks**: Lint, formatting, static analysis

#### Pull Request Workflow (`.github/workflows/pull-request.yml`)
- **Code Quality**: Formatting checks, static analysis, linting
- **Security**: Vulnerability scanning and dependency analysis
- **Build Verification**: Ensures all flavors compile successfully
- **PR Comments**: Automatically comments with check results

#### Dependency Update Workflow (`.github/workflows/dependency-update.yml`)
- **Scheduled**: Runs every Monday at 9:00 AM UTC
- **Update Detection**: Identifies outdated dependencies
- **Automated PRs**: Creates PRs for dependency updates
- **Manual Trigger**: Can be run manually via workflow dispatch

### 3. Reusable Actions

#### Android Environment Setup (`.github/actions/setup-android/action.yml`)
- **Java Setup**: Configures JDK 17 with Temurin distribution
- **Gradle Setup**: Installs and configures Gradle 8.5
- **Android SDK**: Installs Android SDK 34 with build tools
- **Caching**: Optimizes Gradle and SDK caching

### 4. Configuration Files

#### GitHub Actions Config (`.github/actions.yml`)
- **Permissions**: Defines workflow permissions and access
- **Environments**: Configures development, staging, and production environments
- **Branch Protection**: Guidelines for branch protection rules

#### Version Template (`version.properties.template`)
- **Template Format**: Shows expected version properties structure
- **Auto-Generation**: Used by GitHub Actions to create version files

## Required GitHub Secrets

### For Release Signing
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

## Workflow Features

### Automated Versioning
- **Version Names**: Based on branch name and build number
- **Version Codes**: Incremental build numbers
- **Git Integration**: Includes commit SHA and build timestamp
- **Environment Support**: Different versions for different environments

### Multi-Flavor Building
- **Development**: `com.example.tmdbai.dev` with debug features
- **Staging**: `com.example.tmdbai.staging` for testing
- **Production**: `com.example.tmdbai` for release

### Security and Quality
- **Lint Checks**: Code quality and style validation
- **Dependency Scanning**: Vulnerability detection and updates
- **Static Analysis**: Code quality and security analysis
- **Automated Testing**: Unit tests and integration tests

### Artifact Management
- **Debug APKs**: Available for all flavors
- **Release APKs**: Signed production builds
- **Release Bundles**: Play Store compatible AAB files
- **Version Metadata**: JSON files with build information

## Usage Instructions

### 1. Initial Setup
1. **Fork/Clone** the repository
2. **Configure Secrets** in GitHub repository settings
3. **Push to Main** to trigger first workflow run
4. **Verify Builds** in Actions tab

### 2. Development Workflow
1. **Create Feature Branch** from develop
2. **Make Changes** and commit
3. **Push Branch** to trigger PR checks
4. **Create PR** to develop branch
5. **Review Results** from automated checks
6. **Merge** after approval

### 3. Release Process
1. **Create Release Tag** in GitHub
2. **Workflow Automatically** builds signed APK and bundle
3. **Assets Uploaded** to release page
4. **Release Notes** generated automatically

### 4. Dependency Updates
1. **Weekly Checks** run automatically
2. **Update PRs** created for outdated dependencies
3. **Review Changes** and merge if safe
4. **Monitor Security** alerts and updates

## Customization Options

### Adding New Flavors
1. Update `app/build.gradle.kts` with new flavor
2. Add build steps in workflows
3. Update artifact upload paths

### Modifying Build Steps
1. Edit workflow files as needed
2. Add or remove steps
3. Update dependencies and tools

### Environment Configuration
1. Use GitHub Environments for different configs
2. Configure environment-specific secrets
3. Set up deployment targets

## Troubleshooting

### Common Issues

#### Android SDK Setup Errors (FIXED in v2.4.1)
- **502 HTTP Errors**: Fixed by replacing `android-actions/setup-android@v3` with manual setup
- **Emulator Download Issues**: Resolved by disabling emulator download (`emulator: false`)
- **SDK Manager Failures**: Fixed with direct `wget` download and proper path setup
- **Solution**: Use the updated `build-release-apk.yml` workflow with manual SDK installation

#### Build Failures
- Check Gradle version compatibility
- Verify Android SDK installation
- Review dependency conflicts
- Use `simple-test.yml` for quick unit test validation

#### Signing Issues
- Ensure keystore secrets are configured
- Verify keystore file format
- Check alias and password configuration

#### Workflow Failures
- Review workflow logs
- Check secret configuration
- Verify branch protection rules
- Use backup workflows if main workflow fails

### Debug Mode
Enable debug logging:
```bash
GRADLE_OPTS="-Dorg.gradle.debug=true"
```

## Performance Features

### Caching Strategy
- **Gradle Packages**: Dependencies and build artifacts
- **Android SDK**: Platform tools and build tools
- **Build Artifacts**: Intermediate build outputs

### Parallel Execution
- **Multiple Jobs**: Run in parallel where possible
- **Dependency Ordering**: Optimized job sequencing
- **Resource Management**: Efficient resource utilization

## Security Considerations

### Secret Management
- **Never Commit**: Secrets to version control
- **Use GitHub Secrets**: For sensitive data
- **Regular Rotation**: Update keys periodically

### Build Integrity
- **Signed Builds**: For verification
- **Checksum Validation**: For integrity
- **Audit Trail**: For compliance

## Monitoring and Maintenance

### Regular Tasks
- Update workflow dependencies
- Review security advisories
- Monitor build performance
- Update documentation

### Metrics to Track
- Build success rates
- Performance metrics
- Security scan results
- Dependency update frequency

## Support

For questions or issues:
1. Check workflow logs
2. Review configuration
3. Consult troubleshooting guide
4. Create repository issue

## Next Steps

1. **Configure Secrets** in GitHub repository
2. **Test Workflows** with sample builds
3. **Set Up Branch Protection** rules
4. **Configure Notifications** for build status
5. **Monitor Performance** and optimize as needed

---

*This setup provides a robust, automated CI/CD pipeline for the TmdbAi Android application with comprehensive testing, security scanning, and deployment automation.*
