package com.kakaopay.task.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogUtil {
    public static String printData(Object obj) throws JsonProcessingException {
        ObjectMapper mappper = new ObjectMapper();

        return mappper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
