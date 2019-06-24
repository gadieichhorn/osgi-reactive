package com.rds.demo.provider;

import io.reactivex.schedulers.Schedulers;
import org.junit.Assert;
import org.junit.Test;

public class SchedulerTypeTest {

    @Test
    public void getIoByNameTest() {
        Assert.assertEquals(SchedulerType.IO, SchedulerType.get("IO"));
    }

    @Test
    public void getSchedulerIoByNameTest() {
        Assert.assertEquals(Schedulers.io(), SchedulerType.get("IO").value());
    }

    @Test
    public void getComputationByNameTest() {
        Assert.assertEquals(SchedulerType.COMPUTATION, SchedulerType.get("COMPUTATION"));
    }

    @Test
    public void getSchedulerComputationByNameTest() {
        Assert.assertEquals(Schedulers.computation(), SchedulerType.get("COMPUTATION").value());
    }

    @Test
    public void getTrampolineByNameTest() {
        Assert.assertEquals(SchedulerType.TRAMPOLINE, SchedulerType.get("TRAMPOLINE"));
    }

    @Test
    public void getSchedulerTrampolineByNameTest() {
        Assert.assertEquals(Schedulers.trampoline(), SchedulerType.get("TRAMPOLINE").value());
    }

    @Test
    public void getDefaultTest() {
        Assert.assertEquals(SchedulerType.COMPUTATION, SchedulerType.get(""));
    }
}