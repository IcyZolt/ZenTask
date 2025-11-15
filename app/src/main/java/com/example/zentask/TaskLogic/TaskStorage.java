package com.example.zentask.TaskLogic;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class TaskStorage {
    /*in the future we would have to make this class into a database writer and fetcher class
    for now it saves on a local file in order to not be reliant on internet
    and for small showcase of the prototype
     */
    private static final String FILE_NAME = "tasks.txt";

    public static void saveTasks(Context context, List<Task> tasks){
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE),
                StandardCharsets.UTF_8))) {
        for(Task t : tasks){
            JSONObject obj = new JSONObject();
            obj.put("name", t.name);
            obj.put("date", t.date);
            obj.put("description", t.description);
            bw.write(obj.toString());
            bw.newLine();
        }
        } catch(IOException e){
            Log.e("TaskStorage", "Failed to save tasks", e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Task> loadTasks(Context context){
        List<Task> list = new ArrayList<>();
        File file = new File(context.getFilesDir(),FILE_NAME);

        if(!file.exists()) return list;

        try(BufferedReader br = new BufferedReader(new InputStreamReader(
                context.openFileInput(FILE_NAME), StandardCharsets.UTF_8
        ))) {

            String line;
            while((line=br.readLine())!=null) {
                try {
                    JSONObject obj = new JSONObject(line);

                    String name = obj.optString("name", "");
                    String date = obj.optString("date", "");
                    String desc = obj.optString("description", "");

                    list.add(new Task(name, date, desc));
            } catch (org.json.JSONException e){
                    Log.e("TaskStorage", "Bad JSON line, skipping: " + line, e);
                }
          }
        } catch (IOException e){
            Log.e("TaskStorage", "Failed to load task", e);
        }
        return list;
    }
}

