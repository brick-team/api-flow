package com.github.brick.apiflow;

public interface Extract {
    Object extract(Object o, String el);

    Object extract(String json, String el);
}
