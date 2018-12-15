package com.seu.architecture.service;

import java.util.List;

/**
 * Created by 17858 on 2017/5/15.
 */
public interface PdfService {

    String pdfToImage(String pdfName,String path, int page);

    void pdfToImageFolder(String pdfPath);

    List<String> pdfToImageList(List<String> pdfList, String path);

}
