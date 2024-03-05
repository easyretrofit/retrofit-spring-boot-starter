package io.github.liuziyuan.retrofit.core.util;

import io.github.liuziyuan.retrofit.core.util.AntPathStringMatcher;
import io.github.liuziyuan.retrofit.core.util.PathMatcher;
import io.github.liuziyuan.retrofit.core.util.StringUtils;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class AntPathMatcher implements PathMatcher {

    public static final String DEFAULT_PATH_SEPARATOR = "/";

    private static final int CACHE_TURNOFF_THRESHOLD = 65536;

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{[^/]+?}");

    private static final char[] WILDCARD_CHARS = { '*', '?', '{' };

    private String pathSeparator;

    private boolean caseSensitive = true;

    private boolean trimTokens = false;

    @Nullable
    private volatile Boolean cachePatterns;

    private final Map<String, String[]> tokenizedPatternCache = new ConcurrentHashMap<>(256);

    final Map<String, io.github.liuziyuan.retrofit.core.util.AntPathStringMatcher> stringMatcherCache = new ConcurrentHashMap<>(256);

    /**
     * Create a new instance with the {@link #DEFAULT_PATH_SEPARATOR}.
     */
    public AntPathMatcher() {
        this.pathSeparator = DEFAULT_PATH_SEPARATOR;
    }

    @Override
    public boolean match(String pattern, String path) {
        return doMatch(pattern, path, true, null);
    }

    protected boolean doMatch(String pattern, @Nullable String path, boolean fullMatch,
                              @Nullable Map<String, String> uriTemplateVariables) {

        if (path == null || path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
            return false;
        }

        String[] pattDirs = tokenizePattern(pattern);
        if (fullMatch && this.caseSensitive && !isPotentialMatch(path, pattDirs)) {
            return false;
        }

        String[] pathDirs = tokenizePath(path);
        int pattIdxStart = 0;
        int pattIdxEnd = pattDirs.length - 1;
        int pathIdxStart = 0;
        int pathIdxEnd = pathDirs.length - 1;

        // Match all elements up to the first **
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String pattDir = pattDirs[pattIdxStart];
            if ("**".equals(pattDir)) {
                break;
            }
            if (!matchStrings(pattDir, pathDirs[pathIdxStart], uriTemplateVariables)) {
                return false;
            }
            pattIdxStart++;
            pathIdxStart++;
        }

        if (pathIdxStart > pathIdxEnd) {
            // Path is exhausted, only match if rest of pattern is * or **'s
            if (pattIdxStart > pattIdxEnd) {
                return (pattern.endsWith(this.pathSeparator) == path.endsWith(this.pathSeparator));
            }
            if (!fullMatch) {
                return true;
            }
            if (pattIdxStart == pattIdxEnd && pattDirs[pattIdxStart].equals("*") && path.endsWith(this.pathSeparator)) {
                return true;
            }
            for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                if (!pattDirs[i].equals("**")) {
                    return false;
                }
            }
            return true;
        }
        else if (pattIdxStart > pattIdxEnd) {
            // String not exhausted, but pattern is. Failure.
            return false;
        }
        else if (!fullMatch && "**".equals(pattDirs[pattIdxStart])) {
            // Path start definitely matches due to "**" part in pattern.
            return true;
        }

        // up to last '**'
        while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            String pattDir = pattDirs[pattIdxEnd];
            if (pattDir.equals("**")) {
                break;
            }
            if (!matchStrings(pattDir, pathDirs[pathIdxEnd], uriTemplateVariables)) {
                return false;
            }
            pattIdxEnd--;
            pathIdxEnd--;
        }
        if (pathIdxStart > pathIdxEnd) {
            // String is exhausted
            for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
                if (!pattDirs[i].equals("**")) {
                    return false;
                }
            }
            return true;
        }

        while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
            int patIdxTmp = -1;
            for (int i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
                if (pattDirs[i].equals("**")) {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == pattIdxStart + 1) {
                // '**/**' situation, so skip one
                pattIdxStart++;
                continue;
            }
            // Find the pattern between padIdxStart & padIdxTmp in str between
            // strIdxStart & strIdxEnd
            int patLength = (patIdxTmp - pattIdxStart - 1);
            int strLength = (pathIdxEnd - pathIdxStart + 1);
            int foundIdx = -1;

            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    String subPat = pattDirs[pattIdxStart + j + 1];
                    String subStr = pathDirs[pathIdxStart + i + j];
                    if (!matchStrings(subPat, subStr, uriTemplateVariables)) {
                        continue strLoop;
                    }
                }
                foundIdx = pathIdxStart + i;
                break;
            }

            if (foundIdx == -1) {
                return false;
            }

            pattIdxStart = patIdxTmp;
            pathIdxStart = foundIdx + patLength;
        }

        for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
            if (!pattDirs[i].equals("**")) {
                return false;
            }
        }

        return true;
    }

    protected String[] tokenizePattern(String pattern) {
        String[] tokenized = null;
        Boolean cachePatterns = this.cachePatterns;
        if (cachePatterns == null || cachePatterns.booleanValue()) {
            tokenized = this.tokenizedPatternCache.get(pattern);
        }
        if (tokenized == null) {
            tokenized = tokenizePath(pattern);
            if (cachePatterns == null && this.tokenizedPatternCache.size() >= CACHE_TURNOFF_THRESHOLD) {
                // Try to adapt to the runtime situation that we're encountering:
                // There are obviously too many different patterns coming in here...
                // So let's turn off the cache since the patterns are unlikely to be reoccurring.
                deactivatePatternCache();
                return tokenized;
            }
            if (cachePatterns == null || cachePatterns.booleanValue()) {
                this.tokenizedPatternCache.put(pattern, tokenized);
            }
        }
        return tokenized;
    }

    private void deactivatePatternCache() {
        this.cachePatterns = false;
        this.tokenizedPatternCache.clear();
        this.stringMatcherCache.clear();
    }

    protected String[] tokenizePath(String path) {
        return StringUtils.tokenizeToStringArray(path, this.pathSeparator, this.trimTokens, true);
    }

    private boolean isPotentialMatch(String path, String[] pattDirs) {
        if (!this.trimTokens) {
            int pos = 0;
            for (String pattDir : pattDirs) {
                int skipped = skipSeparator(path, pos, this.pathSeparator);
                pos += skipped;
                skipped = skipSegment(path, pos, pattDir);
                if (skipped < pattDir.length()) {
                    return (skipped > 0 || (pattDir.length() > 0 && isWildcardChar(pattDir.charAt(0))));
                }
                pos += skipped;
            }
        }
        return true;
    }

    private int skipSegment(String path, int pos, String prefix) {
        int skipped = 0;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (isWildcardChar(c)) {
                return skipped;
            }
            int currPos = pos + skipped;
            if (currPos >= path.length()) {
                return 0;
            }
            if (c == path.charAt(currPos)) {
                skipped++;
            }
        }
        return skipped;
    }

    private int skipSeparator(String path, int pos, String separator) {
        int skipped = 0;
        while (path.startsWith(separator, pos + skipped)) {
            skipped += separator.length();
        }
        return skipped;
    }

    private boolean isWildcardChar(char c) {
        for (char candidate : WILDCARD_CHARS) {
            if (c == candidate) {
                return true;
            }
        }
        return false;
    }

    private boolean matchStrings(String pattern, String str,
                                 @Nullable Map<String, String> uriTemplateVariables) {

        return getStringMatcher(pattern).matchStrings(str, uriTemplateVariables);
    }

    protected io.github.liuziyuan.retrofit.core.util.AntPathStringMatcher getStringMatcher(String pattern) {
        io.github.liuziyuan.retrofit.core.util.AntPathStringMatcher matcher = null;
        Boolean cachePatterns = this.cachePatterns;
        if (cachePatterns == null || cachePatterns.booleanValue()) {
            matcher = this.stringMatcherCache.get(pattern);
        }
        if (matcher == null) {
            matcher = new AntPathStringMatcher(pattern, this.caseSensitive);
            if (cachePatterns == null && this.stringMatcherCache.size() >= CACHE_TURNOFF_THRESHOLD) {
                // Try to adapt to the runtime situation that we're encountering:
                // There are obviously too many different patterns coming in here...
                // So let's turn off the cache since the patterns are unlikely to be reoccurring.
                deactivatePatternCache();
                return matcher;
            }
            if (cachePatterns == null || cachePatterns.booleanValue()) {
                this.stringMatcherCache.put(pattern, matcher);
            }
        }
        return matcher;
    }
}
