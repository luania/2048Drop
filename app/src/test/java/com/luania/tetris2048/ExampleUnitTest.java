package com.luania.tetris2048;

import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void json() {
        int[][] a = new int[2][2];
        String str = new Gson().toJson(a);
        System.out.println(new Gson().fromJson(str, int[][].class));
    }
}