package com.versa.particles.utils;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextResourceReader {

    public static String readTextFileFromResource(Context context, int resourceId) {
        StringBuilder builder = new StringBuilder();

        InputStream inputStream = context.getResources().openRawResource(resourceId);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while (true) {
            try {
                if (((line = bufferedReader.readLine()) == null)) {
                    break;
                }
                builder.append(line).append("\n");
            } catch (IOException e) {
                throw new RuntimeException(
                        "Could not open resource: " + resourceId, e);
            } catch (Resources.NotFoundException nfe) {
                throw new RuntimeException("Resource not found: " + resourceId, nfe);
            }

        }
        return builder.toString();
    }
}
