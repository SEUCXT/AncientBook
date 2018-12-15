package com.seu.archiecture;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.seu.architecture.ArchitectureApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import com.github.junrar.exception.*;


/**
 * Created by 17858 on 2017-09-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArchitectureApplication.class)
public class RarZipTest {

    @Test
    public void unRar() throws RarException, IOException {
        String rarFileName = "E:\\book.rar";
        File rarFile = new File(rarFileName);
        System.out.println(rarFile.getAbsolutePath());
        System.out.println(rarFile.getName());
        String outFilePath = "E:\\RarZipTest\\book0917";
        File outFile = new File(outFilePath);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        Archive archive = new Archive(rarFile);
        if(archive != null){
            archive.getMainHeader().print();
            FileHeader fileHeader = archive.nextFileHeader();
            while(fileHeader != null){
                if(!fileHeader.isDirectory()) {
                    File out = new File(outFilePath + File.separator + fileHeader.getFileNameString().trim());
                    System.out.println(out.getAbsolutePath());

                    if (!out.exists()) {
                        if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录。
                            out.getParentFile().mkdirs();
                        }
                        out.createNewFile();
                    }
                    FileOutputStream os = new FileOutputStream(out);
                    archive.extractFile(fileHeader, os);
                    os.close();
                }
                fileHeader = archive.nextFileHeader();
            }
        }
        archive.close();

    }

    public static final int BUFFER_SIZE = 1024;

    @Test
    public void unZip() throws RarException, IOException {
        String zipFilePath = "E:\\AncientBook.zip";
        File zipFile = new File(zipFilePath);
        String destDir = "E:\\RarZipTest\\book0917";

        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));//解决中文文件夹乱码
        String name = zip.getName().substring(zip.getName().lastIndexOf('\\')+1, zip.getName().lastIndexOf('.'));

        File pathFile = new File(destDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
            ZipEntry entry = entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (destDir+"/"+ zipEntryName).replaceAll("\\*", "/");

            // 判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            // 输出文件路径信息
//			System.out.println(outPath);

            FileOutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        System.out.println("******************解压完毕********************");
    }

}
