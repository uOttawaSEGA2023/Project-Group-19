package com.example.hams;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static final String pass = "pass";
    private static final String fail = "fail";


    Context mMockContext;

    @Test
    public void testPwLength() {

        PatientForm myObjectUnderTest = new PatientForm(mMockContext);

        //Check if the password is at least 6 characters long.
        String result = myObjectUnderTest.validatePW("123456");


        assertThat(result, is(pass));
    }

    @Test
    public void testPhoneNumber() {

        DoctorForm myObjectUnderTest = new DoctorForm(mMockContext);

        //Check if the phone number is 10 characters long.
        String result = myObjectUnderTest.validatePhoneNumber("9055001000");


        assertThat(result, is(pass));
    }

    @Test
    public void testEmptyUsername() {

        MainActivity myObjectUnderTest = new MainActivity(mMockContext);

        //Check if the email field for logging in is not empty.
        String result = myObjectUnderTest.validateUsernameField("geegee@gmail.com");


        assertThat(result, is(pass));

    }

    @Test
    public void testEmptyAddress() {

        DoctorForm myObjectUnderTest = new DoctorForm(mMockContext);

        //Check if the address field is not empty
        String result = myObjectUnderTest.validateAddressField("1111 bob gapp drive");


        assertThat(result, is(pass));

    }

}