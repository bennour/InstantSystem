package com.salah.instantsystem.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.meanbean.test.BeanTester;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ParkingVMTest {

    @Test
    public void gettersAndSettersCorrectness() {
        BeanTester tester = new BeanTester();
        tester.testBean(ParkingVM.class);
    }
}