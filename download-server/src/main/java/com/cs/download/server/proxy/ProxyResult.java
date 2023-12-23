package com.cs.download.server.proxy;

public record ProxyResult (long currentTimeMillis, String containerId, String type, String host, int privoxyPort) {
}
