package com.example.zentask;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UserStorage {
    private static final String FILE_NAME = "user_info.txt"; // text, not binary

    public static void saveUsername(Context context, String username) {
        String clean = (username == null) ? "" : username.trim();
        try (OutputStream os = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {

            bw.write(clean);
            bw.newLine(); // end the line so readLine works consistently
            bw.flush();
            os.flush();
        } catch (IOException e) {
            Log.e("UserStorage", "Failed to save username", e);
        }

        logFileInfo(context);
    }

    public static String loadUsername(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            Log.d("UserStorage", "Username file missing or empty");
            logFileInfo(context);
            return null;
        }
        try (InputStream is = context.openFileInput(FILE_NAME);
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {

            String line = br.readLine();
            String result = (line == null) ? null : line.trim();
            Log.d("UserStorage", "Loaded username: " + result);
            return (result == null || result.isEmpty()) ? null : result;
        } catch (IOException e) {
            Log.e("UserStorage", "Failed to read username", e);
            return null;
        }
    }

    public static boolean hasUsername(Context context) {
        File f = new File(context.getFilesDir(), FILE_NAME);
        return f.exists() && f.length() > 0;
    }

    private static void logFileInfo(Context context) {
        try {
            File f = new File(context.getFilesDir(), FILE_NAME);
            if (!f.exists()) {
                Log.d("UserStorage", "File not found: " + f.getAbsolutePath());
                return;
            }
            Log.d("UserStorage", "Path=" + f.getAbsolutePath() + " size=" + f.length() + " bytes");
            // Dump first up-to-64 bytes as hex to verify it’s UTF-8 text
            try (FileInputStream fis = new FileInputStream(f)) {
                byte[] buf = new byte[64];
                int n = fis.read(buf);
                if (n > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < n; i++) sb.append(String.format("%02X ", buf[i]));
                    Log.d("UserStorage", "HEAD_HEX: " + sb.toString());
                }
            }
        } catch (Exception e) {
            Log.e("UserStorage", "logFileInfo failed", e);
        }
    }
}
