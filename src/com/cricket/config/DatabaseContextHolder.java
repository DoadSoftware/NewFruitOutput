package com.cricket.config;

public class DatabaseContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDb(String dbType) {
        contextHolder.set(dbType);
    }

    public static String getDb() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}