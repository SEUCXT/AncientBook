package com.seu.archiecture;

import com.seu.architecture.ArchitectureApplication;
import com.seu.architecture.config.Constants;
import com.seu.architecture.service.PdfService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 17858 on 2017/5/15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArchitectureApplication.class)
public class PdfServiceTest {

    @Autowired
    PdfService pdfService;

    @Test
    public void pdfToImageTest(){
       // pdfService.pdfToImage("000274 ELEMENTARY WATER COLOUR PAINTING-0001.pdf");
        pdfService.pdfToImageFolder(Constants.PDF_DIR);

    }
}
