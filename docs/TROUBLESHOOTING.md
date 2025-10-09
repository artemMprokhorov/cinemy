# Cinemy Troubleshooting

## Build Issues

**Q: "Could not find variant 'developmentDebug'"**
A: Use new variants: `dummyDebug`, `prodDebug`, `prodRelease`

**Q: BuildConfig errors**  
A: Clean and sync project: `./gradlew clean`

## Runtime Issues

**Q: App shows only mock data**
A: Check variant - `dummyDebug` always uses mock data

**Q: "Connection failed" message**
A: 
1. Check MCP_SERVER_URL in build.gradle.kts
2. Verify backend is running
3. Use retry button

**Q: No movies loading**
A: Clear app data and restart

## Backend Integration

**Q: How to test real backend?**
A:
1. Start your MCP server
2. Update MCP_SERVER_URL 
3. Use prodDebug variant

**Q: Mock fallback not working**
A: Check logs for "falling back to mock" message
