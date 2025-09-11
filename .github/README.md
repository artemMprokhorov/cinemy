# GitHub Actions & CI/CD

This directory contains GitHub Actions workflows and configurations for the Cinemy Android application.

## 📁 Directory Structure

```
.github/
├── README.md                    # This file - GitHub Actions overview
├── workflows/                   # CI/CD workflows
│   ├── WORKFLOWS.md            # Detailed workflows documentation
│   ├── android-ci-cd.yml       # Main CI/CD pipeline
│   ├── build-release-apk.yml   # Release build workflow
│   ├── dependency-update.yml   # Dependency management
│   ├── pull-request.yml        # PR validation
│   └── simple-test.yml         # Quick testing
└── actions/                     # Reusable actions
    └── setup-android/          # Android environment setup
        └── action.yml
```

## 🚀 Quick Start

### Main Workflows
- **[android-ci-cd.yml](workflows/android-ci-cd.yml)** - Main CI/CD pipeline
- **[pull-request.yml](workflows/pull-request.yml)** - PR validation
- **[dependency-update.yml](workflows/dependency-update.yml)** - Automated dependency updates

### Documentation
- **[WORKFLOWS.md](workflows/WORKFLOWS.md)** - Detailed workflows documentation
- **[Main Project README](../README.md)** - Project overview and setup

## 🔧 Setup

### Required Secrets
Configure these secrets in your GitHub repository settings:

```bash
# For Release Builds
KEYSTORE_BASE64          # Base64 encoded keystore file
KEY_ALIAS                # Keystore alias
KEYSTORE_PASSWORD        # Keystore password
KEY_PASSWORD             # Key password

# For Security Scanning
SNYK_TOKEN               # Snyk API token

# For Notifications (Optional)
SLACK_WEBHOOK_URL        # Slack webhook
DISCORD_WEBHOOK_URL      # Discord webhook
```

### Build Variants
- **dummyDebug** - Development with mock data
- **prodDebug** - Testing with real backend
- **prodRelease** - Production release

## 📊 Workflow Status

| Workflow | Status | Description |
|----------|--------|-------------|
| [android-ci-cd](workflows/android-ci-cd.yml) | ✅ Active | Main CI/CD pipeline |
| [pull-request](workflows/pull-request.yml) | ✅ Active | PR validation |
| [dependency-update](workflows/dependency-update.yml) | ✅ Active | Weekly dependency checks |
| [build-release-apk](workflows/build-release-apk.yml) | ✅ Active | Release builds |
| [simple-test](workflows/simple-test.yml) | ✅ Active | Quick testing |

## 🛠️ Usage

### Manual Triggers
```bash
# Trigger dependency update
gh workflow run dependency-update.yml

# Trigger manual build
gh workflow run android-ci-cd.yml

# Trigger release build
gh workflow run build-release-apk.yml
```

### Branch Protection
Configure branch protection rules to require:
- All CI checks to pass
- Security scans to complete
- Build verification to succeed

## 📚 Documentation

- **[WORKFLOWS.md](workflows/WORKFLOWS.md)** - Complete workflows documentation
- **[Main README](../README.md)** - Project overview and setup
- **[Development Guide](../DEVELOPMENT_GUIDE.md)** - Development guidelines
- **[Project Guide](../PROJECT_GUIDE.md)** - Project documentation

## 🔍 Troubleshooting

### Common Issues
- **Build Failures**: Check Gradle version and Android SDK
- **Signing Issues**: Verify keystore secrets configuration
- **Cache Issues**: Clear GitHub Actions cache if needed

### Debug Mode
Enable debug logging:
```bash
GRADLE_OPTS="-Dorg.gradle.debug=true"
```

## 📞 Support

For questions or issues:
1. Check workflow logs
2. Review configuration
3. Consult [WORKFLOWS.md](workflows/WORKFLOWS.md)
4. Create an issue in the repository

---

**Last Updated**: 2025-01-XX  
**Version**: 2.4.4  
**Status**: Active
