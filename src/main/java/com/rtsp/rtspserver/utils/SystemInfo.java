package com.rtsp.rtspserver.utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SystemInfo  {
    public static long getFreeDiskSpace() {
            File root = new File("/");
            return root.getUsableSpace(); // returns free space in bytes
}
}

