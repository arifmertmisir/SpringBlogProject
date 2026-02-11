package org.misir.springproject.util.constants;

import java.io.File;

public class AppUtil {
    // Sets the slashes automatically based on the OS either uses "/" or "\"
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads";

    public static String getUploadPath(String fileName) {
        File uploadPath = new File(UPLOAD_DIR);

        // If no directory then create uploads dir
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        return uploadPath.getAbsolutePath() + File.separator + fileName;
    }
}