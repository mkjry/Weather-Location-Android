package com.ssj.weather_location_android

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BasicUnitTest {
    private var myValue: Int = 0

    @Before
    fun setUp() {
        // This method is called before each test method is executed
        myValue = 42
    }

    @After
    fun tearDown() {
        // This method is called after each test method is executed
        // Clean up resources if needed
    }

    @Test
    fun testAddition() {
        // Basic assertion example
        val result = add(10, 20)
        assertEquals(30, result)
    }

    @Test
    fun testSubtraction() {
        // Another basic assertion example
        val result = subtract(50, 30)
        assertEquals(20, result)
    }

    @Test
    fun testMyValue() {
        // Accessing values set in setUp method
        assertEquals(42, myValue)
    }

    // Sample functions to test
    private fun add(a: Int, b: Int): Int {
        return a + b
    }

    private fun subtract(a: Int, b: Int): Int {
        return a - b
    }
}