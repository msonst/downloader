package com.cs.download.server.api.proxy;

public record ProxyResult (long currentTimeMillis, String containerId, String type, String host, int privoxyPort) {
}
