package com.seu.architecture.service.impl;

import com.seu.architecture.config.Constants;
import com.seu.architecture.service.PdfService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17858 on 2017/5/15.
 */
@Service
public class PdfServiceImpl implements PdfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfServiceImpl.class);

    @Override
    public String pdfToImage(String pdfName, String path, int page) {    //每次转一张PDF
        File file = new File(pdfName);
        String imgName = "";
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            BufferedImage image = renderer.renderImageWithDPI(0, 400);
            int dotPos = pdfName.lastIndexOf(".");
            imgName = path + "\\" + page + ".jpg";
            ImageIO.write(image, "jpg", new File(imgName));
            doc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgName;
    }

    @Override
    public List<String> pdfToImageList(List<String> pdfList, String path) {
        List<String> imageList = new ArrayList<>();
        for (int i = 0; i < pdfList.size(); i++) {
            imageList.add(pdfToImage(pdfList.get(i),path,i+1));
        }
        return imageList;
    }

    @Override
    public void pdfToImageFolder(String pdfPath) {       //每次转一本书的所有PDF
        File originalPdfPath = new File(pdfPath);
        File pdfList[] = originalPdfPath.listFiles();
        try {
            for (int i = 0; i < pdfList.length; i++) {
                int dotPos = pdfList[i].getName().lastIndexOf(".");
                String fileExt = pdfList[i].getName().substring(dotPos + 1).toLowerCase();
                if (fileExt.equals("pdf")) {
                    PDDocument doc = PDDocument.load(pdfList[i]);
                    PDFRenderer renderer = new PDFRenderer(doc);
                    int pageCount = doc.getNumberOfPages();
                    for (int j = 0; j < pageCount; j++) {
                        BufferedImage image = renderer.renderImageWithDPI(j, 296);
                        //BufferedImage image = renderer.renderImage(i, 2.5f);
                        String imgName = pdfPath + pdfList[i].getName().substring(0, dotPos) + ".jpg";
                        ImageIO.write(image, "jpg", new File(imgName));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
