package com.rds.demo;

import com.rds.demo.api.Message;

import java.util.UUID;

public class TestMessage implements Message {
    public final UUID key = UUID.randomUUID();
}
