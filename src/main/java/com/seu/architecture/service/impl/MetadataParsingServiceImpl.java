package com.seu.architecture.service.impl;

import com.google.common.hash.Hashing;
import com.seu.architecture.config.Constants;
import com.seu.architecture.model.*;
import com.seu.architecture.repository.RoleRepository;
import com.seu.architecture.repository.UserRepository;
import com.seu.architecture.service.MetadataParsingService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class MetadataParsingServiceImpl implements MetadataParsingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataParsingService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    private List<Book> parseAncientBookSheet(Sheet sheet) {
        List<Book> ancientBookList = new ArrayList<>();

        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            AncientBook ancientBook = new AncientBook();

            long bookid = (long)(sheet.getRow(rowNum).getCell(0).getNumericCellValue());
            ancientBook.setClientId(bookid);

            Cell title = sheet.getRow(rowNum).getCell(1);
            if (title != null) {
                ancientBook.setTitle(title.getStringCellValue());
            }

            Cell author = sheet.getRow(rowNum).getCell(2);
            if (author != null) {
                ancientBook.setAuthor(author.getStringCellValue());
            }

            Cell juanshuCell = sheet.getRow(rowNum).getCell(3);
            if(juanshuCell != null){
                try {
                    ancientBook.setJuanshu((int)(juanshuCell.getNumericCellValue()));
                } catch (NumberFormatException ex) {
                    // TODO 记录
                }
            }

            Cell leibie = sheet.getRow(rowNum).getCell(4);
            if (leibie != null) {
                ancientBook.setLeibie(leibie.getStringCellValue());
            }

            Cell suoshucongshu = sheet.getRow(rowNum).getCell(5);
            if (suoshucongshu != null) {
                ancientBook.setSuoshucongshu(suoshucongshu.getStringCellValue());
            }

            Cell cangshubanben = sheet.getRow(rowNum).getCell(6);
            if (cangshubanben != null) {
                ancientBook.setCangshubanben(cangshubanben.getStringCellValue());
            }

            Cell leixing = sheet.getRow(rowNum).getCell(7);
            if (leixing != null) {
                ancientBook.setLeixing(leixing.getStringCellValue());
            }

            Cell chuban = sheet.getRow(rowNum).getCell(8);
            if (chuban != null) {
                ancientBook.setChuban(chuban.getStringCellValue());
            }

            Cell chengshushijian = sheet.getRow(rowNum).getCell(9);
            if (chengshushijian != null) {
                ancientBook.setChengshushijian(chengshushijian.getStringCellValue());
            }

            Cell chubanshijian = sheet.getRow(rowNum).getCell(10);
            if (chubanshijian != null) {
                ancientBook.setChubanshijian(chubanshijian.getStringCellValue());
            }

            Cell chubandidian = sheet.getRow(rowNum).getCell(11);
            if (chubandidian != null) {
                ancientBook.setChubandidian(chubandidian.getStringCellValue());
            }

            Cell dingzhe = sheet.getRow(rowNum).getCell(12);
            if (dingzhe != null) {
                ancientBook.setDingzhe(dingzhe.getStringCellValue());
            }

            Cell fengleihao = sheet.getRow(rowNum).getCell(13);
            if (fengleihao != null) {
                ancientBook.setFenleihao(fengleihao.getStringCellValue());
            }

            Cell dengluhao = sheet.getRow(rowNum).getCell(14);
            if (dengluhao != null) {
                ancientBook.setDengluhao(dengluhao.getStringCellValue());
            }

            Cell lurushijian = sheet.getRow(rowNum).getCell(15);
            if (lurushijian != null) {
                ancientBook.setLurushiijan(lurushijian.getStringCellValue());
            }

            Cell tiyao = sheet.getRow(rowNum).getCell(16);
            if (tiyao != null) {
                ancientBook.setTiyao(tiyao.getStringCellValue());
            }
            ancientBookList.add(ancientBook);
        }
        return ancientBookList;
    }

    @Override
    public List<Book> parseBookXssf(String type, InputStream is) throws IOException {
        switch(type){
            case "ancient_book":{
                return parseAncientBookXssf(is);
            }
            case "republic_book":{
                return parseRepublicBookXssf(is);
            }
            case "characteristic_book":{
                return parseCharacteristicBookXssf(is);
            }
        }
        return null;
    }

    @Override
    public List<Book> parseAncientBookXssf(InputStream is) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        return parseAncientBookSheet(sheet);
    }

    /**
     * 解析馆藏古籍登记表
     *
     * @param is
     * @return
     */
    @Override
    public List<Book> parseAncientBookHssf(InputStream is) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        HSSFSheet sheet = workbook.getSheetAt(0);
        return parseAncientBookSheet(sheet);
    }

    /**
     * 解析民国图书期刊
     *
     * @param sheet
     * @return
     */
    private List<Book> parseRepublicBookSheet(Sheet sheet) {
        // FIXME 根据登记表读入
        List<Book> republicBooks = new ArrayList<>();

        for (int rowNum = 3; rowNum <= sheet.getLastRowNum(); rowNum++) {
            RepublicBook republicBook = new RepublicBook();

            long bookid = (long)(sheet.getRow(rowNum).getCell(0).getNumericCellValue());
            republicBook.setClientId(bookid);

            Cell title = sheet.getRow(rowNum).getCell(1);
            if (title != null) {
                republicBook.setTitle(title.getStringCellValue());
            }else{
                continue;
            }

            Cell zaitixingtai = sheet.getRow(rowNum).getCell(2);
            if (zaitixingtai != null) {
                republicBook.setZaitixingtai(zaitixingtai.getStringCellValue());
            }

            Cell author = sheet.getRow(rowNum).getCell(3);
            if (author != null) {
                republicBook.setAuthor(author.getStringCellValue());
            }

            Cell chuban = sheet.getRow(rowNum).getCell(4);
            if (chuban != null) {
                republicBook.setChuban(chuban.getStringCellValue());
            }

            Cell chubanshijian = sheet.getRow(rowNum).getCell(5);
            if (chubanshijian != null) {
                if(chubanshijian.getCellType() == Cell.CELL_TYPE_STRING){
                    republicBook.setChubanshijian(chubanshijian.getStringCellValue());
                }else{
                    republicBook.setChubanshijian(String.valueOf(chubanshijian.getNumericCellValue()));
                }
            }

            Cell yuyan = sheet.getRow(rowNum).getCell(6);
            if (yuyan != null) {
                republicBook.setYuyan(yuyan.getStringCellValue());
            }

            Cell dengjidanwei1 = sheet.getRow(rowNum).getCell(7);
            if (dengjidanwei1 != null) {
                republicBook.setDengjidanwei1(dengjidanwei1.getStringCellValue());
            }

            Cell dengjidanwei2 = sheet.getRow(rowNum).getCell(8);
            if (dengjidanwei2 != null) {
                if(dengjidanwei2.getCellType() == Cell.CELL_TYPE_STRING){
                    republicBook.setDengjidanwei2(dengjidanwei2.getStringCellValue());
                }else{
                    republicBook.setDengjidanwei2(String.valueOf(dengjidanwei2.getNumericCellValue()));
                }

            }

            Cell fenleihao = sheet.getRow(rowNum).getCell(9);
            if (fenleihao != null) {
                if(fenleihao.getCellType() == Cell.CELL_TYPE_STRING){
                    republicBook.setFenleihao(fenleihao.getStringCellValue());
                }else{
                    republicBook.setFenleihao(String.valueOf(fenleihao.getNumericCellValue()));
                }
            }

            Cell benshu = sheet.getRow(rowNum).getCell(10);
            if (benshu != null) {
                republicBook.setBenshu((int)(benshu.getNumericCellValue()));
            }

            Cell tuzhang = sheet.getRow(rowNum).getCell(11);
            if (tuzhang != null) {
                republicBook.setTuzhang(tuzhang.getStringCellValue());
            }

            Cell beizhu = sheet.getRow(rowNum).getCell(12);
            if (beizhu != null) {
                if(beizhu.getCellType() == Cell.CELL_TYPE_STRING){
                    republicBook.setBeizhu(beizhu.getStringCellValue());
                }else{
                    republicBook.setBeizhu(String.valueOf(beizhu.getNumericCellValue()));
                }
            }

            Cell xinfenleihao = sheet.getRow(rowNum).getCell(13);
            if (xinfenleihao != null) {
                republicBook.setXinfenleihao(xinfenleihao.getStringCellValue());
            }

            Cell zhaiyao = sheet.getRow(rowNum).getCell(14);
            if (zhaiyao != null) {
                republicBook.setZhaiyao(zhaiyao.getStringCellValue());
            }

            republicBooks.add(republicBook);
        }
        return republicBooks;
    }

    /**
     * 解析民国图书期刊
     *
     * @param is
     * @return
     */
    @Override
    public List<Book> parseRepublicBookXssf(InputStream is) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        return parseRepublicBookSheet(sheet);

    }

    /**
     * 解析民国图书期刊
     *
     * @param is
     * @return
     * @throws IOException
     */
    @Override
    public List<Book> parseRepublicBookHssf(InputStream is) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        HSSFSheet sheet = workbook.getSheetAt(0);
        return parseRepublicBookSheet(sheet);
    }


    private List<Book> parseCharacteristicBookSheet(Sheet sheet) {
        // FIXME 根据登记表读入
        List<Book> characteristicBooks = new ArrayList<>();

        for (int rowNum =1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            CharacteristicBook characteristicBook = new CharacteristicBook();

            long bookid = (long)(sheet.getRow(rowNum).getCell(0).getNumericCellValue());
            characteristicBook.setClientId(bookid);

            Cell title = sheet.getRow(rowNum).getCell(1);
            if (title != null) {
                characteristicBook.setTitle(title.getStringCellValue());
            }

            Cell chubanshijian = sheet.getRow(rowNum).getCell(2);
            if (chubanshijian != null) {
                characteristicBook.setChubanshijian(chubanshijian.getStringCellValue());
            }

            Cell author = sheet.getRow(rowNum).getCell(3);
            if (author != null) {
                characteristicBook.setAuthor(author.getStringCellValue());
            }

            Cell chuban = sheet.getRow(rowNum).getCell(4);
            if (chuban != null) {
                characteristicBook.setChuban(chuban.getStringCellValue());
            }

            Cell leixing = sheet.getRow(rowNum).getCell(5);
            if (leixing != null) {
                characteristicBook.setLeixing(leixing.getStringCellValue());
            }

            Cell tuzhang = sheet.getRow(rowNum).getCell(6);
            if (tuzhang != null) {
                characteristicBook.setTuzhang(tuzhang.getStringCellValue());
            }

            Cell beizhu = sheet.getRow(rowNum).getCell(7);
            if (beizhu != null) {
                characteristicBook.setBeizhu(beizhu.getStringCellValue());
            }

            Cell zhaiyao = sheet.getRow(rowNum).getCell(8);
            if (zhaiyao != null) {
                characteristicBook.setZhaiyao(zhaiyao.getStringCellValue());
            }
            characteristicBooks.add(characteristicBook);
        }
        return characteristicBooks;
    }

    /**
     * 解析特色文献
     *
     * @param is
     * @return
     */
    @Override
    public List<Book> parseCharacteristicBookXssf(InputStream is) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        return parseCharacteristicBookSheet(sheet);
    }

    /**
     * 解析特色文献
     *
     * @param is
     * @return
     */
    @Override
    public List<Book> parseCharacteristicBookHssf(InputStream is) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        HSSFSheet sheet = workbook.getSheetAt(0);
        return parseCharacteristicBookSheet(sheet);
    }

    /**
     * 解析用户列表
     *
     * @param is
     * @return
     */
    @Override
    public List<User> parseUsersXssf(InputStream is) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        return parseUsersSheet(sheet);
    }

    /**
     * 解析用户列表
     *
     * @param is
     * @return
     */
    @Override
    public List<User> parseUsersHssf(InputStream is) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        HSSFSheet sheet = workbook.getSheetAt(0);
        return parseUsersSheet(sheet);
    }

    private List<User> parseUsersSheet(Sheet sheet) {
        // FIXME 根据用户表读入
        List<User> users = new ArrayList<>();
        Long i = 0L;
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            User user = new User();
            user.setUsername(sheet.getRow(rowNum).getCell(0).toString());
            user.setSalt(UUID.randomUUID().toString().substring(0, 5));
            String password = sheet.getRow(rowNum).getCell(1).toString();
            user.setPassword(Hashing.md5().hashUnencodedChars(password + user.getSalt()).toString());
            Set<Role> defaultRole = new HashSet<>();
            String roleStr = sheet.getRow(rowNum).getCell(2).toString();
            Role role = new Role();
            switch (roleStr) {
                case "普通用户": {
                    role = roleRepository.findByName(Constants.PRIMARY_USER_ROLE);
                    break;
                }
                case "中级用户": {
                    role = roleRepository.findByName(Constants.MIDDLE_USER_ROLE);
                    break;
                }
                case "高级用户": {
                    role = roleRepository.findByName(Constants.SENIOR_USER_ROLE);
                    break;
                }
            }
            defaultRole.add(role);
            user.setRoles(defaultRole);
            user.setStatus(Constants.OPEN_USER_STATUS);

            users.add(user);
        }
        return users;
    }
}
