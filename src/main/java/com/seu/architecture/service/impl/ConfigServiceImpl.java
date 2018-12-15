package com.seu.architecture.service.impl;

import com.seu.architecture.model.Permission;
import com.seu.architecture.model.Role;
import com.seu.architecture.model.ViewObject;
import com.seu.architecture.repository.PermissionRepository;
import com.seu.architecture.repository.RoleRepository;
import com.seu.architecture.service.ConfigService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 17858 on 2017-11-23.
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public ViewObject configPermission(InputStream is){
        ViewObject vo = new ViewObject();
        try {
            List<Permission> permissions = parsePermissionXssf(is);
            permissionRepository.save(permissions);
            vo.set(ViewObject.ERROR,0).set(ViewObject.MESSAGE,"权限初始化成功！");
            LOGGER.info("权限初始化成功！");
        }catch(IOException e){
            e.printStackTrace();
            vo.set(ViewObject.ERROR,1).set(ViewObject.MESSAGE,"权限初始化失败！");
            LOGGER.info("权限初始化失败！");
        }
        return vo;
    }


    @Override
    public ViewObject configRole(InputStream is) {
        ViewObject vo = new ViewObject();
        try {
            List<Role> roles = parseRoleXssf(is);
            roleRepository.save(roles);
            vo.set(ViewObject.ERROR,0).set(ViewObject.MESSAGE,"角色初始化成功！");
            LOGGER.info("角色初始化成功！");
        }catch(IOException e){
            e.printStackTrace();
            vo.set(ViewObject.ERROR,1).set(ViewObject.MESSAGE,"角色初始化失败！");
            LOGGER.info("角色初始化失败！");
        }
        return vo;
    }

    private List<Permission> parsePermissionXssf(InputStream is) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        return parsePermissionSheet(sheet);
    }

    private List<Permission> parsePermissionSheet(Sheet sheet) {
        List<Permission> permissionList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Permission permission = new Permission();
            permission.setName(sheet.getRow(rowNum).getCell(0).getStringCellValue());
            permission.setDisplayName(sheet.getRow(rowNum).getCell(1).getStringCellValue());
            permission.setDescription(sheet.getRow(rowNum).getCell(2).getStringCellValue());
            permissionList.add(permission);
        }
        return permissionList;
    }

    private List<Role> parseRoleXssf(InputStream is) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        return parseRoleSheet(sheet);
    }

    private List<Role> parseRoleSheet(Sheet sheet) {
        List<Role> roleList = new ArrayList<>();
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Role role = new Role();
            role.setName(sheet.getRow(rowNum).getCell(0).getStringCellValue());
            role.setDisplayName(sheet.getRow(rowNum).getCell(1).getStringCellValue());
            role.setDescription(sheet.getRow(rowNum).getCell(2).getStringCellValue());
            String[] permissions = sheet.getRow(rowNum).getCell(3).getStringCellValue().split(";");
            Set<Permission> permissionSet = new HashSet<>();
            for(int i = 0; i < permissions.length; i++) {
                permissionSet.add(permissionRepository.findByname(permissions[i]));
            }
            role.setPermissions(permissionSet);
            roleList.add(role);
        }
        return roleList;
    }
}
