package com.randomrainbow.springboot.demosecurity.util;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import com.randomrainbow.springboot.demosecurity.dto.VideoDTO;
import com.randomrainbow.springboot.demosecurity.entity.Video;

public class Util {

    public static String randomString() {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 20;
    Random random = new Random();

    String generatedString = random.ints(leftLimit, rightLimit + 1)
      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
      .limit(targetStringLength)
      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
      .toString();

    return generatedString;
}

}
