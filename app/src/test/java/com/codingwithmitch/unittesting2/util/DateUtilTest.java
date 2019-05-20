package com.codingwithmitch.unittesting2.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.Random;

import static com.codingwithmitch.unittesting2.util.DateUtil.GET_MONTH_ERROR;
import static com.codingwithmitch.unittesting2.util.DateUtil.getMonthFromNumber;
import static com.codingwithmitch.unittesting2.util.DateUtil.monthNumbers;
import static com.codingwithmitch.unittesting2.util.DateUtil.months;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilTest {

    private static final String today = "05-2019"; // MM-yyyy

    @Test
    public void testGetCurrentTimestamp_returnTimestamp(){
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                assertEquals(today, DateUtil.getCurrentTimeStamp());
                System.out.println("Timestamp is generated correctly.");
            }
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8,9,10,11})
    public void getMonthFromNumber_returnSuccess(int monthNumber, TestInfo testInfo, TestReporter testReporter){
        assertEquals(months[monthNumber], DateUtil.getMonthFromNumber(monthNumbers[monthNumber]));
        System.out.println(monthNumbers[monthNumber] + " : " + months[monthNumber]);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,10,11})
    void testgetMonthFromNumber_returnError(int monthNumber, TestInfo testInfo, TestReporter testReporter) {
        int randomInt = new Random().nextInt(90) + 10; // any number from 10 - 100
        assertEquals(getMonthFromNumber(String.valueOf(monthNumber * randomInt)), GET_MONTH_ERROR);
        System.out.println(monthNumbers[monthNumber] + " : " + GET_MONTH_ERROR);
    }
}





















