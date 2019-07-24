package com.apps.m.tielbeke4


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import org.junit.jupiter.api.Test


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleInstrumentedTest {

    /*lateinit var target: String*/

    /*@Before
    fun setUp() {
        target = FireBaseDao().getFilialen()?.value?.find {
            val compare : Short = 167
            it.filiaalnummer== compare }.toString()
        Log.d("TEST",target)
    }*/

    /*@Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.apps.m.tielbeke4", appContext.packageName)
    }*/

    @Test
    fun enterNumber () {

        onView(withId(R.id.filiaal_num)).perform(typeText("167"))
        onView(withId(R.id.zoek)).perform(click())
        onView(withId(R.id.text_gegevens)).check(matches(withText("x"
        )))
    }
}
