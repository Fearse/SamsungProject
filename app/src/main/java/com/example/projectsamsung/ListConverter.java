package com.example.projectsamsung;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListConverter {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public String fromHobbies(ArrayList<String> strings) {
        return String.join(",", strings);
    }

    @TypeConverter
    public ArrayList<String> toHobbies(String data) {
        return new ArrayList<>(Arrays.asList(data.split(",")));
    }
}
