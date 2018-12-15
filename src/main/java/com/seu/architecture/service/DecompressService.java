package com.seu.architecture.service;


import java.io.File;
import java.util.List;

/**
 * Created by 17858 on 2017-09-19.
 */
public interface DecompressService {

    List<String> decompress(File file, String destDir);

    List<String> unRar(File file, String destDir);

    List<String> unZip(File file, String destDir);

}
