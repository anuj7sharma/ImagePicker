package com.imagepicker;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    double fValue1 = 2.0;
    double fValue2 = 3.0;

    public static void main(String[] args){
        ExampleUnitTest obj = new ExampleUnitTest();
        try {
            obj.testAdd();
//            obj.testMultiplicationOfZero();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test(timeout = 100)
    public void testMultiplicationOfZero() throws Exception{
        int x = 10;
        int y = 1;
        assertTrue("its working",(x*y!=0));
        if(x*y == 0){
            System.out.println("output is zero");
        }else{
            System.out.println("output not zero");
        }
    }

    @Test
    public void testAdd() {
        double result= fValue1 + fValue2;
        assertTrue(result == 5.0);
    }
}