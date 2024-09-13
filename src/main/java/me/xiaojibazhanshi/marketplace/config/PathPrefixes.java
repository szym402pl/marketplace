package me.xiaojibazhanshi.marketplace.config;

public enum PathPrefixes {

    CMD_PERMS("command-permissions")
    ;

    private final String path;

    PathPrefixes(String path) { this.path = path; }

    public String getPath() { return path; }
}
