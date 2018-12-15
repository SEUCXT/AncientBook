package com.seu.architecture.service.impl;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.seu.architecture.service.DecompressService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by 17858 on 2017-09-19.
 */
@Service
public class DecompressServiceImpl implements DecompressService {

    @Override
    public List<String> decompress(File file, String destDir){
        List<String> res = new ArrayList<>();
        String fileName = file.getName(); //book.rar
        int dotPos = fileName.lastIndexOf(".");
        String extName = fileName.substring(dotPos + 1);  //rar
        //String name = fileName.substring(0, dotPos);
        if(extName.equals("rar")){
            res = unRar(file,destDir);
        } else if(extName.equals("zip")){
            res = unZip(file,destDir);
        }
        return res;
    }

    @Override
    public List<String> unRar(File file, String destDir){
        List<String> res = new ArrayList<>();
        File outFile = new File(destDir);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        Archive archive = null;
        try {
            archive = new Archive(file);
            if (archive != null) {
                archive.getMainHeader().print();
                FileHeader fileHeader = archive.nextFileHeader();
                while (fileHeader != null) {
                    if (!fileHeader.isDirectory()) {
                        File out = new File(destDir + File.separator + fileHeader.getFileNameString().trim());
                        //System.out.println(out.getAbsolutePath());
                        res.add(out.getAbsolutePath());

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
        }catch(RarException | IOException e){
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<String> unZip(File zipFile, String destDir){

        ZipFile zip = null;
        List<String> res = new ArrayList<>(); //存放解压后的文件路径
        try {
            zip = new ZipFile(zipFile, Charset.forName("GBK"));//解决中文文件夹乱码
           // String name = zip.getName().substring(zip.getName().lastIndexOf('\\')+1, zip.getName().lastIndexOf('.'));

            File pathFile = new File(destDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }

            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String outPath = (destDir + "/" + zipEntryName).replaceAll("\\*", "/");

                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                res.add(outPath);

                FileOutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
